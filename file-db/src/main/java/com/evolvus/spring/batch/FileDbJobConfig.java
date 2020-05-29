package com.evolvus.spring.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EnableBatchProcessing
public class FileDbJobConfig {

	private static final String INSERT_DATA = "INSERT " + "INTO SAMPLE_DATA(name, info) " + "VALUES (?, ?)";

	@Value("${chunkSize}")
	private String chunkSize;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job fileDbJob(Step fileDbStep) {
		return jobBuilderFactory
				.get("fileDbJob")
				.incrementer(new RunIdIncrementer())
				.flow(fileDbStep)
				.end()
				.build();
	}

	@Bean
	public Step fileDbStep(DataSource dataSource, FlatFileItemReader<FileData> reader, JdbcBatchItemWriter<FileData> writer) {
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
	public FlatFileItemReader<FileData> fileReader() {
		FlatFileItemReader<FileData> reader = new FlatFileItemReader<FileData>();
		reader.setResource(new FileSystemResource("input/report_file.CSV"));

		reader.setLineMapper(new DefaultLineMapper<FileData>() {
			{

				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "name", "info" });
						setDelimiter(",");
					}
				});

				setFieldSetMapper(new BeanWrapperFieldSetMapper<FileData>() {
					{
						setTargetType(FileData.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public JdbcBatchItemWriter<FileData> writer(DataSource dataSource) {
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		JdbcBatchItemWriter<FileData> writer = new JdbcBatchItemWriter<FileData>();
		writer.setDataSource(dataSource);
		writer.setJdbcTemplate(jdbcTemplate);
		writer.setSql(INSERT_DATA);
		writer.setItemPreparedStatementSetter(new FileDataStatementSetter());
		return writer;
	}

}
