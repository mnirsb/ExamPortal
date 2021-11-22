package com.exam.repository;

import com.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

        //creating findbyusername function

        public User findByUsername(String username);
}
