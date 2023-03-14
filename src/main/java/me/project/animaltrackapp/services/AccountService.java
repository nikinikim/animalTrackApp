package me.project.animaltrackapp.services;

import me.project.animaltrackapp.exceptions.AccountNotFoundException;
import me.project.animaltrackapp.models.Account;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AccountService {
    Account create(Account account);

    Account findById(Long id) throws AccountNotFoundException;

    Account findByEmail(String email) throws AccountNotFoundException;

    List<Account> search(String firstName, String lastName, String email, int from, int size);

    Account update(Long id, Account account) throws AccountNotFoundException;

    abstract void delete(Long id);
}
