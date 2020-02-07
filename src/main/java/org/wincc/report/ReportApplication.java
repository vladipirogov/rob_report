package org.wincc.report;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ReportApplication {

	public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ReportApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
	}

}
