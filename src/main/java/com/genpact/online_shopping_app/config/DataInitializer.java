package com.genpact.online_shopping_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.genpact.online_shopping_app.entity.Role;
import com.genpact.online_shopping_app.repository.RoleRepository;

/** Makes a fresh database usable: registration needs the role rows 101/102 to exist. */
@Component
public class DataInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;

	public DataInitializer(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public void run(String... args) {
		seed(101, "user");
		seed(102, "vendor");
		seed(103, "admin");
	}

	private void seed(int id, String name) {
		if (!roleRepository.existsById(id)) {
			Role role = new Role(name);
			role.setRoleId(id);
			roleRepository.save(role);
		}
	}
}
