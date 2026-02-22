package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;
import com.sharko.yura.taskflow.entity.Task;
import com.sharko.yura.taskflow.entity.TaskPriority;
import com.sharko.yura.taskflow.entity.TaskStatus;
import com.sharko.yura.taskflow.repository.TaskRepository;
import com.sharko.yura.taskflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TaskResponseDTO create(TaskCreateDTO taskCreateDTO, Long creatorId) {

        Task task = new Task();

        task.setTitle(taskCreateDTO.getTitle());
        task.setDescription(taskCreateDTO.getDescription());
        task.setPriority(taskCreateDTO.getPriority());
        task.setDeadline(taskCreateDTO.getDeadline());
        TaskPriority taskPriority = taskCreateDTO.getPriority() != null
                ? taskCreateDTO.getPriority(): TaskPriority.LOW;
        task.setPriority(taskPriority);
        task.setStatus(TaskStatus.NEW);

        task.setCreator(userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator user not found!")));

        Long ExecutorId = taskCreateDTO.getExecutorId();
        if(ExecutorId != null){
            task.setExecutor(userRepository.findById(ExecutorId)
                .orElseThrow(() -> new RuntimeException("Executor user not found!")));
        }

        Task savedTask = taskRepository.save(task);

        return mapToTaskResponseDTO(savedTask);

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
