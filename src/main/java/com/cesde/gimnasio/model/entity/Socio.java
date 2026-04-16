package com.cesde.gimnasio.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "socios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Socio extends BaseEntity{

    private String nombre;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaIngreso;

    private String estadoSalud;

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Suscripcion> suscripciones = new ArrayList<>();

    @JsonCreator
    public Socio(@JsonProperty("id") Long id) {
        setId(id);
    }
}
