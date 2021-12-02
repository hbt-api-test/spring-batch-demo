package com.hbt.data.file.service.processor;


import com.hbt.data.file.service.model.entity.Persona;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PersonaItemProcessor implements ItemProcessor<Persona, Persona> {
    @Override
    public Persona process(Persona persona) throws Exception {
        log.info("Iniciamos procesamiento de los datos de la persona");

        String primerNombre = persona.getPrimerNombre().toUpperCase();
        String segundoNombre = persona.getSegundoNombre().toUpperCase();
        String primerApellido = persona.getPrimerApellido().toUpperCase();
        String segundoApellido = persona.getSegundoApellido().toUpperCase();

        Persona nuevaPersona = Persona.builder()
                .primerNombre(primerNombre)
                .segundoNombre(segundoNombre)
                .primerApellido(primerApellido)
                .segundoApellido(segundoApellido)
                .identificacion(persona.getIdentificacion())
                .telefono(persona.getTelefono())
                .direccion(persona.getDireccion())
                .build();

        log.info("Convirtiendo (" + persona + ") a (" + nuevaPersona + "); ");
        return nuevaPersona;
    }
}
