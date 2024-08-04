package com.example.PruebaT.test.integration;

import com.example.PruebaT.model.Cliente;
import com.example.PruebaT.model.Cuenta;
import com.example.PruebaT.repository.AccountRepository;
import com.example.PruebaT.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Cuenta cuenta;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();

        cliente = new Cliente();
        cliente.setNombre("John Doe");
        cliente = customerRepository.save(cliente);

        cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123456789");
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setSaldoInicial(1000.0);
        cuenta.setEstado("Activo");
        cuenta.setCliente(cliente);
        cuenta = accountRepository.save(cuenta);
    }

    @Test
    void testGetAccountById() throws Exception {
        mockMvc.perform(get("/cuentas/id/{id}", cuenta.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value(cuenta.getNumeroCuenta()));
    }

    @Test
    void testGetAccountByNumber() throws Exception {
        mockMvc.perform(get("/cuentas/number_account/{id}", cuenta.getNumeroCuenta())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value(cuenta.getNumeroCuenta()));
    }

    @Test
    void testCreateAccount() throws Exception {
        Cuenta newAccount = new Cuenta();
        newAccount.setNumeroCuenta("987654321");
        newAccount.setTipoCuenta("Corriente");
        newAccount.setSaldoInicial(5000.0);
        newAccount.setEstado("Activo");
        newAccount.setCliente(cliente);

        mockMvc.perform(post("/cuentas/insert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("987654321"));
    }

    @Test
    void testDeleteAccount() throws Exception {
        mockMvc.perform(delete("/cuentas/{id}", cuenta.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Cuenta> deletedAccount = accountRepository.findById(cuenta.getId());
        assert(deletedAccount.isEmpty());
    }
}
