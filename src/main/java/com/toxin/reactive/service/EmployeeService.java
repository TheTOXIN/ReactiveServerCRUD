package com.toxin.reactive.service;

import com.toxin.reactive.entity.Employee;
import com.toxin.reactive.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.toxin.reactive.util.EmployeeUtils.getAvatar;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final static String EVENT_HIRED = "hired-employee";
    private final static String EVENT_FIRED = "fired-employee";

    private final List<Employee> firedEmployees = new ArrayList<>();
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
        return employeeRepository.findById(id)
            .doOnNext(firedEmployees::add)
            .flatMap(employeeRepository::delete);
    }

    public Flux<Employee> findAll() {
        return employeeRepository.findAllByOrderByHired();
    }

    public Flux<Employee> stream() {
        Flux<Employee> employeesFlux = Flux.merge(
            employeeRepository.findAllByOrderByHired().doOnNext(e -> e.setEvent(EVENT_HIRED)),
            Flux.fromIterable(firedEmployees).doOnNext(e -> e.setEvent(EVENT_FIRED))
        );

        return employeeRepository.count()
            .map(Function.identity())
            .flatMapMany(n -> Flux.interval(Duration.ofMillis(n * streamDuration)))
            .flatMap(i -> employeesFlux)
            .delayElements(Duration.ofMillis(streamDuration));
    }
}
