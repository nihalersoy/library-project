package com.tpe.security.service;

import com.tpe.entity.user.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            User user = userRepository.findByEmail(email).orElseThrow(()->
                    new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND,email)));

            Set<String> userRoleNames = new HashSet<>();
            user.getUserRoles().forEach(userRole -> userRoleNames.add(userRole.getName()));

            if (user!=null){
                return new UserDetailsImpl(
                        user.getPhone(),
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        userRoleNames);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User could not be found");
        }

        return null;
    }


}
