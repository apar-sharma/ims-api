package com.apsharma.ims_api.user.service;

import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.user.model.UserPrincipal;
import com.apsharma.ims_api.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username + " not found");
        }
        return new UserPrincipal(user);
    }
}
