package com.example.PruebaT.test.service;

import com.example.PruebaT.dto.CustomerDTO;
import com.example.PruebaT.exceptions.ResourceNotFoundException;
import com.example.PruebaT.model.Cliente;
import com.example.PruebaT.repository.CustomerRepository;
import com.example.PruebaT.service.CustomerService;
import com.example.PruebaT.exceptions.InsertDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente());
        when(customerRepository.findAll()).thenReturn(clientes);

        List<Cliente> result = customerService.getAllClientes();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Success() {
        Long clienteId = 1L;
        Cliente cliente = new Cliente();
        cliente.setClienteId(clienteId);
        when(customerRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        CustomerDTO result = customerService.findById(clienteId);

        assertNotNull(result);
        assertEquals(clienteId, result.getId());
        verify(customerRepository, times(1)).findById(clienteId);
    }

    @Test
    public void testFindById_NotFound() {
        Long clienteId = 99L;
        when(customerRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.findById(clienteId));
        verify(customerRepository, times(1)).findById(clienteId);
    }

    @Test
    public void testCreateCliente_Success() {
        Cliente cliente = new Cliente();
        cliente.setClienteId(12L);
        cliente.setNombre("Test");
        cliente.setIdentificacion("123e890");
        cliente.setDireccion("Test Address");
        cliente.setEdad(30);
        when(customerRepository.save(cliente)).thenReturn(cliente);

        Cliente result = customerService.createCliente(cliente);

        assertNotNull(result);
        assertEquals(cliente.getNombre(), result.getNombre());
        verify(customerRepository, times(1)).save(cliente);
    }

    @Test
    public void testCreateCliente_MissingFields() {
        Cliente cliente = new Cliente();
        cliente.setEdad(30);

        assertThrows(InsertDataException.class, () -> customerService.createCliente(cliente));
        verify(customerRepository, never()).save(cliente);
    }

    @Test
    public void testCreateCliente_InvalidAge() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Test");
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Test Address");
        cliente.setEdad(-1);

        assertThrows(InsertDataException.class, () -> customerService.createCliente(cliente));
        verify(customerRepository, never()).save(cliente);
    }

    @Test
    public void testUpdateCliente_Success() {
        Long clienteId = 1L;
        Cliente existingCliente = new Cliente();
        existingCliente.setClienteId(clienteId);
        Cliente updatedDetails = new Cliente();
        updatedDetails.setNombre("Updated Name");
        when(customerRepository.findById(clienteId)).thenReturn(Optional.of(existingCliente));
        when(customerRepository.save(existingCliente)).thenReturn(existingCliente);

        Cliente result = customerService.updateCliente(clienteId, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Name", result.getNombre());
        verify(customerRepository, times(1)).findById(clienteId);
        verify(customerRepository, times(1)).save(existingCliente);
    }

    @Test
    public void testUpdateCliente_NotFound() {
        Long clienteId = 1L;
        Cliente updatedDetails = new Cliente();
        when(customerRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCliente(clienteId, updatedDetails));
        verify(customerRepository, times(1)).findById(clienteId);
        verify(customerRepository, never()).save(any(Cliente.class));
    }

    @Test
    public void testDeleteCliente_Success() {
        Long clienteId = 1L;

        doNothing().when(customerRepository).deleteById(clienteId);

        customerService.deleteCliente(clienteId);

        verify(customerRepository, times(1)).deleteById(clienteId);
    }

    @Test
    public void testDeleteCliente_NotFound() {
        Long clienteId = 1L;
        doThrow(EmptyResultDataAccessException.class).when(customerRepository).deleteById(clienteId);

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCliente(clienteId));
        verify(customerRepository, times(1)).deleteById(clienteId);
    }
}

