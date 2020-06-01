package com.toxin.reactive.repository;

import com.toxin.reactive.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

    Flux<Employee> findAllByOrderByHired();

    Flux<Employee> findAllByWorkTrue();
}
