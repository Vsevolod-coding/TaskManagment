package com.finalproject.TaskManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finalproject.TaskManagement.dtos.TaskDTO;
import com.finalproject.TaskManagement.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Date dueDate;

    private String description;

    private String priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    private TaskStatus taskStatus;

    public TaskDTO getTaskDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(id);
        taskDTO.setTitle(title);
        taskDTO.setTaskStatus(taskStatus);
        taskDTO.setEmployeeName(user.getName());
        taskDTO.setEmployeeId(user.getId());
        taskDTO.setDueDate(dueDate);
        taskDTO.setPriority(priority);
        taskDTO.setDescription(description);
        return taskDTO;
    }

}
