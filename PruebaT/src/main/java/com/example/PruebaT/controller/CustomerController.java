package com.example.PruebaT.controller;

import com.example.PruebaT.dto.CustomerDTO;
import com.example.PruebaT.exceptions.InsertDataException;
import com.example.PruebaT.exceptions.ResourceNotFoundException;
import com.example.PruebaT.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.PruebaT.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Cliente> getAllClientes() {
        return customerService.getAllClientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerByIdentificacion(@PathVariable Long id) {
        try {
            CustomerDTO cliente = customerService.findById(id);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Cliente clienteDetails) {
        try {
            Cliente updatedCliente = customerService.updateCliente(id, clienteDetails);
            return new ResponseEntity<>(updatedCliente.getClienteId(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCliente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> createCustomer(@RequestBody Cliente cliente) {
        try{
            Cliente nuevoCliente = customerService.createCliente(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.OK);
        } catch (InsertDataException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
