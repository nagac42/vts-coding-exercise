package com.c42.poc.vts.security;

import com.c42.poc.vts.entity.User;
import com.c42.poc.vts.exception.UserNotFoundException;
import com.c42.poc.vts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(userId,userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), List.of() // Add roles if needed
        );
    }
}
