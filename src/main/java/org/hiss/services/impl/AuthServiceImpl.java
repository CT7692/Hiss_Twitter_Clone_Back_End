package org.hiss.services.impl;

import org.hiss.DTO.LoginDTO;
import org.hiss.DTO.PasswordChangeDTO;
import org.hiss.DTO.RegisterDTO;
import org.hiss.entities.User;
import org.hiss.exceptions.ResourceNotFoundException;
import org.hiss.repositories.UserRepository;
import org.hiss.response.LoginResponse;
import org.hiss.response.RequestResponse;
import org.hiss.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTServiceImpl jwtService;

    @Override
    public RequestResponse register(RegisterDTO registerDTO) {

        boolean success = false;
        boolean usernameTaken =
                userRepository.existsByName(registerDTO.getName());

        if(!usernameTaken) {
            User user = new User();
            user.setEmail(registerDTO.getEmail());
            user.setName(registerDTO.getName());
            user.setPassword(
                    passwordEncoder
                            .encode(registerDTO.getPassword()));

            user.setImageURL("https://static-00.iconduck.com/assets.00/vulture-icon-2048x2048-fczgn9sx.png");

            userRepository.save(user);
            success = true;
        }

        return new RequestResponse(success);
    }

    @Override
    public LoginResponse login(LoginDTO loginDTO) {

        LoginResponse response = new LoginResponse();
        String name = loginDTO.getName();
        String password = loginDTO.getPassword();

        Authentication authentication =
                authenticationManager.authenticate(new
                        UsernamePasswordAuthenticationToken(name, password));

        if(authentication.isAuthenticated()) {
            response.setToken(jwtService.generateToken(loginDTO.getName()));
            response.setError(false);
        }

        else{
            response.setToken(null);
            response.setError(true);
        }

        return response;
    }

    @Override
    public boolean changePassword(String username, PasswordChangeDTO passwordChangeDTO) {

        User user = userRepository.
                findByName(username);

        if(user == null)
            throw new ResourceNotFoundException("User", "username", username);
        else{
            if(passwordChangeDTO.getOld_password().equals(passwordChangeDTO.getNew_password()))
                throw new RuntimeException("Invalid password.");

            Authentication authentication =
                    authenticationManager.authenticate(new
                            UsernamePasswordAuthenticationToken(user.getName(), passwordChangeDTO.getOld_password()));

            if(authentication.isAuthenticated()) {

                user.setPassword(passwordEncoder
                        .encode(passwordChangeDTO.getNew_password()));
                userRepository.save(user);
            }

            else
                throw new RuntimeException("Incorrect credentials.");
        }
        return true;
    }
}
