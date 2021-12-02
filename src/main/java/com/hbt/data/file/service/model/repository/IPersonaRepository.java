package com.hbt.data.file.service.model.repository;


import com.hbt.data.file.service.model.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface IPersonaRepository extends JpaRepository<Persona, Long> {
}
