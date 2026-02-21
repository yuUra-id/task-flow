package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;

public interface TaskService {

    TaskCreateDTO createTask(TaskCreateDTO taskCreateDTO);

}
