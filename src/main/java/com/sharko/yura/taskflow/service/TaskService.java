package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;

public interface TaskService {

    TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO, Long creatorId);

}
