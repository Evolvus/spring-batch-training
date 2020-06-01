package com.evolvus.spring.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class StepOneTasklet implements Tasklet {
	
	private static final Logger LOG = LoggerFactory.getLogger(StepOneTasklet.class);

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOG.info("execute: start sleep!");
		Thread.sleep(10000);
		LOG.info("execute: end sleep!");
		return RepeatStatus.FINISHED;
	}

}
