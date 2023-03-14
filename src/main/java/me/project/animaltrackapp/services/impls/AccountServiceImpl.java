package me.project.animaltrackapp.services.impls;

import me.project.animaltrackapp.exceptions.AccountAlreadyExistsException;
import me.project.animaltrackapp.exceptions.AccountNotFoundException;
import me.project.animaltrackapp.exceptions.InvalidInputDataException;
import me.project.animaltrackapp.models.Account;
import me.project.animaltrackapp.repositories.AccountRepository;
import me.project.animaltrackapp.services.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
@Repository
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account create(Account account) {
        if (StringUtils.isBlank(account.getFirstName()) || StringUtils.isBlank(account.getLastName())
                || StringUtils.isBlank(account.getEmail()) || StringUtils.isBlank(account.getPassword())) {
            throw new InvalidInputDataException("Отсутствуют некоторые входные данные");
        }
        if (findByEmail(account.getEmail()) != null) {
            throw new AccountAlreadyExistsException("Аккаунт с указанным email уже существует");

        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Аккаунт с id:" + id + " не найден"));
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<Account> search(String firstName, String lastName, String email, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return accountRepository.search(firstName, lastName, email, pageable);
    }

    @Override
    public Account update(Long id, Account account) throws AccountNotFoundException {
        Account existingAccount = findById(id);

        if (StringUtils.isNotBlank(account.getFirstName())) {
            existingAccount.setFirstName(account.getFirstName());
        }
        if (StringUtils.isNotBlank(account.getLastName())) {
            existingAccount.setLastName(account.getLastName());
        }
        if (StringUtils.isNotBlank(account.getPassword())) {
            existingAccount.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        return accountRepository.save(existingAccount);
    }
    @Override
    public void delete(Long id) {
        Account account = findById(id);
        accountRepository.delete(account);
    }
}
