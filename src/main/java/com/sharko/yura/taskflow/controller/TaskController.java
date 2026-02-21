package com.sharko.yura.taskflow.controller;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;
import com.sharko.yura.taskflow.repository.UserRepository;
import com.sharko.yura.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final UserRepository userRepository;
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO,
                                                      @AuthenticationPrincipal UserDetails userDetails) {

        Long creatorId = userRepository.findByUsername(userDetails.getUsername()).getId();
        TaskResponseDTO taskResponseDTO = taskService.create(taskCreateDTO, creatorId);

        return ResponseEntity.ok(taskResponseDTO);

    }

}
