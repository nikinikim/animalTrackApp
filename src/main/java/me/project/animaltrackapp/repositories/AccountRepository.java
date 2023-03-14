package me.project.animaltrackapp.repositories;

import me.project.animaltrackapp.models.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);

    Optional<Account> findByEmail(String email);
    @Query ("SELECT a FROM Account a " +
            "WHERE (:firstName IS NULL OR LOWER(a.firstName) LIKE %:firstName%) " +
            "AND (:lastName IS NULL OR LOWER(a.lastName) LIKE %:lastName%) " +
            "AND (:email IS NULL OR LOWER(a.email) LIKE %:email%)")
    List<Account> search(@Param("firstName") String firstName,
                         @Param("lastName") String lastName,
                         @Param("email") String email,
                         Pageable pageable);
}
