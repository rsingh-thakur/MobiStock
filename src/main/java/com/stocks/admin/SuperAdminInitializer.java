package com.stocks.admin;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stocks.entity.Permission;
import com.stocks.entity.Role;
import com.stocks.entity.User;
import com.stocks.repository.PermissionRepository;
import com.stocks.repository.RoleRepository;
import com.stocks.repository.UserRepository;

@Component
public class SuperAdminInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepo;

	private BCryptPasswordEncoder passwordEncoder;

	public SuperAdminInitializer(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder passwordEncoder, PermissionRepository permissionRepo) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.permissionRepo = permissionRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(String... args) {

		if (!userRepository.existsByEmail("superAdmin@gmail.com")) {

			// create new role ADMIN
			Role superAdminRole = roleRepository.findByName("ADMIN");
			if (superAdminRole == null) {
				superAdminRole = new Role();
				superAdminRole.setName("ADMIN");
				superAdminRole = roleRepository.save(superAdminRole);
               System.out.println("creating new admin");
			}
			// create super admin
			User superAdmin = new User();
			superAdmin.setEmail("superAdmin@gmail.com");
			superAdmin.setShopStoreName("Super");
			superAdmin.setShopAddress("Admin");
			String encodedPssword = passwordEncoder.encode("admin@1234");
			superAdmin.setPassword(encodedPssword);
			superAdmin.setRole(superAdminRole);
			superAdmin.setStatus(1);
			java.util.Date utilDate = new java.util.Date();
			Date date = new Date(utilDate.getTime());
			superAdmin.setCreationDate(LocalDate.now());
			superAdmin.setPasswordUpdatedAt(date);
			userRepository.save(superAdmin);

			// create permission with Role_admin
			if (!permissionRepo.existsByRoleName("ROLE_ADMIN")) {
				Permission permission = new Permission();
				permission.setName("ROLE_ADMIN");
				permission.setRole(superAdminRole);
				permissionRepo.save(permission);
			}
		}
		
		
		if(!roleRepository.existsByName("guest"))
        {
			try {
			Role guest = new Role();
			guest.setName("guest");
			Permission permission = new Permission();
			permission.setName("ROLE_MOBILES");
			permission.setRole(guest);
			permissionRepo.save(permission); 
			
			Permission permission2 = new Permission();
			permission2.setName("ROLE_EMI");
			permission2.setRole(guest);  
			permissionRepo.save(permission2);
			
			Permission permission3 = new Permission();
			permission3.setName("ROLE_SALE");
			permission3.setRole(guest);  
			permissionRepo.save(permission3);
			 
			List<Permission> permissions = new ArrayList<Permission>();
			permissions.add(permission);
			permissions.add(permission2);
			permissions.add(permission3);
			guest.setPermission(permissions);
			
			roleRepository.save(guest);
			System.out.print("Role guest is created now ");
			}catch(Exception e)
			{
				e.printStackTrace();
				System.out.print(" Exeption is occured while creating the default role "+e.getMessage());
				
			}
         } else{
			System.out.print(" Guest role already exists");
		}
		
	}
}
