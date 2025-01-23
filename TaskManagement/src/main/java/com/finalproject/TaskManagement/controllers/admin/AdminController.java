package com.finalproject.TaskManagement.controllers.admin;

import com.finalproject.TaskManagement.dtos.CommentDTO;
import com.finalproject.TaskManagement.dtos.TaskDTO;
import com.finalproject.TaskManagement.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PostMapping("/task")
    public ResponseEntity<?> postTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdtaskDTO = adminService.postTask(taskDTO);
        if (createdtaskDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdtaskDTO);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks() {
        return ResponseEntity.ok(adminService.getTasks());
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getTaskById(id));
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable Long id) {
        adminService.deleteTaskById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/tasks/search/{title}")
    public ResponseEntity<?> searchTaskByTitle(@PathVariable String title) {
        return ResponseEntity.ok(adminService.searchTaskByTitle(title));
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTaskDTO = adminService.updateTask(id, taskDTO);
        if (updatedTaskDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedTaskDTO);
    }

    @PostMapping("/task/comment")
    public ResponseEntity<?> createComment(@RequestParam Long taskId, @RequestParam Long postedBy, @RequestBody String content) {
        CommentDTO createdCommentDTO = adminService.createComment(taskId, postedBy, content);
        if (createdCommentDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDTO);
    }

    @GetMapping("/task/{taskId}/comments")
    public ResponseEntity<?> getCommentsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(adminService.getCommentsByTask(taskId));
    }
}
