package com.evolvus.spring.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class CustomChunkListener implements ChunkListener {

	private static final Logger LOG = LoggerFactory.getLogger(CustomChunkListener.class);
	
	public void beforeChunk(ChunkContext context) {
		LOG.info("beforeChunk: " );
	}

	public void afterChunk(ChunkContext context) {
		LOG.info("afterChunk: " );
	}

	public void afterChunkError(ChunkContext context) {
		LOG.info("afterChunkError: " );
	}

}
