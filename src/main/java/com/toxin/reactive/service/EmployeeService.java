package com.toxin.reactive.service;

import com.toxin.reactive.entity.Employee;
import com.toxin.reactive.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Mono<Employee> find(String id) {
        return employeeRepository.findById(id);
    }

    public Mono<Employee> save(Employee employee) {
        employee.setId(null);
        employee.setWork(true);
        employee.setHired(LocalDateTime.now());

        return employeeRepository.save(employee);
    }

    public Mono<Employee> edit(String id, Employee employee) {
        return employeeRepository.findById(id).doOnNext(exist -> {
            exist.setPosition(employee.getPosition());
            exist.setSalary(employee.getSalary());
            exist.setPhoto(employee.getPhoto());
            exist.setWork(employee.isWork());
        }).flatMap(employeeRepository::save);
    }

    public Mono<Void> remove(String id) {
        return employeeRepository.deleteById(id);
    }
}
