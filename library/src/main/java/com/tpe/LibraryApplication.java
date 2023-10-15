package com.tpe;

import com.tpe.entity.enums.Role;
import com.tpe.entity.user.UserRole;
import com.tpe.payload.request.user.SaveUserRequest;
import com.tpe.repository.user.UserRoleRepository;
import com.tpe.service.user.UserRoleService;
import com.tpe.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
@SpringBootApplication
public class LibraryApplication implements CommandLineRunner {

	private final UserRoleService userRoleService;
	private final UserRoleRepository userRoleRepository;
	private final UserService userService;

	public LibraryApplication(UserRoleService userRoleService, UserRoleRepository userRoleRepository, UserService userService) {
		this.userRoleService = userRoleService;
		this.userRoleRepository = userRoleRepository;
		this.userService = userService;
	}


	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//Role tablosunu olusturucaz
		if (userRoleService.getAllUserRole().isEmpty()){

			UserRole admin = new UserRole();
			admin.setRole(Role.ADMIN);
			admin.setName("ADMIN");
			userRoleRepository.save(admin);

			UserRole member = new UserRole();
			member.setRole(Role.MEMBER);
			member.setName("MEMBER");
			userRoleRepository.save(member);

			UserRole employee = new UserRole();
			employee.setRole(Role.EMPLOYEE);
			employee.setName("EMPLOYEE");
			userRoleRepository.save(employee);

		}

		//Built-in admin yapıyoruz
		if (userService.countAllAdmins()==0){

			SaveUserRequest adminRequest = new SaveUserRequest();
			adminRequest.setEmail("aaa@bbb.com");
			adminRequest.setPassword("12345678");
			adminRequest.setFirstName("Ayşe");
			adminRequest.setLastName("Nihal");
			adminRequest.setAddress("Ankara,Turkey");
			adminRequest.setPhone("999-999-9999");
			adminRequest.setBirthDate(LocalDate.of(1994,5,22));
			adminRequest.setRole("ADMIN");


			userService.saveAdmin(adminRequest);
		}
	}


}
