package com.example.PruebaT.dto;
import lombok.Data;

import java.util.Date;

@Data
public class MovementDTO {

    private Date fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipoCuenta;
    private Double saldoInicial;
    private double movimiento;
    private String estado;
    private String tipoMovimiento;
    private Double saldo;

    public MovementDTO(Date fecha, String cliente, String numeroCuenta, String tipoCuenta, Double saldoInicial, double movimiento, String estado, String tipoMovimiento, Double saldo) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.movimiento = movimiento;
        this.estado = estado;
        this.tipoMovimiento = tipoMovimiento;
        this.saldo = saldo;
    }

    public MovementDTO() {
    }
}
