package com.cesde.gimnasio.service;

import com.cesde.gimnasio.model.entity.Plan;
import com.cesde.gimnasio.model.enums.TipoPlan;
import com.cesde.gimnasio.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlanService {

    private static final double DESCUENTO_ANUAL = 0.20;
    private final PlanRepository planRepository;

    public List<Plan> listar() {
        return planRepository.findAll();
    }

    public Plan obtenerPorId(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado con id: " + id));
    }

    @Transactional
    public Plan crear(Plan plan) {
        plan.setId(null);
        aplicarDescuentoSiEsAnual(plan);
        return planRepository.save(plan);
    }

    @Transactional
    public Plan actualizar(Long id, Plan plan) {
        if (!planRepository.existsById(id)) {
            throw new NoSuchElementException("Plan no encontrado con id: " + id);
        }
        plan.setId(id);
        aplicarDescuentoSiEsAnual(plan);
        return planRepository.save(plan);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!planRepository.existsById(id)) {
            throw new NoSuchElementException("Plan no encontrado con id: " + id);
        }
        planRepository.deleteById(id);
    }

    private void aplicarDescuentoSiEsAnual(Plan plan) {
        if (plan.getNombre() == TipoPlan.ANUAL && plan.getPrecio() != null) {
            plan.setPrecio(plan.getPrecio() * (1 - DESCUENTO_ANUAL));
        }
    }
}
