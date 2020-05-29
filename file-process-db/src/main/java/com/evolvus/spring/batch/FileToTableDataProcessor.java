package com.evolvus.spring.batch;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FileToTableDataProcessor implements ItemProcessor<FileData, TableData> {

	private static int filter = 0;
	private static final Logger LOG = LoggerFactory.getLogger(FileToTableDataProcessor.class);

	public TableData process(FileData item) throws Exception {
		TableData tableData = new TableData();
		tableData.setName(item.getName());
		tableData.setInfo(item.getInfo());
		tableData.setStatus("NEW");
		tableData.setLoadDate(new Date());
		filter++;

		if (filter % 5 == 0) {
			LOG.info("Filtering line: {}", filter);
			return null;
		}
		return tableData;
	}

}
