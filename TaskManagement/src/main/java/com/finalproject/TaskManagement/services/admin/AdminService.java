package com.finalproject.TaskManagement.services.admin;

import com.finalproject.TaskManagement.dtos.CommentDTO;
import com.finalproject.TaskManagement.dtos.TaskDTO;
import com.finalproject.TaskManagement.dtos.UserDTO;

import java.util.List;

public interface AdminService {

    List<UserDTO> getUsers();

    TaskDTO postTask(TaskDTO taskDTO);

    List<TaskDTO> getTasks();

    TaskDTO getTaskById(Long id);

    void deleteTaskById(Long id);

    List<TaskDTO> searchTaskByTitle(String title);

    TaskDTO updateTask(Long id, TaskDTO taskDTO);

    CommentDTO createComment(Long taskId, Long postedBy, String content);

    List<CommentDTO> getCommentsByTask(Long taskId);
}
