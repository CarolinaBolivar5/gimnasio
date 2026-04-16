package com.cesde.gimnasio.controller;

import com.cesde.gimnasio.model.entity.Plan;
import com.cesde.gimnasio.model.entity.Socio;
import com.cesde.gimnasio.model.entity.Suscripcion;
import com.cesde.gimnasio.repository.PlanRepository;
import com.cesde.gimnasio.repository.SocioRepository;
import com.cesde.gimnasio.repository.SuscripcionRepository;
import com.cesde.gimnasio.service.SuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
public class SuscripcionController {

    private final SuscripcionRepository suscripcionRepository;
    private final SocioRepository socioRepository;
    private final PlanRepository planRepository;
    private final SuscripcionService suscripcionService;

    @GetMapping
    public List<Suscripcion> listar() {
        return suscripcionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Suscripcion obtenerPorId(@PathVariable Long id) {
        return suscripcionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Suscripción no encontrada con id: " + id));
    }

    @GetMapping("/socio/{socioId}")
    public List<Suscripcion> listarPorSocio(@PathVariable Long socioId) {
        if (!socioRepository.existsById(socioId)) {
            throw new NoSuchElementException("Socio no encontrado con id: " + socioId);
        }
        return suscripcionRepository.findBySocioId(socioId);
    }

    @GetMapping("/plan/{planId}")
    public List<Suscripcion> listarPorPlan(@PathVariable Long planId) {
        if (!planRepository.existsById(planId)) {
            throw new NoSuchElementException("Plan no encontrado con id: " + planId);
        }
        return suscripcionRepository.findByPlanId(planId);
    }

    @PostMapping
    public ResponseEntity<Suscripcion> crear(@RequestBody Suscripcion suscripcion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(suscripcionService.crearSuscripcion(suscripcion));
    }

    @PutMapping("/{id}")
    public Suscripcion actualizar(@PathVariable Long id, @RequestBody Suscripcion suscripcion) {
        Suscripcion existente = suscripcionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Suscripción no encontrada con id: " + id));
        Socio socio = resolverSocio(suscripcion.getSocio());
        Plan plan = resolverPlan(suscripcion.getPlan());
        existente.setFechaInicio(suscripcion.getFechaInicio());
        existente.setFechaFin(suscripcion.getFechaFin());
        existente.setActiva(suscripcion.isActiva());
        existente.setSocio(socio);
        existente.setPlan(plan);
        return suscripcionRepository.save(existente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!suscripcionRepository.existsById(id)) {
            throw new NoSuchElementException("Suscripción no encontrada con id: " + id);
        }
        suscripcionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Socio resolverSocio(Socio referencia) {
        if (referencia == null || referencia.getId() == null) {
            throw new IllegalArgumentException("Debe indicar el socio (campo socio.id).");
        }
        return socioRepository.findById(referencia.getId())
                .orElseThrow(() -> new NoSuchElementException("Socio no encontrado con id: " + referencia.getId()));
    }

    private Plan resolverPlan(Plan referencia) {
        if (referencia == null || referencia.getId() == null) {
            throw new IllegalArgumentException("Debe indicar el plan (campo plan.id).");
        }
        return planRepository.findById(referencia.getId())
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado con id: " + referencia.getId()));
    }
}
