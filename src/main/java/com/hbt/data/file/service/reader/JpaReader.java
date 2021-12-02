package com.hbt.data.file.service.reader;

import com.hbt.data.file.service.model.entity.Persona;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Slf4j
public class JpaReader extends JdbcCursorItemReader<Persona> implements ItemReader<Persona> {

    public JpaReader(@Autowired DataSource dataSource){
        setDataSource(dataSource);
        setSql("SELECT id, primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,identificacion,telefono,direccion FROM personas");
        setFetchSize(100);
        setRowMapper(new PersonaRowMapper());
    }

}
