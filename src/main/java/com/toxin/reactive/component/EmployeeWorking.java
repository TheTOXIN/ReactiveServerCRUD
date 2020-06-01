package com.toxin.reactive.component;

import com.toxin.reactive.constant.EmployeeActivity;
import com.toxin.reactive.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class EmployeeWorking {

    private final EmployeeRepository employeeRepository;

    @Value("${employees.working.interval:10000}")
    private Long workingInterval;

    public void start() {
        Flux.interval(Duration.ofMillis(workingInterval))
            .flatMap(n -> employeeRepository.findAllByWorkTrue())
            .filter(e -> Math.random() < 0.5)
            .doOnNext(e -> e.setActivity(EmployeeActivity.random()))
            .flatMap(employeeRepository::save)
            .subscribe();
    }
}
