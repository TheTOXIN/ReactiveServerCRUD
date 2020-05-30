package com.toxin.reactive.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employees")
public class Employee {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String photo;

    @NotNull
    private String position;

    @NotNull
    private int salary;

    @NotNull
    private boolean work;

    @NotNull
    private LocalDateTime hired;
}
