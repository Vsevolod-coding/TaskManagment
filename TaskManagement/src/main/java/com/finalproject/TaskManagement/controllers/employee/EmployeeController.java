package com.finalproject.TaskManagement.controllers.employee;

import com.finalproject.TaskManagement.dtos.CommentDTO;
import com.finalproject.TaskManagement.dtos.TaskDTO;
import com.finalproject.TaskManagement.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTasksByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getTasksByUserId(id));
    }

    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @PathVariable String status) {
        TaskDTO updatedTaskDTO = employeeService.updateTask(id, status);
        if (updatedTaskDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedTaskDTO);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getTaskById(id));
    }

    @PostMapping("/task/comment")
    public ResponseEntity<?> createComment(@RequestParam Long taskId, @RequestParam Long postedBy, @RequestBody String content) {
        CommentDTO createdCommentDTO = employeeService.createComment(taskId, postedBy, content);
        if (createdCommentDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDTO);
    }

    @GetMapping("/task/{taskId}/comments")
    public ResponseEntity<?> getCommentsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(employeeService.getCommentsByTask(taskId));
    }
}
