package org.hiss.services;

import org.hiss.DTO.LoginDTO;
import org.hiss.DTO.PasswordChangeDTO;
import org.hiss.DTO.RegisterDTO;
import org.hiss.response.LoginResponse;
import org.hiss.response.RequestResponse;

public interface AuthService {
    RequestResponse register(RegisterDTO registerDTO);
    LoginResponse login(LoginDTO loginDTO);
    boolean changePassword(String username, PasswordChangeDTO passwordChangeDTO);

}
