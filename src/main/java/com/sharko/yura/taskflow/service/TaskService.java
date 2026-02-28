package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.PageResponseDTO;
import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;
import com.sharko.yura.taskflow.dto.TaskUpdateDTO;
import org.springframework.data.domain.Pageable;


public interface TaskService {

    TaskResponseDTO create(TaskCreateDTO taskCreateDTO, String usernameCreator);

    PageResponseDTO<TaskResponseDTO> getAllTasks(String username, Pageable pageable);

    TaskResponseDTO update(Long taskId, TaskUpdateDTO taskUpdateDTO, String username);

    void delete(Long taskId);

    TaskResponseDTO findByIdTask(Long taskId, String username);

    PageResponseDTO<TaskResponseDTO> findAllTasksExecutor(Long executorId, Pageable pageable);

    PageResponseDTO<TaskResponseDTO> findAllMyTasks(String username, Pageable pageable);

}
