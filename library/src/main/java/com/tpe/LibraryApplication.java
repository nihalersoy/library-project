package com.tpe;

import com.tpe.entity.enums.Role;
import com.tpe.entity.user.UserRole;
import com.tpe.repository.user.UserRoleRepository;
import com.tpe.service.user.UserRoleService;
import com.tpe.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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


		}
	}

	/*


		// !!! Built_in Admin olusturuluyor
		if(userService.countAllAdmins() == 0) {
			UserRequest adminRequest = new UserRequest();
			adminRequest.setUsername("Admin");
			adminRequest.setEmail("aaa@bbb.com");
			adminRequest.setSsn("111-11-1111");
			adminRequest.setPassword("12345678");
			adminRequest.setName("Ahmet");
			adminRequest.setSurname("ahmet");
			adminRequest.setPhoneNumber("111-111-1111");
			adminRequest.setGender(Gender.MALE);
			adminRequest.setBirthDay(LocalDate.of(1980,2,2));
			adminRequest.setBirthPlace("Texas");

			userService.saveUser(adminRequest,"Admin");
		}

	}
	 */

}
