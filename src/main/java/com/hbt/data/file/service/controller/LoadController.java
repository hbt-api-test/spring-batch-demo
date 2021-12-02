package com.hbt.data.file.service.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/load")
public class LoadController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("import")
    Job job;

    @Autowired
    @Qualifier("export")
    Job expJob;

    @PostMapping("/demo")
    public Long load() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobExecution jobExecution = jobLauncher
                .run(job, new JobParametersBuilder()
                        .addLong("idInicio", System.nanoTime())
                        .toJobParameters());
        return  jobExecution.getId();
    }

    @PostMapping("/export")
    public Long export() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobExecution jobExecution = jobLauncher
                .run(expJob, new JobParametersBuilder()
                        .addLong("idInicio", System.nanoTime())
                        .toJobParameters());
        return jobExecution.getId();

    }
}
