package com.example.PruebaT.controller;

import com.example.PruebaT.dto.AccountDTO;
import com.example.PruebaT.exceptions.InsertDataException;
import com.example.PruebaT.exceptions.ResourceNotFoundException;
import com.example.PruebaT.model.Cuenta;
import com.example.PruebaT.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuentas")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("id/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id){
        try{
            AccountDTO cuenta = accountService.getAccountById(id);
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("number_account/{id}")
    public ResponseEntity<?> getAccountByNumber(@PathVariable String id){
        try{
            AccountDTO cuenta = accountService.getByNumberAccount(id);
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> createAccount(@RequestBody Cuenta cuenta){
        try{
            Cuenta nuevaCuenta = accountService.createAccount(cuenta);
            return new ResponseEntity<>(nuevaCuenta, HttpStatus.OK);
        } catch (InsertDataException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
