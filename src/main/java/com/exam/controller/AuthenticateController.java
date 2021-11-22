package com.exam.controller;

import com.exam.config.JwtUtils;
import com.exam.model.JwtRequest;
import com.exam.model.JwtResponse;
import com.exam.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticateController {

    //--------------------------------------------------------------------------------------------------------------
    // Objective : This class will Generate Token
    // Step 1: We will authenticate Username and Password first using Method -> authenticate
    // -> It requires Authentication Manager, So Create Authentication Manager bean in MySecurityConfig to use it
    // -> It requires User detail service impl
    // -> Also requires UTIL to generate token
    //**************************************************************************************************************
    // Step 2 : Will create a API which will generate the token
    //--------------------------------------------------------------------------------------------------------------

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtils jwtUtils;

    //--------------------------------------------------------------------------------------------------------------
    //Step 2
    //--------------------------------------------------------------------------------------------------------------

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {

        try{
            //Authenticate method is called from below
            //It will authenticate the user
            authenticate(jwtRequest.getUsername(),jwtRequest.getPassword());

        }catch (UsernameNotFoundException e){

            e.printStackTrace();
            throw new Exception("User not Found");
        }

        //Once user get Authenticated, now load the user

        UserDetails userDetails = this.userDetailService.loadUserByUsername(jwtRequest.getUsername());

        //now we can generate tokens as user got authenticated

        String token = this.jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    //--------------------------------------------------------------------------------------------------------------
    //Step 1
    //--------------------------------------------------------------------------------------------------------------
    private void authenticate(String username, String password) throws Exception {

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        }catch (DisabledException disabledException){
            disabledException.printStackTrace();
            throw new Exception("User is Disabled" + disabledException.getMessage());

        }catch (BadCredentialsException badCredentialsException){
            badCredentialsException.printStackTrace();
            throw new Exception("Invalid Credentials" + badCredentialsException.getMessage());
        }
    }

}
