package com.evolvus.spring.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
public class CustomSkipListener implements SkipListener<FileData, TableData> {

	private static final Logger LOG = LoggerFactory.getLogger(CustomSkipListener.class);

	public void onSkipInRead(Throwable t) {
		LOG.info("onSkipInRead: ", t );
	}

	public void onSkipInWrite(TableData item, Throwable t) {
		LOG.info("onSkipInWrite: name: {}", item.getName(), t );		
	}

	public void onSkipInProcess(FileData item, Throwable t) {
		LOG.info("onSkipInProcess: name: {}", item.getName(), t );				
	}

}
