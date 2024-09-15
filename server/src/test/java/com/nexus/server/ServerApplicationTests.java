package com.nexus.server;

import com.nexus.server.entities.Role;
import com.nexus.server.entities.User;
import com.nexus.server.repositories.IActivityTypeRepository;
import com.nexus.server.repositories.IRoleRepository;
import com.nexus.server.repositories.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ServerApplicationTests {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private IActivityTypeRepository activityTypeRepository;

	@Test
	public void createDefaultRoles() {
		Role admin = new Role();
		admin.setName("ADMIN");

		Role boss = new Role();
		boss.setName("BOSS");

		Role employee = new Role();
		employee.setName("EMPLOYEE");

		roleRepository.saveAll(List.of(admin, boss, employee));
		assertEquals(3, roleRepository.findAll().size());
	}

	@Test
	public void createDefaultUsers() {
		User admin = new User();
		admin.setRole(roleRepository.findById(1L).orElse(null));
		admin.setDui("00000000-0");
		admin.setEmail("admin");
		admin.setGender("M");
		admin.setBirthday(LocalDate.now());
		User saved = userRepository.save(admin);

		assertTrue(saved.getEmail().equalsIgnoreCase(admin.getEmail()));
	}
}
