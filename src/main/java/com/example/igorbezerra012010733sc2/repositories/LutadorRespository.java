package com.example.igorbezerra012010733sc2.repositories;

import com.example.igorbezerra012010733sc2.entities.Lutador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LutadorRespository extends JpaRepository<Lutador, Integer> {

    @Query(value = "SELECT * FROM LUTADOR ORDER BY FORCA_GOLPE", nativeQuery = true)
    List<Lutador> findAllOrderByForcaGolpe();

    Integer countLutadorByVidaGreaterThan(Double vida);

    List<Lutador> findAllByVidaEquals(Double vida);

}
