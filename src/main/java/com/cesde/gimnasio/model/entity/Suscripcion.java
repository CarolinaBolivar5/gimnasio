package com.cesde.gimnasio.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "suscripciones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Suscripcion extends BaseEntity{

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;

    private boolean activa;

    private String codigoAcceso;

    @ManyToOne
    @JoinColumn(name = "socio_id", nullable = false)
    @JsonIgnoreProperties("suscripciones")
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonIgnoreProperties("suscripciones")
    private Plan plan;

}
