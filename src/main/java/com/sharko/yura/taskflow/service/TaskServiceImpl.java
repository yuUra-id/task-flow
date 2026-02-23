package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;
import com.sharko.yura.taskflow.entity.*;
import com.sharko.yura.taskflow.exception.UserAlreadyExistsException;
import com.sharko.yura.taskflow.exception.UserNotFoundException;
import com.sharko.yura.taskflow.repository.TaskRepository;
import com.sharko.yura.taskflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
