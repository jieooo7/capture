package com.web.capture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CaptureApplication {

	public static void main(String[] args) {
//		new SpringApplication().setAddCommandLineProperties(false);设置不能从命令行设置参数
//		System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(CaptureApplication.class, args);
	}
}
