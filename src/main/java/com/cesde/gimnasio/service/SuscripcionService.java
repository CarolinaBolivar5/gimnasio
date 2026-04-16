package com.cesde.gimnasio.service;

import com.cesde.gimnasio.model.entity.Plan;
import com.cesde.gimnasio.model.entity.Socio;
import com.cesde.gimnasio.model.entity.Suscripcion;
import com.cesde.gimnasio.repository.PlanRepository;
import com.cesde.gimnasio.repository.SocioRepository;
import com.cesde.gimnasio.repository.SuscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final SocioRepository socioRepository;
    private final PlanRepository planRepository;

    @Transactional
    public Suscripcion crearSuscripcion(Suscripcion suscripcion) {
        Socio socio = resolverSocio(suscripcion.getSocio());

        // REGLA #4 (VALIDACIÓN)
        if (socio.getEstadoSalud() == null || socio.getEstadoSalud().trim().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear la suscripción: el socio no tiene estado de salud registrado");
        }

        Plan plan = resolverPlan(suscripcion.getPlan());

        List<Suscripcion> suscripcionesActuales = suscripcionRepository.findBySocioId(socio.getId());
        for (Suscripcion existente : suscripcionesActuales) {
            if (existente.isActiva()) {
                existente.setActiva(false);
            }
        }
        suscripcionRepository.saveAll(suscripcionesActuales);

        suscripcion.setId(null);
        suscripcion.setSocio(socio);
        suscripcion.setPlan(plan);

        return suscripcionRepository.save(suscripcion);
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
