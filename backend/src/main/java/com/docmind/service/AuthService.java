package com.docmind.service;

import com.docmind.dto.AuthRequest;
import com.docmind.dto.AuthResponse;
import com.docmind.dto.RegisterRequest;
import com.docmind.dto.UserDto;
import com.docmind.entity.User;
import com.docmind.entity.UserProfile;
import com.docmind.repository.UserProfileRepository;
import com.docmind.repository.UserRepository;
import com.docmind.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuditLogService auditLogService;

    public AuthService(UserRepository userRepository,
                       UserProfileRepository userProfileRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider,
                       AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use.");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .user(savedUser)
                .readinessScore(15)
                .build();
        userProfileRepository.save(profile);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);

        auditLogService.logAction(savedUser, "REGISTER", "User", "User registered successfully", null, null);

        UserDto userDto = mapToUserDto(savedUser);
        return AuthResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        auditLogService.logAction(user, "LOGIN", "User", "User logged in successfully", null, null);

        UserDto userDto = mapToUserDto(user);
        return AuthResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }

    public UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
