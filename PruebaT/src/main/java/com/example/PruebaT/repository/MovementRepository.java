package com.example.PruebaT.repository;

import com.example.PruebaT.model.Cliente;
import com.example.PruebaT.model.Cuenta;
import com.example.PruebaT.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovementRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findTipoMovimientosByCuenta(Cuenta numeroCuenta);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :id ORDER BY m.fecha DESC LIMIT 1")
    List<Movimiento> findLastMovementByCuentaId(@Param("id") Long id);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.cliente.id = :id AND fecha BETWEEN :fechaInicial and :fechaFinal")
    List<Movimiento> findByFechaBetweenAndCuentaClienteId(@Param("fechaInicial") Date fechaInicial,@Param("fechaFinal") Date fechaFinal,@Param("id") Long cliente);

}
