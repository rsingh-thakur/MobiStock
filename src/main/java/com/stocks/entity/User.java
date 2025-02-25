package com.stocks.entity;

import java.sql.Date;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String shopStoreName;

	private String shopAddress;

	@Column(unique = true) // Ensure uniqueness in the database
	private String email;

	private String password;

	private Date passwordUpdatedAt;

	@Column(name = "account_creation_date")
	private LocalDate CreationDate;

	@ManyToOne
	@JoinColumn(name = "role_id")
	@JsonBackReference
	private Role role;
 
	public int Status;

	@PrePersist
	protected void onCreate() {
		CreationDate = LocalDate.now();
	}
}