package com.example.PruebaT.service;

import com.example.PruebaT.dto.AccountDTO;
import com.example.PruebaT.exceptions.InsertDataException;
import com.example.PruebaT.exceptions.ResourceNotFoundException;
import com.example.PruebaT.model.Cuenta;
import com.example.PruebaT.repository.AccountRepository;
import com.example.PruebaT.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public AccountDTO getAccountById(Long id) {
        Optional<Cuenta> cuentaOptional = accountRepository.findById(id);
        if (cuentaOptional.isPresent()) {
            return convertToDTO(cuentaOptional.get());
        } else {
            throw new ResourceNotFoundException("Cuenta con ID " + id + " no encontrada");
        }
    }

    private AccountDTO convertToDTO(Cuenta cuenta) {
        AccountDTO cuentaDTO = new AccountDTO();
        cuentaDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
        cuentaDTO.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDTO.setSaldoInicial(cuenta.getSaldoInicial());
        cuentaDTO.setEstado(cuenta.getEstado());
        cuentaDTO.setNombreCliente(cuenta.getCliente().getNombre());
        return cuentaDTO;
    }

    public AccountDTO getByNumberAccount(String number){
        try {
            Optional<Cuenta> cuentaOptional = accountRepository.findByNumeroCuenta(number);
            if (cuentaOptional.isPresent()) {
                return convertToDTO(cuentaOptional.get());
            } else {
                throw new ResourceNotFoundException("Cuenta con Numero " + number + " no encontrada");
            }
        }catch (Exception e){
            throw new RuntimeException("Error al obtener la cuenta", e);
        }

    }

    public Cuenta createAccount(Cuenta cuenta) {
        try {

            if (cuenta.getNumeroCuenta() == null || cuenta.getTipoCuenta() == null || cuenta.getSaldoInicial() == null) {
                throw new InsertDataException("Error: Campos obligatorios faltantes");
            }

            if (cuenta.getSaldoInicial() < 0) {
                throw new InsertDataException("Error: Saldo inicial no válido");
            }

            if(cuenta.getCliente() == null){
                throw new ResourceNotFoundException("SE NECESITA UN CLIENTE PARA ASOCIAR A LA CUENTA");
            }
            customerRepository.findById(cuenta.getCliente().getClienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("NO EXISTE CLIENTE PARA ASOCIAR A LA CUENTA"));

            if (accountRepository.findByNumeroCuenta(cuenta.getNumeroCuenta()).isPresent()) {
                throw new InsertDataException("Error: El número de cuenta ya existe");
            }

            Cuenta cuentaRegistrada = accountRepository.save(cuenta);

            if (cuentaRegistrada.getNumeroCuenta() != null) {
                return cuentaRegistrada;
            } else {
                throw new InsertDataException("Error: No se pudo crear la cuenta");
            }
        } catch (InsertDataException e) {
            throw new InsertDataException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteAccount(Long id){
        try{
            accountRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Cuenta con ID " + id + " no encontrado para eliminar");
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar cuenta", e);
        }

    }

}
