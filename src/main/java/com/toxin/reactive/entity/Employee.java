package com.toxin.reactive.entity;

import com.toxin.reactive.constant.EmployeeActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employees")
public class Employee {

    @Id
    private String id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String photo;

    @NotNull
    @NotBlank
    private String position;

    @NotNull
    private EmployeeActivity activity;

    @Min(1)
    @NotNull
    private int salary;

    @NotNull
    private boolean work;

    @NotNull
    private LocalDate hired;
}
