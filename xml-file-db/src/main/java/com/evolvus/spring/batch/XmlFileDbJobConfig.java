package com.evolvus.spring.batch;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
@EnableBatchProcessing
public class XmlFileDbJobConfig {

	private static final String INSERT_DATA = "INSERT INTO SAMPLE_DATA(id, code, desc) VALUES (?, ?, ?)";

	@Value("${chunkSize}")
	private String chunkSize;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public Job fxmlFileDbJob(Step fileDbStep) {
		return jobBuilderFactory
				.get("xmlFileDbJob")
				.incrementer(new RunIdIncrementer())
				.flow(fileDbStep)
				.end()
				.build();
	}

	@Bean
	public Step fileDbStep(StaxEventItemReader<FileData> reader, JdbcBatchItemWriter<FileData> writer) {
		return stepBuilderFactory
				.get("fileDbStep")
				.<FileData, FileData>chunk(Integer.parseInt(chunkSize))
				.reader(reader)
				.writer(writer)
				.faultTolerant()
				.skipLimit(5)
				.skip(Exception.class)
				.build();
	}

	@Bean
	public StaxEventItemReader<FileData> fileReader(XStreamMarshaller fileDataMarshaller) {
		StaxEventItemReader<FileData> reader = new StaxEventItemReader<FileData>();
		reader.setResource(new FileSystemResource("input/ACH-DR-INDB-INDBH2H-27052020-LEGACY000014-INP-ACK.xml"));
		reader.setFragmentRootElementName("TxInfAndSts");
		reader.setUnmarshaller(fileDataMarshaller);
		return reader;
	}
	
	

	@Bean
	public JdbcBatchItemWriter<FileData> writer() {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		JdbcBatchItemWriter<FileData> writer = new JdbcBatchItemWriter<FileData>();
		writer.setDataSource(dataSource);
		writer.setJdbcTemplate(jdbcTemplate);
		writer.setSql(INSERT_DATA);
		writer.setItemPreparedStatementSetter(new FileDataStatementSetter());
		return writer;
	}
	
	@Bean
	public XStreamMarshaller fileDataMarshaller() {
		Map<String, Class> aliases = new HashMap<>();
		aliases.put("TxInfAndSts", FileData.class);
		aliases.put("StsRsnInf", StatusData.class);
		aliases.put("Rsn", ReasonData.class);
		
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(aliases);

		return marshaller;
	}

}
