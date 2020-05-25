package com.evolvus.spring.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldTasklet implements Tasklet {
	
	private static final Logger LOG = LoggerFactory.getLogger(HelloWorldTasklet.class);

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOG.info("execute: Hello World!");
		return RepeatStatus.FINISHED;
	}

}
