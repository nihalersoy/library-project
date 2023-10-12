package com.tpe.security.service;

import com.tpe.entity.user.User;
import com.tpe.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            User user = userRepository.findByEmail(email);
            if (user!=null){
                return new UserDetailsImpl(
                        user.getPhone(),
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getUserRole().getRole().getName());
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User could not be found");
        }

        return null;
    }


}
