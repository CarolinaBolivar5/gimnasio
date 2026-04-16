package com.cesde.gimnasio.controller;

import com.cesde.gimnasio.model.entity.Plan;
import com.cesde.gimnasio.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public List<Plan> listar() {
        return planService.listar();
    }

    @GetMapping("/{id}")
    public Plan obtenerPorId(@PathVariable Long id) {
        return planService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<Plan> crear(@RequestBody Plan plan) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.crear(plan));
    }

    @PutMapping("/{id}")
    public Plan actualizar(@PathVariable Long id, @RequestBody Plan plan) {
        return planService.actualizar(id, plan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        planService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
