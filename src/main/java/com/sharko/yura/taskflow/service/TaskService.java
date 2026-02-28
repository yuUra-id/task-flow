package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.*;
import org.springframework.data.domain.Pageable;


public interface TaskService {

    TaskResponseDTO create(TaskCreateDTO taskCreateDTO, String usernameCreator);

    PageResponseDTO<TaskResponseDTO> getAllTasks(String username, TaskFilterDTO taskFilterDTO, Pageable pageable);

    TaskResponseDTO update(Long taskId, TaskUpdateDTO taskUpdateDTO, String username);

    void delete(Long taskId);

    TaskResponseDTO findByIdTask(Long taskId, String username);

}
