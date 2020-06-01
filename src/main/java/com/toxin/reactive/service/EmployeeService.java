package com.toxin.reactive.service;

import com.toxin.reactive.constant.EmployeeActivity;
import com.toxin.reactive.entity.Employee;
import com.toxin.reactive.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.toxin.reactive.util.EmployeeUtils.getAvatar;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Value("${employees.stream.duration:1000}")
    private Long streamDuration;

    public Mono<Employee> find(String id) {
        return employeeRepository.findById(id);
    }

    public Mono<Employee> save(Employee employee) {
        employee.setId(null);
        employee.setWork(true);
        employee.setPhoto(getAvatar(employee));
        employee.setActivity(EmployeeActivity.random());

        return employeeRepository.save(employee);
    }

    public Mono<Employee> edit(String id, Employee employee) {
        return employeeRepository.findById(id).doOnNext(exist -> {
            exist.setPosition(employee.getPosition());
            exist.setSalary(employee.getSalary());
            exist.setHired(employee.getHired());
            exist.setName(employee.getName());
            exist.setWork(employee.isWork());
        }).flatMap(employeeRepository::save);
    }

    public Mono<Void> remove(String id) {
        return employeeRepository.deleteById(id);
    }

    public Flux<Employee> findAll() {
        return employeeRepository.findAllByOrderByHired();
    }

    public Flux<Employee> stream() {
         return employeeRepository
             .findAllByOrderByHired()
             .delayElements(Duration.ofMillis(streamDuration));
    }
}
