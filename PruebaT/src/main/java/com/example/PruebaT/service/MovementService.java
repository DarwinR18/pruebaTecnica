package com.example.PruebaT.service;

import com.example.PruebaT.dto.AccountDTO;
import com.example.PruebaT.dto.MovementDTO;
import com.example.PruebaT.exceptions.InsertDataException;
import com.example.PruebaT.exceptions.ResourceNotFoundException;
import com.example.PruebaT.model.Cliente;
import com.example.PruebaT.model.Cuenta;
import com.example.PruebaT.model.Movimiento;
import com.example.PruebaT.repository.AccountRepository;
import com.example.PruebaT.repository.CustomerRepository;
import com.example.PruebaT.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.sum;

@Service
public class MovementService {

    @Autowired
    private MovementRepository movementRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;

    private MovementDTO convertToDTO(Movimiento movimiento) {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setFecha(movimiento.getFecha());
        movementDTO.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        movementDTO.setTipoMovimiento(movimiento.getTipoMovimiento());
        movementDTO.setSaldo(movimiento.getSaldo());
        movementDTO.setSaldoInicial(movimiento.getCuenta().getSaldoInicial());
        movementDTO.setMovimiento(movimiento.getValor());
        movementDTO.setCliente(movimiento.getCuenta().getCliente().getNombre());
        movementDTO.setEstado(movimiento.getCuenta().getEstado());
        movementDTO.setTipoCuenta(movimiento.getCuenta().getTipoCuenta());
        return movementDTO;
    }

    public Double getLastMovementBalance(Long id) {
        double lastMovementValue = 0.0;
        int indexToSearch = 0;
        List<Movimiento> lastMovement = movementRepository.findLastMovementByCuentaId(id);
        if (lastMovement.isEmpty()) {
            return 0.0;
        }
        indexToSearch = lastMovement.size()-1;
        lastMovementValue = lastMovement.getLast().getSaldo();
        return lastMovementValue;
    }

    public MovementDTO createMovement(Movimiento movimiento){
        try{
            if (movimiento.getFecha() == null || movimiento.getTipoMovimiento() == null || movimiento.getValor() == null) {
                throw new InsertDataException("Error: Campos obligatorios faltantes");
            }

            Cuenta cuenta = accountRepository.findByNumeroCuenta(movimiento.getCuenta().getNumeroCuenta())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada NO SE PUEDE REALIZAR EL MOVIMIENTO"));

            if (movimiento.getValor() == 0) {
                throw new InsertDataException("Error: El valor del movimiento no puede ser cero");
            }

            if (!(movimiento.getTipoMovimiento().equalsIgnoreCase("deposito")
                    || movimiento.getTipoMovimiento().equalsIgnoreCase("retiro"))){
                throw new InsertDataException("EL TIPO DE MOVIMIENTO NO CORRESPONDE A UNO PERMITIDO -DEPOSITO- O -RETIRO-");
            }

            List<Movimiento> movimientosValores = movementRepository.findTipoMovimientosByCuenta(cuenta);

            Double totalDepositos = movimientosValores.stream()
                    .filter(m -> m.getTipoMovimiento().equalsIgnoreCase("deposito"))
                    .mapToDouble(Movimiento::getValor)
                    .sum();

            Double totalRetiros = movimientosValores.stream()
                    .filter(m -> m.getTipoMovimiento().equalsIgnoreCase("retiro"))
                    .mapToDouble(m -> Math.abs(m.getValor()))
                    .sum();

            movimiento.setSaldo(getLastMovementBalance(cuenta.getId()));

            if (movimiento.getTipoMovimiento().equalsIgnoreCase("deposito") && movimiento.getValor() < 0) {
                throw new InsertDataException("Error: NO SE PUEDE DEPOSITAR VALORES NEGATIVOS");
            }
            if (movimiento.getTipoMovimiento().equalsIgnoreCase("deposito") && totalDepositos > 0) {
                movimiento.setSaldo(movimiento.getSaldo() + movimiento.getValor());
            } else if (movimiento.getTipoMovimiento().equalsIgnoreCase("deposito") && totalDepositos == 0) {
                movimiento.setSaldo(cuenta.getSaldoInicial() + movimiento.getValor());
            }


            if (movimiento.getTipoMovimiento().equalsIgnoreCase("retiro") && movimiento.getValor() < 0 && totalRetiros > 0) {
                if (movimiento.getSaldo() + movimiento.getValor() < 0) {
                    throw new InsertDataException("Error: Saldo no disponible, no se puede hacer el retiro");
                }
                movimiento.setSaldo(movimiento.getSaldo() + movimiento.getValor());
            }else if(movimiento.getTipoMovimiento().equalsIgnoreCase("retiro") && totalRetiros == 0) {
                movimiento.setSaldo(cuenta.getSaldoInicial() + movimiento.getValor());
            }

            if (movimiento.getTipoMovimiento().equalsIgnoreCase("retiro") && movimiento.getValor() > 0 && totalRetiros > 0) {
                if (movimiento.getSaldo() - movimiento.getValor() < 0) {
                    throw new InsertDataException("Error: Saldo no disponible, no se puede hacer el retiro");
                }
                movimiento.setSaldo(movimiento.getSaldo() - movimiento.getValor());
            }else if(movimiento.getTipoMovimiento().equalsIgnoreCase("retiro") && totalRetiros == 0) {
                movimiento.setSaldo(cuenta.getSaldoInicial() - movimiento.getValor());
            }

            accountRepository.save(cuenta);

            movimiento.setCuenta(cuenta);
            Movimiento movimientoRegistrado = movementRepository.save(movimiento);
            if (movimientoRegistrado.getId() != null) {
                return convertToDTO(movimientoRegistrado);
            } else {
                throw new InsertDataException("Error: No se pudo registrar el movimiento");
            }
        } catch (ResourceNotFoundException e) {
            throw new InsertDataException(e.getMessage());
        } catch (InsertDataException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar movimiento", e);
        }
    }

    public List<Movimiento> findByFechaBetweenAndCliente(Date fechaInicial, Date fechaFinal, Long clienteId) {
        Cliente cliente = customerRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + clienteId + " no encontrado"));
        return movementRepository.findByFechaBetweenAndCuentaClienteId(fechaInicial, fechaFinal, clienteId);
    }

}
