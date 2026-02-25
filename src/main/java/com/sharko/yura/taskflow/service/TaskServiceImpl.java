package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;
import com.sharko.yura.taskflow.dto.TaskUpdateDTO;
import com.sharko.yura.taskflow.entity.*;
import com.sharko.yura.taskflow.exception.TaskNotFoundException;
import com.sharko.yura.taskflow.exception.UserNotFoundException;
import com.sharko.yura.taskflow.repository.TaskRepository;
import com.sharko.yura.taskflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TaskResponseDTO create(TaskCreateDTO taskCreateDTO, String usernameCreator) {

        Task task = new Task();

        User creator = userRepository.findByUsername(usernameCreator);
        if(creator == null) {

            throw new UserNotFoundException("Creator not found");

        }

        task.setTitle(taskCreateDTO.getTitle());
        task.setDescription(taskCreateDTO.getDescription());
        task.setDeadline(taskCreateDTO.getDeadline());
        TaskPriority taskPriority = taskCreateDTO.getPriority() != null
                ? taskCreateDTO.getPriority(): TaskPriority.LOW;
        task.setPriority(taskPriority);
        task.setStatus(TaskStatus.NEW);

        creator.addCreatedTask(task);

        if(taskCreateDTO.getExecutorId() != null) {

            User executor = userRepository.findById(taskCreateDTO.getExecutorId())
                    .orElseThrow(()-> new UserNotFoundException("Executor not found"));
            executor.addExecutorTask(task);
        }

        Task savedTask = taskRepository.save(task);

        return mapToTaskResponseDTO(savedTask);

    }

    @Override
    public List<TaskResponseDTO> getAllTasks(String username) {

        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException("User not found");
        }
        List<Task> tasks;
        if(user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER) {

            tasks = taskRepository.findAll();

        }else{

            tasks = new ArrayList<>();
            tasks.addAll(taskRepository.findByExecutorId(user.getId()));

        }

        List<TaskResponseDTO> taskResponseDTOList = tasks
                .stream().map(this::mapToTaskResponseDTO).toList();

        return taskResponseDTOList;

    }

    @Override
    @Transactional
    public TaskResponseDTO update(Long taskId, TaskUpdateDTO taskUpdateDTO, String username) {

        //Получаем пользователя и проверяем есть или нет
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        //Находим задачу которую нужно обновить
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        //Проверяем роли, если админ или менеджер то можно все менять и всех задач
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER){

            task = taskUpdateDTOMapTask(taskUpdateDTO, task);

        } else if (user.getRole() == Role.USER){//Если изменения пытается внести обычный пользователь
            //разрешаем менять толь статус и только у своей задачи
            //вот тут проверяем его это задача или нет
            if(task.getExecutor() != null && user.getId().equals(task.getExecutor().getId())) {

                if (taskUpdateDTO.getStatus() != null) task.setStatus(taskUpdateDTO.getStatus());

            }else {

                throw new AccessDeniedException("Access denied");

            }

        }else {

            throw new AccessDeniedException("Access denied");

        }

        return mapToTaskResponseDTO(task);

    }

    @Override
    @Transactional
    public void delete(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        User creator = task.getCreator();
        if(creator != null) {
            creator.removeCreatedTask(task);
        }
        User executor = task.getExecutor();
        if(executor != null) {
            executor.removeExecutorTask(task);
        }

        taskRepository.delete(task);

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
