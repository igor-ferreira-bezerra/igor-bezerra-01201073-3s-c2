package com.example.igorbezerra012010733sc2.controllers;

import com.example.igorbezerra012010733sc2.entities.Luta;
import com.example.igorbezerra012010733sc2.entities.Lutador;
import com.example.igorbezerra012010733sc2.repositories.LutadorRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lutadores")
public class LutadorController {

    @Autowired
    LutadorRespository respository;

    @PostMapping
    public ResponseEntity postLutador(@Valid @RequestBody Lutador lutador) {
        Lutador novoLutador = respository.save(lutador);
        if(novoLutador.equals(lutador)) {
            return ResponseEntity.status(201).build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping
    public ResponseEntity getLutadores() {
        List<Lutador> lutadores = respository.findAllOrderByForcaGolpe();

        if (lutadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lutadores);
        }
    }

    @GetMapping("/contagem-vivos")
    public ResponseEntity getLutadoresVivos() {
        Integer lutadoresVivos = respository.countLutadorByVidaGreaterThan(0.0);

        return ResponseEntity.status(200).body(lutadoresVivos);
    }

    @PostMapping("/{id}/concentrar")
    public ResponseEntity getLutadorConcentrar(@PathVariable Integer id) {

        Optional<Lutador> lutador = respository.findById(id);
        if(lutador.isPresent()) {
            Integer concentracoes = lutador.get().getConcentracoesRealizadas();
            if(concentracoes < 3) {
                Double vida = lutador.get().getVida();
                lutador.get().setVida(vida * 115 / 100);
                lutador.get().setConcentracoesRealizadas(concentracoes + 1);
                lutador.get().setId(id);
                respository.save(lutador.get());
                return ResponseEntity.status(202).build();
            } else {
               return ResponseEntity.status(400).body("Lutador já se concentrou 3 vezes!");
            }
        } else {
          return   ResponseEntity.status(400).body("Id inválido");
        }
    }

    @PostMapping("/golpe")
    public ResponseEntity getGolpe(@RequestBody Luta luta) {

        Integer idLutadorBate = luta.getIdLutadorBate();
        Integer idLutadorApanha = luta.getIdLutadorApanha();

        if(idLutadorBate < 0 && idLutadorApanha < 0) {
            return ResponseEntity.status(400).body("O Id deve ser maior que 0");
        }

        Optional<Lutador> lutadorBate = respository.findById(idLutadorBate);
        Optional<Lutador> lutadorApanha = respository.findById(idLutadorApanha);

        if(lutadorBate.isPresent() && lutadorApanha.isPresent()) {

            if (!lutadorBate.get().getVivo() || !lutadorApanha.get().getVivo()) {
                return  ResponseEntity.status(400).body("Ambos os lutadores devem estar vivos!");
            }
            Double forcaGolpe = lutadorBate.get().getForcaGolpe();
            Double vida = lutadorApanha.get().getVida();

            if ((vida - forcaGolpe) < 0.0) {
                vida = 0.0;
            } else {
                vida = vida - forcaGolpe;
            }

            lutadorApanha.get().setVida(vida);
            respository.save(lutadorApanha.get());
            List<Lutador> lutadores = Arrays.asList(lutadorBate.get(), lutadorApanha.get());
            return   ResponseEntity.status(202).body(lutadores);
        } else {
            return   ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/mortos")
    public ResponseEntity getMortos() {
        List<Lutador> lutadores = respository.findAllByVidaEquals(0.0);

        if(lutadores.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lutadores);
        }
    }
}
