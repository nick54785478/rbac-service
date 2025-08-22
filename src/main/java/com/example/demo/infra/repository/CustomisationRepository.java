package com.example.demo.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.customisation.aggregate.Customisation;

@Repository
public interface CustomisationRepository extends JpaRepository<Customisation, Long> {

}
