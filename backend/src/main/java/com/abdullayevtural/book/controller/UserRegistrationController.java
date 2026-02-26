package com.abdullayevtural.book.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.book.dto.AuthRequest;
import com.abdullayevtural.book.entity.RoleEntity;
import com.abdullayevtural.book.entity.UserEntity;
import com.abdullayevtural.book.repository.RoleRepository;
import com.abdullayevtural.book.repository.UserRepository;

@RestController
public class UserRegistrationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/reg-user")
	public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			return ResponseEntity.status(409).body("Istifadeci artiq movcuddur");
		}

		UserEntity user = new UserEntity();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		Set<RoleEntity> roles = new HashSet<>();
		roleRepository.findByName("ROLE_GET").ifPresent(roles::add);
		if (roles.isEmpty()) {
			RoleEntity defaultRole = new RoleEntity();
			defaultRole.setName("ROLE_GET");
			defaultRole = roleRepository.save(defaultRole);
			roles.add(defaultRole);
		}
		user.setRoles(roles);

		userRepository.save(user);

		return ResponseEntity.status(201).body("Qeydiyyat ugurla tamamlandi");
	}
}

