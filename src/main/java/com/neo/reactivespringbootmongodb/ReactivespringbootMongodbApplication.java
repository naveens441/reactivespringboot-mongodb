package com.neo.reactivespringbootmongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@SpringBootApplication

public class ReactivespringbootMongodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactivespringbootMongodbApplication.class, args);
	}

}
@Component
@RequiredArgsConstructor
@Log4j2
class SampleDataInitializer{
	private final EmployeeRepository employeeRepository;
	@EventListener(ApplicationReadyEvent.class)
	public void ready(){
		Flux<Employee> employees = Flux.just("Naveen", "Rahul", "Ajay")
				.map(emp->new Employee(null,emp))
				.flatMap(this.employeeRepository::save);
		this.employeeRepository
				.deleteAll()
				.thenMany(employees)
				.thenMany(this.employeeRepository.findAll())
				.subscribe(log::info);

	}


}
interface  EmployeeRepository extends
		ReactiveCrudRepository<Employee,String>{
	Flux<Employee> findByName(String name);
}
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Employee{
	@Id
	private String id;
	private String name;

}
