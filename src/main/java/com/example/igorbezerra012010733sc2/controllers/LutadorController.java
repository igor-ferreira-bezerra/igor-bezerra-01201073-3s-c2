package com.example.igorbezerra012010733sc2.controllers;

import com.example.igorbezerra012010733sc2.entities.Lutador;
import com.example.igorbezerra012010733sc2.repositories.LutadorRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
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
        List<Lutador> lutadores = respository.findAll();

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

    @PostMapping("/lutadores/{id}/concentrar")
    public ResponseEntity getLutadorConcentrar(@RequestParam Integer id) {

        Optional<Lutador> lutador = respository.findById(id);
        if(lutador.isPresent()) {
            Integer concentracoes = lutador.get().getConcentracoesRealizadas();
            if(concentracoes < 3) {
                Double vida = lutador.get().getVida();
                lutador.get().setVida(vida * 1.15);
                lutador.get().setConcentracoesRealizadas(concentracoes++);
                return ResponseEntity.status(202).build();
            } else {
               return ResponseEntity.status(400).body("Lutador já se concentrou 3 vezes!");
            }
        } else {
          return   ResponseEntity.status(400).body("Id inválido");
        }
    }
}
