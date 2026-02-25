package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;
import com.sharko.yura.taskflow.dto.TaskUpdateDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO create(TaskCreateDTO taskCreateDTO, String usernameCreator);

    List<TaskResponseDTO> getAllTasks(String username);

    TaskResponseDTO update(Long taskId, TaskUpdateDTO taskUpdateDTO, String username);

    void delete(Long taskId);

}
