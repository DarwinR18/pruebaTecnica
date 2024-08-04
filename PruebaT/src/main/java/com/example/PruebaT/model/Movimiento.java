package com.example.PruebaT.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fecha;
    private String tipoMovimiento;
    private Double valor;
    private double saldo;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", referencedColumnName = "id")
    private Cuenta cuenta;
}