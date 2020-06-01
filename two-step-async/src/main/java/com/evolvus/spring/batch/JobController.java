package com.evolvus.spring.batch;

import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired
	@Qualifier("AsyncJobLauncher")
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job twoStepJob;
	
	@PostMapping("/job")
	public String runJob() {
		JobParametersBuilder jb = new JobParametersBuilder();
		jb.addString("uuid", UUID.randomUUID().toString());
		
		JobExecution jobExecution = null;
		
		try {
			jobExecution = jobLauncher.run(twoStepJob, jb.toJobParameters());
		} catch (Exception e) {
			return "-1";
		}
		
		return ""+jobExecution.getJobInstance().getInstanceId();
	}
	
	@GetMapping("/job/{id}")
	public String getJob(@PathVariable("id") Long id) {
	    JobExecution jobExecution = jobExplorer.getJobExecution(id);
	    String jobName = jobExecution.getJobInstance().getJobName();
	    Map<String,JobParameter> jp = jobExecution.getJobParameters().getParameters();
	    return jobName + ": " +jobExecution.getStatus() + ": " + jp;
	}
}
