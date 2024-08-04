package com.example.PruebaT.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Data
@Entity
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;

    private String tipoCuenta;
    private Double saldoInicial;
    private String estado;

    //@Column(name = "cliente_id", nullable = false)
    //private Long clienteId;

    @ManyToOne
    @JoinColumn(name = "clienteId", referencedColumnName = "id")
    private Cliente cliente;
/*
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos;*/
}
