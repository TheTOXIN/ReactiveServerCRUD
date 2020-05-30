package com.toxin.reactive.controller;

import com.toxin.reactive.entity.Employee;
import com.toxin.reactive.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Bean
    RouterFunction<ServerResponse> create() {
        return route(POST("/employees"), req -> ok().body(
            req.bodyToMono(Employee.class).flatMap(
                employeeService::save
            ),
            Employee.class
        ));
    }

    @Bean
    RouterFunction<ServerResponse> read() {
        return route(GET("/employees/{id}"), req -> ok().body(
            employeeService.find(req.pathVariable("id")),
            Employee.class
        ));
    }

    @Bean
    RouterFunction<ServerResponse> update() {
        return route(PUT("/employees/{id}"), req -> ok().body(
            req.bodyToMono(Employee.class).flatMap(e ->
                employeeService.edit(req.pathVariable("id"), e)
            ),
            Employee.class
        ));
    }

    @Bean
    RouterFunction<ServerResponse> delete() {
        return route(DELETE("/employees/{id}"), req -> ok().body(
            employeeService.remove(req.pathVariable("id")),
            Void.class
        ));
    }
}
