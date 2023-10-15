package com.tpe.repository.user;

import com.tpe.entity.enums.Role;
import com.tpe.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT COUNT(ur) FROM User u JOIN u.userRoles ur WHERE ur.role = :role")
    int countAllAdmins(Role role);


}
