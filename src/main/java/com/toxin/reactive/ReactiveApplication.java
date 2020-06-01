package com.toxin.reactive;

import com.toxin.reactive.entity.Employee;
import com.toxin.reactive.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

import static com.toxin.reactive.util.EmployeeUtils.getAvatar;

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
        return () -> operations.dropCollection(Employee.class)
            .thenMany(operations.createCollection(Employee.class))
            .thenMany(Flux.range(1, 10).map(this::make))
            .flatMap(employeeRepository::save)
            .thenMany(employeeRepository.findAll())
            .subscribe(System.out::println);
   }

    private Employee make(int n) {
        Employee employee = new Employee();

        employee.setId(null);
        employee.setName("TEST_" + n);
        employee.setPhoto(getAvatar(employee));
        employee.setPosition("POS_" + n);
        employee.setSalary(n * 1000);
        employee.setWork(true);
        employee.setHired(LocalDate.now().plusDays(n));

        return employee;
    }

    @Bean
    public CorsWebFilter cors() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
