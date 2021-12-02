package com.hbt.data.file.service.processor;

import com.hbt.data.file.service.model.entity.Persona;
import com.hbt.data.file.service.model.repository.IPersonaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Component("dbWriter")
@Slf4j
public class DBWriter implements ItemWriter<Persona> {

    @Autowired
    IPersonaRepository repository;


    @Override
    public void write(List<? extends Persona> personas) throws Exception {
        log.info("Registrando datos de personas: " + personas);
        repository.saveAll(personas);
    }
}
