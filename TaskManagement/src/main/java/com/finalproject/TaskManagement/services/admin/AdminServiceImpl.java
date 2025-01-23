package com.finalproject.TaskManagement.services.admin;

import com.finalproject.TaskManagement.dtos.CommentDTO;
import com.finalproject.TaskManagement.dtos.TaskDTO;
import com.finalproject.TaskManagement.dtos.UserDTO;
import com.finalproject.TaskManagement.entities.Comment;
import com.finalproject.TaskManagement.entities.Task;
import com.finalproject.TaskManagement.entities.User;
import com.finalproject.TaskManagement.enums.TaskStatus;
import com.finalproject.TaskManagement.enums.UserRole;
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
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final CommentRepository commentRepository;

    @Override
    public List<UserDTO> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(User::getUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO postTask(TaskDTO taskDTO) {
        Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
        if (optionalUser.isPresent()) {
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setDueDate(taskDTO.getDueDate());
            task.setUser(optionalUser.get());
            task.setTaskStatus(TaskStatus.PENDING);
            return taskRepository.save(task).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> getTasks() {
        return taskRepository.findAll().stream().map(Task::getTaskDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTO> searchTaskByTitle(String title) {
        return taskRepository.findAllByTitleContaining(title).stream().map(Task::getTaskDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
        if (optionalTask.isPresent() && optionalUser.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setPriority(taskDTO.getPriority());
            existingTask.setUser(optionalUser.get());
            TaskStatus taskStatus = mapStringToTaskStatus(String.valueOf(taskDTO.getTaskStatus()));
            existingTask.setTaskStatus(taskStatus);
            return taskRepository.save(existingTask).getTaskDTO();
        }
        return null;
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
        throw new EntityNotFoundException("Пользователь или задача не найдены!");
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
