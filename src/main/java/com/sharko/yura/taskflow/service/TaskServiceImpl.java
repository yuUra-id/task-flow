package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.*;
import com.sharko.yura.taskflow.entity.*;
import com.sharko.yura.taskflow.exception.TaskNotFoundException;
import com.sharko.yura.taskflow.exception.UserNotFoundException;
import com.sharko.yura.taskflow.repository.TaskRepository;
import com.sharko.yura.taskflow.repository.UserRepository;
import com.sharko.yura.taskflow.repository.specification.TaskRoleSpecification;
import com.sharko.yura.taskflow.repository.specification.TaskSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {

        this.taskRepository = taskRepository;
        this.userRepository = userRepository;

    }

    @Override
    @Transactional
    public TaskResponseDTO create(TaskCreateDTO taskCreateDTO, String usernameCreator) {

        log.info("TASK: attempt create task by user {}", usernameCreator);

        Task task = new Task();

        log.debug("TASK: finding creator with username {}", usernameCreator);
        User creator = userRepository.findByUsername(usernameCreator);
        if(creator == null) {

            log.warn("TASK: creator with username: {} not found", usernameCreator);
            throw new UserNotFoundException("Creator not found");

        }

        log.debug("TASK: setting task fields: title {}, priority {}, deadline {}, status and description",
                taskCreateDTO.getTitle(), taskCreateDTO.getPriority(), taskCreateDTO.getDeadline());
        task.setTitle(taskCreateDTO.getTitle());
        task.setDescription(taskCreateDTO.getDescription());
        task.setDeadline(taskCreateDTO.getDeadline());
        TaskPriority taskPriority = taskCreateDTO.getPriority() != null
                ? taskCreateDTO.getPriority(): TaskPriority.LOW;
        task.setPriority(taskPriority);
        task.setStatus(TaskStatus.NEW);

        log.debug("TASK: linking created task to creator {}", usernameCreator);
        creator.addCreatedTask(task);

        log.debug("TASK: checking executor assignment, executorId {} ", taskCreateDTO.getExecutorId());
        if(taskCreateDTO.getExecutorId() != null) {

            log.debug("TASK: finding executor with ID: {}", taskCreateDTO.getExecutorId());
            User executor = userRepository.findById(taskCreateDTO.getExecutorId())
                    .orElseThrow(()-> new UserNotFoundException("Executor not found"));
            executor.addExecutorTask(task);

        }else {

            log.warn("TASK: executor with ID: {} not found", taskCreateDTO.getExecutorId());

        }

        log.debug("TASK: saving new task");
        Task savedTask = taskRepository.save(task);

        log.info("TASK: task with ID {} successfully created by user {}", savedTask.getId(), usernameCreator);
        return mapToTaskResponseDTO(savedTask);

    }

    @Override
    public PageResponseDTO<TaskResponseDTO> getAllTasks(String username, TaskFilterDTO taskFilterDTO,
                                                        Pageable pageable) {

        log.info("TASK: attempt get all tasks for user {}", username);

        log.debug("TASK: finding user with username {}", username);
        User user = userRepository.findByUsername(username);

        if(user == null) {

            throw new UserNotFoundException("User not found");

        }

        if(taskFilterDTO != null){

            log.debug("TASK: applying filters status = {}, priority = {}, executorId = {}, " +
                    "deadlineFrom = {}, deadlineTo = {}", taskFilterDTO.getTaskStatus(),
                    taskFilterDTO.getTaskPriority(), taskFilterDTO.getExecutorId(),
                    taskFilterDTO.getDeadlineFrom(), taskFilterDTO.getDeadlineTo());

        }else {

            log.debug("TASK: no filters provided");

        }

        log.debug("TASK: pageable params page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        log.debug("TASK: building filter role-based specification");
        Specification<Task> roleSpec = TaskRoleSpecification.forUser(user);

        log.debug("TASK: building filter specification");
        Specification<Task> filterSpec = TaskSpecification.build(taskFilterDTO);

        Specification<Task> finalSpec = roleSpec.and(filterSpec);

        log.debug("TASK: executing search for tasks");
        Page<Task> tasks =  taskRepository.findAll(finalSpec, pageable);

        Page<TaskResponseDTO> dtoPage = tasks.map(this::mapToTaskResponseDTO);

        log.info("TASK: successfully fetched {} tasks for user {}",
                tasks.getTotalElements(), username);
        return PageResponseDTO.mapToPageResponse(dtoPage);

    }

    @Override
    @Transactional
    public TaskResponseDTO update(Long taskId, TaskUpdateDTO taskUpdateDTO, String username) {

        log.info("TASK: attempt update task with ID {} by user {}", taskId, username);

        log.debug("TASK: finding user with username {}", username);
        //Получаем пользователя и проверяем есть или нет
        User user = userRepository.findByUsername(username);
        if (user == null) {

            log.warn("TASK: user with username {} not found", username);
            throw new UserNotFoundException("User not found");

        }

        log.debug("TASK: finding task with ID {}", taskId);
        //Находим задачу которую нужно обновить
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("TASK: task with ID {} not found for update", taskId);
                    return new TaskNotFoundException("Task not found");
                });

        log.debug("TASK: user {} has role {}", username, user.getRole());
        //Проверяем роли, если админ или менеджер то можно все менять и всех задач
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER){

            log.debug("TASK: user {} has elevated role, full update allowed for task ID {}",
                    username, taskId);
            task = taskUpdateDTOMapTask(taskUpdateDTO, task);

        } else if (user.getRole() == Role.USER){//Если изменения пытается внести обычный пользователь
            //разрешаем менять толь статус и только у своей задачи
            //вот тут проверяем его это задача или нет
            log.debug("TASK: user {} has USER role, only own task status update allowed",
                    username);

            if(task.getExecutor() != null && user.getId().equals(task.getExecutor().getId())) {

                log.debug("TASK: user {} is executor of task ID {}, checking status update", username, taskId);

                if (taskUpdateDTO.getStatus() != null) {

                    log.debug("TASK: updating status for task ID {} to {}", taskId, taskUpdateDTO);
                    task.setStatus(taskUpdateDTO.getStatus());

                }else {

                    log.debug("TASK: no status provided for task ID {} by user", taskId);

                }

            }else {

                log.warn("TASK: access denied for user {} when updating task with ID {}", username, taskId);
                throw new AccessDeniedException("Access denied");

            }

        }else {

            log.warn("TASK: access enied for user {} due to unsupported role {}", username, user.getRole());
            throw new AccessDeniedException("Access denied");

        }

        log.debug("TASK: saving update task with ID {}", taskId);
        Task updatedTask = taskRepository.save(task);

        log.info("TASK: task with ID {} successfully update by user {}", taskId, username);
        return mapToTaskResponseDTO(updatedTask);

    }

    @Override
    @Transactional
    public void delete(Long taskId) {

        log.info("TASK: attempt delete task with ID {}", taskId);

        log.debug("TASK: finding task with ID {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("TASK: task with ID {} not found for delete", taskId);
                    return new TaskNotFoundException("Task not found");
                });

        User creator = task.getCreator();
        if(creator != null) {

            log.debug("TASK: removing task ID {} from creator {}", taskId, creator.getUsername());
            creator.removeCreatedTask(task);

        }else {

            log.debug("TASK: task ID {} has no creator", taskId);

        }

        User executor = task.getExecutor();
        if(executor != null) {

            log.debug("TASK: removing task ID {} from executor {}", taskId, executor.getUsername());
            executor.removeExecutorTask(task);

        }else {

            log.debug("TASK: task ID {} has no executor", taskId);

        }

        log.debug("TASK: deleting task with ID {}", taskId);
        taskRepository.delete(task);

        log.info("TASK: successfully deleting task with ID {}", taskId);

    }

    @Override
    public TaskResponseDTO findByIdTask(Long taskId, String username) {

        log.info("TASK: attempt get task with ID {} by user {}", taskId, username);

        log.debug("TASK: finding task with ID {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->{
                    log.warn("TASK: task with ID {} not found", taskId);
                    return new TaskNotFoundException("Task not found");
                });

        TaskResponseDTO taskResponseDTO;

        log.debug("TASK: finding user with username {}", username);
        User user = userRepository.findByUsername(username);

        if(user == null) {

            log.warn("TASK: user with username {} not found", username);
            throw new UserNotFoundException("User not found");

        }

        log.debug("TASK: user {} has role {}", username, user.getRole());

        if(user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER) {

            log.debug("TASK: access granted for user {} to task ID {} due to elevated role {}",
                    username, taskId, user.getRole());
            taskResponseDTO = mapToTaskResponseDTO(task);

        }else {

            if(task.getExecutor() != null && user.getId().equals(task.getExecutor().getId())){

                log.debug("TASK: access granted for user {} to own task ID {}", username, taskId);
                taskResponseDTO = mapToTaskResponseDTO(task);

            }else  {

                log.warn("TASK: access denied for user {} to task ID {}", username, taskId);
                throw new AccessDeniedException("Access denied");

            }

        }

        log.info("TASK: task with ID {} successfully returned to user {}", taskId, username);
        return taskResponseDTO;

    }


    //Вспомогательный метод для обновления
    private Task taskUpdateDTOMapTask(TaskUpdateDTO taskUpdateDTO, Task task){
        //Тут происходит проверка есть ли данные для обновления и если есть, то устанавливаем их
        if (taskUpdateDTO.getTitle() != null) task.setTitle(taskUpdateDTO.getTitle());
        if (taskUpdateDTO.getDescription() != null) task.setDescription(taskUpdateDTO.getDescription());
        if (taskUpdateDTO.getDeadline() != null) task.setDeadline(taskUpdateDTO.getDeadline());
        if (taskUpdateDTO.getPriority() != null) task.setPriority(taskUpdateDTO.getPriority());
        if (taskUpdateDTO.getStatus() != null) task.setStatus(taskUpdateDTO.getStatus());
        //Тут проверяем есть ли исполнитель у задачи
        if (taskUpdateDTO.getExecutorID() != null) {
            //Если исполнитель установлен нужно проверить существует ли он.
            User executor = userRepository.findById(taskUpdateDTO.getExecutorID())
                    .orElseThrow(() -> new UserNotFoundException("Executor not found"));
            //Получаем старого исполнителя и удаляем у него задачу
            User oldExecutor = task.getExecutor();

            if (oldExecutor != null) {
                oldExecutor.removeExecutorTask(task);
            }
            //новый исполнитель установка
            executor.addExecutorTask(task);


        }

        return task;
    }

    // Внутренний метод для преобразования task в TaskResponseDTO
    private TaskResponseDTO mapToTaskResponseDTO(Task task){

        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();

        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setTitle(task.getTitle());
        taskResponseDTO.setDescription(task.getDescription());
        taskResponseDTO.setPriority(task.getPriority());
        taskResponseDTO.setStatus(task.getStatus());
        taskResponseDTO.setDeadline(task.getDeadline());
        taskResponseDTO.setCreationDate(task.getCreatedAt());

        if(task.getExecutor() != null){
            taskResponseDTO.setExecutorUsername(task.getExecutor().getUsername());
        }
        if(task.getCreator() != null){
            taskResponseDTO.setCreatorUsername(task.getCreator().getUsername());
        }

        return taskResponseDTO;

    }

}
