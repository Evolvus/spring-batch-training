package com.evolvus.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class TwoStepAsyncJobConfig {

	@Autowired
	private JobRepository jobRepository;
	
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
				.get("twoStepAsyncJob")
//				.incrementer(new RunIdIncrementer())
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
	
	@Bean(name = "AsyncJobLauncher")	
	public JobLauncher asyncJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

}
