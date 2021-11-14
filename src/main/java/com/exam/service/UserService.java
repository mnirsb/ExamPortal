package com.exam.service;

import com.exam.model.User;
import com.exam.model.UserRole;

import java.util.Set;

public interface UserService {

    //Creating user
    public User createUser(User user, Set<UserRole> userRoles) throws Exception;

    //get user
    public User getUser(String username);

    //delete user
    public void deleteUser(Long userId);

}
