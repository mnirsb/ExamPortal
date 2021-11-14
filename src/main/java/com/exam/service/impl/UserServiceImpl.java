package com.exam.service.impl;

import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repository.RoleRepository;
import com.exam.repository.UserRepository;
import com.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {

        //Check if user is already present ?
        User local = this.userRepository.findByUserName(user.getUserName());

        if(local != null){
            System.out.println("User is already present!!");
            throw new Exception("User is already Present");
        }else{
            //We will create new user
            //First we will store the role

            for(UserRole ur:userRoles){
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            local = this.userRepository.save(user);
        }
        return local;
    }

    //Getting user by username
    @Override
    public User getUser(String username) {
        return this.userRepository.findByUserName(username);
    }

    //delete user by userid
    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }
}
