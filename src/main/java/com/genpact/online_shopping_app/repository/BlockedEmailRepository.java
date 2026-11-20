package com.genpact.online_shopping_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.genpact.online_shopping_app.entity.BlockedEmail;

@Repository
public interface BlockedEmailRepository extends JpaRepository<BlockedEmail, Integer> {

	boolean existsByEmailIgnoreCase(String email);
}
