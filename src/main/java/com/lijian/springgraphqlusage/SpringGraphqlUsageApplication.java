package com.lijian.springgraphqlusage;

import com.lijian.springgraphqlusage.service.CarService;
import com.lijian.springgraphqlusage.service.HumanService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringGraphqlUsageApplication implements ApplicationRunner {

	private final HumanService humanService;
	private final CarService carService;

	public SpringGraphqlUsageApplication(HumanService humanService, CarService carService) {
		this.humanService = humanService;
		this.carService = carService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringGraphqlUsageApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		humanService.init();
		carService.init();
	}
}
