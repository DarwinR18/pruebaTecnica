package com.example.PruebaT.controller;

import com.example.PruebaT.dto.MovementDTO;
import com.example.PruebaT.exceptions.InsertDataException;
import com.example.PruebaT.exceptions.ResourceNotFoundException;
import com.example.PruebaT.model.Movimiento;
import com.example.PruebaT.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movimientos")
public class MovementController {

    @Autowired
    private MovementService movementService;

    @PostMapping("/insertar")
    public ResponseEntity<?> createMovement(@RequestBody Movimiento movimiento) {
        try {
            MovementDTO movimientoNuevo = movementService.createMovement(movimiento);
            return new ResponseEntity<>(movimientoNuevo, HttpStatus.OK);
        } catch (InsertDataException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reporte")
    public ResponseEntity<?> findMovementByBetweenDateAndCustomer(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin,
            @RequestParam("clienteId") Long clienteId) {
        try {
            List<Movimiento> movimientos = movementService.findByFechaBetweenAndCliente(fechaInicio, fechaFin, clienteId);
            List<MovementDTO> resultado = movimientos.stream().map(mov -> new MovementDTO(
                    mov.getFecha(),
                    mov.getCuenta().getCliente().getNombre(),
                    mov.getCuenta().getNumeroCuenta(),
                    mov.getCuenta().getTipoCuenta(),
                    mov.getCuenta().getSaldoInicial(),
                    mov.getValor(),
                    mov.getCuenta().getEstado(),
                    mov.getTipoMovimiento(),
                    mov.getSaldo()
            )).collect(Collectors.toList());
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
