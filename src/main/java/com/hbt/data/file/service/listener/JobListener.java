package com.hbt.data.file.service.listener;


import com.hbt.data.file.service.model.repository.IPersonaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class JobListener extends JobExecutionListenerSupport {

    @Autowired
    IPersonaRepository repository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        /*if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("Ha finalizado el job, verifica los resultados");

            repository.findAll().forEach(persona -> log.info("Registro: <" + persona + ">"));

        }*/
    }
}
