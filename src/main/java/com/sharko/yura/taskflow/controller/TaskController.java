package com.sharko.yura.taskflow.controller;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;
import com.sharko.yura.taskflow.repository.UserRepository;
import com.sharko.yura.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO,
                                                      @AuthenticationPrincipal UserDetails userDetails) {

        TaskResponseDTO taskResponseDTO = taskService.create(taskCreateDTO, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponseDTO);

    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@AuthenticationPrincipal UserDetails userDetails) {

        List<TaskResponseDTO> taskResponseDTOList = taskService.getAllTasks(userDetails.getUsername());
        return ResponseEntity.ok(taskResponseDTOList);

    }

}
