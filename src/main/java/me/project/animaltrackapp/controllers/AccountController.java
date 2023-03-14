package me.project.animaltrackapp.controllers;

import me.project.animaltrackapp.exceptions.AccountAlreadyExistsException;
import me.project.animaltrackapp.exceptions.InvalidInputDataException;
import me.project.animaltrackapp.models.Account;
import me.project.animaltrackapp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Account> create(@RequestBody Account account) {
        try {
            Account createdAccount = accountService.create(account);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (InvalidInputDataException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (AccountAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.findById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts/search")
    public List<Account> searchAccounts(@RequestParam(required = false) String firstName,
                                        @RequestParam(required = false) String lastName,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return accountService.search(firstName, lastName, email, from, size);
    }
}
