package com.tpe.service.user;

import com.tpe.entity.enums.Role;
import com.tpe.entity.user.UserRole;
import com.tpe.exception.BadRequestException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRole getUserRole(Role role){

        return userRoleRepository.findByEnumRole(role).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND));

    }

    public Role getRoleFromString (String role){

        if (role.equalsIgnoreCase(Role.ADMIN.getName())){
            return Role.ADMIN;

        } else if (role.equalsIgnoreCase(Role.MEMBER.getName())){
            return Role.MEMBER;

        }else if (role.equalsIgnoreCase(Role.EMPLOYEE.getName())){
            return Role.EMPLOYEE;

        }else throw new BadRequestException(String.format(ErrorMessages.ROLE_DOES_NOT_EXIST,role));
    }


    public List<UserRole> getAllUserRole() {
        return userRoleRepository.findAll();
    }
}
