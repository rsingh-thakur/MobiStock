package com.stocks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stocks.entity.Role;
import com.stocks.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmailAndPassword(String email, String password);

	User findByEmail(String currentUser);

	boolean existsByEmail(String string);

	List<User> findByRole(Role roleToDelete);

}