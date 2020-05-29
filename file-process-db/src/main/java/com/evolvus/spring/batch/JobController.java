package com.evolvus.spring.batch;

import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JobController {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job fileDbJob;
	
	@PostMapping("/runJob")
	@ResponseBody
	public String runJob() {
		JobParametersBuilder jb = new JobParametersBuilder();
		jb.addString("uuid", UUID.randomUUID().toString());
		
		JobExecution jobExecution = null;
		
		try {
			jobExecution = jobLauncher.run(fileDbJob, jb.toJobParameters());
		} catch (Exception e) {
			return "failed";
		}
		
		return "ok";
	}
}
