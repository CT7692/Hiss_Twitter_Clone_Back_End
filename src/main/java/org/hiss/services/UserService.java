package org.hiss.services;

import org.hiss.DTO.PasswordChangeDTO;
import org.hiss.DTO.PicDTO;
import org.hiss.entities.User;
import org.hiss.entities.UserDetailsImpl;
import org.hiss.services.impl.UserDetailsServiceImpl;

import java.security.Principal;
import java.util.List;

public interface UserService {
    List<User> getUsers();
    Boolean changeImage(String username, PicDTO picDTO);
    User getUserInfo(String username);
    Long getUserIDFromUsername(String username);
}
