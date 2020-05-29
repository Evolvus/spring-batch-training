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
public class TwoStepJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private Tasklet stepOneTasklet;

	@Autowired
	private Tasklet stepTwoTasklet;
	
	@Bean
	public Job twoStepJob() {
		return jobBuilderFactory
				.get("twoStepJob")
				.incrementer(new RunIdIncrementer())
				.flow(oneTaskletStep())
				.next(twoTaskletStep())
				.end()
				.build();
	}

	@Bean
	public Step oneTaskletStep() {
		return stepBuilderFactory
				.get("stepOneTasklet")
				.tasklet(stepOneTasklet)
				.build();
	}
	
	@Bean
	public Step twoTaskletStep() {
		return stepBuilderFactory
				.get("stepTwoTasklet")
				.tasklet(stepTwoTasklet)
				.build();
	}

}
