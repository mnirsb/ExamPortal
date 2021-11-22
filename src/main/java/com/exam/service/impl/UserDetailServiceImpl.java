package com.exam.service.impl;

import com.exam.model.User;
import com.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = this.userRepository.findByUsername(username);

        if(user == null){
            System.out.println(username + " not Found!!!" );
            throw new UsernameNotFoundException("your username is incorrect!!!");
        }

        return user;
    }
}
