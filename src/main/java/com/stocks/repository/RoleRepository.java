package com.stocks.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocks.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findById(Long id);

	void deleteById(Long id);

	Role findByName(String string);

	boolean existsByName(String string);

}
