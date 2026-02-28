package com.sharko.yura.taskflow.dto;

import com.sharko.yura.taskflow.entity.TaskPriority;
import com.sharko.yura.taskflow.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskFilterDTO {

    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private Long creatorId;
    private Long executorId;
    private LocalDateTime deadlineFrom;
    private LocalDateTime deadlineTo;

}
