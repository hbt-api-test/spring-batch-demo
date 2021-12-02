package com.hbt.data.file.service.reader;

import com.hbt.data.file.service.model.entity.Persona;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class PersonaRowMapper implements RowMapper<Persona> {
    @Override
    public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
        Persona persona = Persona.builder()
                .id(rs.getLong("id"))
                .primerNombre(rs.getString("primer_nombre"))
                .segundoNombre(rs.getString("segundo_nombre"))
                .primerApellido(rs.getString("primer_apellido"))
                .segundoApellido(rs.getString("segundo_apellido"))
                .identificacion(rs.getString("identificacion"))
                .direccion(rs.getString("direccion"))
                .build();
            log.info("PersonaRowMapper -> " + persona);
        return persona;
    }
}
