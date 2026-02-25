package com.sharko.yura.taskflow.controller;

import com.sharko.yura.taskflow.dto.TaskCreateDTO;
import com.sharko.yura.taskflow.dto.TaskResponseDTO;
import com.sharko.yura.taskflow.dto.TaskUpdateDTO;
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

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResponseDTO> updateTask(@AuthenticationPrincipal UserDetails userDetails,
                                                      @Valid @RequestBody TaskUpdateDTO taskUpdateDTO,
                                                      @PathVariable("id") Long taskID) {

        TaskResponseDTO taskResponseDTO = taskService.update(taskID, taskUpdateDTO, userDetails.getUsername());
        return ResponseEntity.ok().body(taskResponseDTO);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long taskID) {

        taskService.delete(taskID);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResponseDTO> getByIdTask(@PathVariable("id") Long taskID,
                                                       @AuthenticationPrincipal UserDetails userDetails) {

        TaskResponseDTO taskResponseDTO = taskService.findByIdTask(taskID, userDetails.getUsername());

        return ResponseEntity.ok(taskResponseDTO);

    }

}
