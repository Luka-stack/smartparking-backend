package pl.ttpsc.smartparking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.ttpsc.smartparking.persistence.repository.AccessRepository;

@SpringBootApplication
public class SmartparkingApplication {

	public static void main(String[] args) {

		SpringApplication.run(SmartparkingApplication.class, args);

	}

}
