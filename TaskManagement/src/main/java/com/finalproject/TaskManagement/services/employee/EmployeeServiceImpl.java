package com.finalproject.TaskManagement.services.employee;

import com.finalproject.TaskManagement.dtos.CommentDTO;
import com.finalproject.TaskManagement.dtos.TaskDTO;
import com.finalproject.TaskManagement.entities.Comment;
import com.finalproject.TaskManagement.entities.Task;
import com.finalproject.TaskManagement.entities.User;
import com.finalproject.TaskManagement.enums.TaskStatus;
import com.finalproject.TaskManagement.repository.CommentRepository;
import com.finalproject.TaskManagement.repository.TaskRepository;
import com.finalproject.TaskManagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    @Override
    public List<TaskDTO> getTasksByUserId(Long id) {
        return taskRepository.findAllByUserId(id).stream().map(Task::getTaskDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(Long id, String status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            TaskStatus taskStatus = mapStringToTaskStatus(String.valueOf(status));
            existingTask.setTaskStatus(taskStatus);
            return taskRepository.save(existingTask).getTaskDTO();
        }
        return null;
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        return taskRepository.findById(id).map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public CommentDTO createComment(Long taskId, Long postedBy, String content) {
        // Получаем текущего пользователя из SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new AccessDeniedException("Доступ запрещен: пользователь не аутентифицирован.");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> optionalCurrentUser = userRepository.findFirstByEmail(userDetails.getUsername());

        if (optionalCurrentUser.isEmpty() || !optionalCurrentUser.get().getId().equals(postedBy)) {
            throw new AccessDeniedException("Доступ запрещен: нельзя публиковать комментарии от имени другого пользователя.");
        }
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Optional<User> optionalUser = userRepository.findById(postedBy);
        if (optionalTask.isPresent() &&  optionalUser.isPresent()) {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCreatedAt(new Date());
            comment.setUser(optionalUser.get());
            comment.setTask(optionalTask.get());
            return commentRepository.save(comment).getCommentDTO();
        }
        throw  new EntityNotFoundException("Пользователь или задача не найдены!");
    }

    @Override
    public List<CommentDTO> getCommentsByTask(Long taskId) {
        return commentRepository.findAllByTaskId(taskId).stream().map(Comment::getCommentDTO).collect(Collectors.toList());
    }

    private TaskStatus mapStringToTaskStatus(String taskStatus) {
        return switch (taskStatus) {
            case "PENDING" -> TaskStatus.PENDING;
            case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED" -> TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELED;
        };
    }
}
