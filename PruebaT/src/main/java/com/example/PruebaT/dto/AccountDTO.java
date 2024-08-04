package com.example.PruebaT.dto;
import lombok.Data;

@Data
public class AccountDTO {
    private String numeroCuenta;
    private String tipoCuenta;
    private Double saldoInicial;
    private String estado;
    private String nombreCliente;
}
