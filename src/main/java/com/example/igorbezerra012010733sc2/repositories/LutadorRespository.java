package com.example.igorbezerra012010733sc2.repositories;

import com.example.igorbezerra012010733sc2.entities.Lutador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LutadorRespository extends JpaRepository<Lutador, Integer> {

    Integer countLutadorByVidaGreaterThan(Double vida);

}
