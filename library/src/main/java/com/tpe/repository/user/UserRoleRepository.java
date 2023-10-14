package com.tpe.repository.user;

import com.tpe.entity.enums.Role;
import com.tpe.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

    @Query("SELECT u FROM UserRole u WHERE u.role = ?1")
    Optional<UserRole> findByEnumRole(Role role);





}
