package org.hiss.services.impl;

import org.hiss.DTO.PicDTO;
import org.hiss.entities.User;
import org.hiss.exceptions.ResourceNotFoundException;
import org.hiss.repositories.UserRepository;
import org.hiss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.Principal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserInfo(String username) {
        User user = userRepository.findByName(username);
        if(user == null)
            throw new ResourceNotFoundException("User", "Username", username);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Boolean changeImage(String username, PicDTO picDTO) {
        User user = userRepository.findByName(username);

        if(user == null)
            throw new ResourceNotFoundException("User", "Username", username);

        else {
            String path = "C:\\Users\\josep\\twitter_clone\\public\\"
                    + picDTO.getNew_img_url();

            File pictureFile = new File(path);

            if(pictureFile.exists()) {
                user.setImageURL(picDTO.getNew_img_url());
                userRepository.save(user);
                return true;
            }
            else
                return false;
        }
    }

    @Override
    public Long getUserIDFromUsername(String username) {
        User user = userRepository.findByName(username);
        if(user == null)
            throw new ResourceNotFoundException("User", "Username", username);
        return user.getUserID();
    }
}
