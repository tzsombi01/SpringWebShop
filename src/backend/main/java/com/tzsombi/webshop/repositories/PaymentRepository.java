package com.tzsombi.webshop.repositories;

import com.tzsombi.webshop.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<CreditCard, Long> {

    @Query(value = "SELECT c " +
            "FROM CreditCard c inner join User u ON c.userId = u.id " +
            "WHERE c.isActive = true AND u.id = ?1")
    Optional<CreditCard> findActiveCardUnderUserById(Long userId);
}
