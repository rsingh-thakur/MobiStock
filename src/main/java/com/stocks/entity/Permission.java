package com.stocks.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Permissions_Details")
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long pid;

	private String name;

	@ManyToOne
	private Role role;

	 
}
