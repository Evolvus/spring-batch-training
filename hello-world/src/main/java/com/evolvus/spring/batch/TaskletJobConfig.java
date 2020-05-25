package com.evolvus.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class TaskletJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private Tasklet helloWorldTasklet;

	@Bean
	public Job helloWorldJob() {
		return jobBuilderFactory
				.get("helloWorldJob")
				.incrementer(new RunIdIncrementer())
				.flow(singleStep())
				.end()
				.build();
	}

	@Bean
	public Step singleStep() {
		return stepBuilderFactory
				.get("taskletStep")
				.tasklet(helloWorldTasklet)
				.build();
	}
}
