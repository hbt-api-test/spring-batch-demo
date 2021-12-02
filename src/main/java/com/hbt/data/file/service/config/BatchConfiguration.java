package com.hbt.data.file.service.config;

import com.hbt.data.file.service.listener.JobListener;
import com.hbt.data.file.service.model.entity.Persona;
import com.hbt.data.file.service.processor.PersonaItemProcessor;
import com.hbt.data.file.service.reader.JpaReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobRepository repository;

    @Autowired
    JpaReader jpaReader;

    @Value("${batch.job.export.file.header}")
    private String headerData;

    @Value("${batch.job.export.file.path}")
    private String exportFile;


    @Bean
    public JobLauncher launcher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(repository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }


    @Bean
    public FlatFileItemReader<Persona> reader(){
        return new FlatFileItemReaderBuilder<Persona>()
                .name("personaItemReader")
                .resource(new ClassPathResource("dataPersonFile.csv"))
                .delimited()
                .names(new String[] {"primerNombre", "segundoNombre", "primerApellido", "segundoApellido", "identificacion", "telefono", "direccion"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Persona>(){{
                    setTargetType(Persona.class);
                }})
                .build();

    }

    @Bean
    public PersonaItemProcessor processor(){
        return new PersonaItemProcessor();
    }


    @Bean(name = "import")
    public Job importPersonaJob(JobListener listener, Step firstStep){
        return jobBuilderFactory.get("importPersonaJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(firstStep)
                .end()
                .build();
    }

    @Bean
    public Step firstStep(@Qualifier(value = "dbWriter")
                                      ItemWriter<Persona> writer){
        return stepBuilderFactory.get("firstStep")
                .<Persona, Persona> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }


    @Bean(name = "export")
    public Job exportPersonaJob(Step exportStep){
        return jobBuilderFactory.get("exportPersonaJob")
                .incrementer(new RunIdIncrementer())
                .flow(exportStep)
                .end().build();
    }

    @Bean
    public ItemWriter<Persona> csvWriter(){
        String exportFilePath = exportFile;
        Resource exportFileResource = new FileSystemResource(exportFilePath);

        String exportFileHeader = headerData;
        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);

        LineAggregator<Persona> lineAggregator = createPersonaAggregator();

        return new FlatFileItemWriterBuilder<Persona>()
                .name("personaWriter")
                .headerCallback(headerWriter)
                .lineAggregator(lineAggregator)
                .resource(exportFileResource)
                .build();
    }

    private LineAggregator<Persona> createPersonaAggregator() {
        DelimitedLineAggregator<Persona> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        FieldExtractor<Persona> fieldExtractor = createPersonaFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);
        return lineAggregator;
    }

    private FieldExtractor<Persona> createPersonaFieldExtractor() {
        BeanWrapperFieldExtractor<Persona> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"id", "primerNombre", "segundoNombre", "primerApellido", "segundoApellido", "identificacion", "telefono", "direccion"});
        return extractor;
    }

    @Bean
    public Step exportStep(ItemWriter<Persona> csvWriter) {
        return stepBuilderFactory.get("exportStep")
                .<Persona, Persona> chunk(10)
                .reader(jpaReader)
                .writer(csvWriter)
                .build();
    }

}
