package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO create(TaskCreateDTO taskCreateDTO, String usernameCreator);

    List<TaskResponseDTO> getAllTasks(String username);

}
