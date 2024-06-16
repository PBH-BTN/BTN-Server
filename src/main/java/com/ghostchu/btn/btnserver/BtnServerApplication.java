package com.ghostchu.btn.btnserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan
@Validated
public class BtnServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtnServerApplication.class, args);
	}

}
