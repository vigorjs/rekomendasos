package com.virgo.todoapp.utils.dto;

import com.virgo.todoapp.entity.enums.TaskStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    @NotNull(message = "name gabole kosong")
    private String name;

    @NotNull(message = "date cant be empty")
    private Date date;

    @Nullable
    private Date deadline;

    @Nullable
    private TaskStatus status;

}
