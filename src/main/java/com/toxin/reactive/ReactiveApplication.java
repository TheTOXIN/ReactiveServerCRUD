package com.toxin.reactive;

import com.toxin.reactive.entity.Employee;
import com.toxin.reactive.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
@EnableReactiveMongoRepositories
public class ReactiveApplication {

    private final EmployeeRepository employeeRepository;
    private final ReactiveMongoOperations operations;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }

    @Bean
    InitializingBean init() {
        var options = CollectionOptions
            .empty()
            .capped()
            .size(1024 * 1024)
            .maxDocuments(100);

        return () -> operations.dropCollection(Employee.class)
            .thenMany(operations.createCollection(Employee.class, options))
            .thenMany(Flux.range(1, 10).map(this::make))
            .flatMap(employeeRepository::save)
            .thenMany(employeeRepository.findAll())
            .subscribe(System.out::println);
   }

    private Employee make(int n) {
        Employee employee = new Employee();

        employee.setId(null);
        employee.setName("TEST_" + n);
        employee.setPhoto("http://www.gravatar.com/avatar/" + UUID.randomUUID() + "?d=identicon");
        employee.setPosition("POS_" + n);
        employee.setSalary(n * 1000);
        employee.setWork(true);
        employee.setHired(LocalDateTime.now());

        return employee;
    }
}
