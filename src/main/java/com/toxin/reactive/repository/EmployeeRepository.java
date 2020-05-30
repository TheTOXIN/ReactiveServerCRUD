package com.toxin.reactive.repository;

import com.toxin.reactive.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

    @Tailable
    Flux<Employee> findAllByWork(boolean work);
}
