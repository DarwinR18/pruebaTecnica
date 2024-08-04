package com.example.PruebaT.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String contrasenia;
    private String estado;
}
