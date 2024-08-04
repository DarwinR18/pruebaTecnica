package com.example.PruebaT.service;

import com.example.PruebaT.dto.AccountDTO;
import com.example.PruebaT.dto.CustomerDTO;
import com.example.PruebaT.exceptions.InsertDataException;
import com.example.PruebaT.exceptions.ResourceNotFoundException;
import com.example.PruebaT.model.Cliente;
import com.example.PruebaT.model.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.PruebaT.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository clienteRepository;

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    private CustomerDTO convertToDTO(Cliente cliente) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(cliente.getClienteId());
        customerDTO.setContrasenia(cliente.getContrasenia());
        customerDTO.setEstado(cliente.getEstado());
        customerDTO.setTelefono(cliente.getTelefono());
        customerDTO.setNombre(cliente.getNombre());
        customerDTO.setDireccion(cliente.getDireccion());

        return customerDTO;
    }

    public CustomerDTO findById(Long id) {

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            return convertToDTO(clienteOptional.get());
        } else {
            throw new ResourceNotFoundException("Cliente con ID " + id + " no encontrada");
        }

    }

    public Cliente createCliente(Cliente cliente) {
        try{

            if (cliente.getNombre() == null || cliente.getIdentificacion() == null || cliente.getDireccion() == null) {
                throw new InsertDataException("Error: Campos obligatorios faltantes");
            }

            if (cliente.getEdad() <= 0) {
                throw new InsertDataException("Error: Edad no válida");
            }

            Cliente clientRegistration = clienteRepository.save(cliente);
            if (clientRegistration.getClienteId() != null) {
                return clientRegistration;
            } else {
                throw new InsertDataException("Error: No se pudo registrar el cliente");
            }
        } catch (InsertDataException e){
            throw new InsertDataException(e.getMessage());
        }

    }

    public Cliente updateCliente(Long id, Cliente clienteDetails) {
        try {
            Cliente cliente = clienteRepository.findById(id).orElse(null);

            if (cliente == null) {
                throw new ResourceNotFoundException("Cliente con ID " + id + " no encontrado");
            }

            cliente.setNombre(clienteDetails.getNombre());
            cliente.setGenero(clienteDetails.getGenero());
            cliente.setEdad(clienteDetails.getEdad());
            cliente.setIdentificacion(clienteDetails.getIdentificacion());
            cliente.setDireccion(clienteDetails.getDireccion());
            cliente.setTelefono(clienteDetails.getTelefono());
            cliente.setContrasenia(clienteDetails.getContrasenia());
            cliente.setEstado(clienteDetails.getEstado());

            return clienteRepository.save(cliente);

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public void deleteCliente(Long id) {
        try {
            clienteRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Cliente con ID " + id + " no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cliente", e);
        }
    }
}
