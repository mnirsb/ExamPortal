package com.exam.config;

import com.exam.service.impl.UserDetailServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokerHeader = request.getHeader("Authorization");
        System.out.println(requestTokerHeader);
        String username = null;
        String jwtToken = null;

        //------------------------------------------------------------------------------------
        //Here we try to get the token
        //------------------------------------------------------------------------------------

        if(requestTokerHeader != null && requestTokerHeader.startsWith("Bearer ")){
            jwtToken = requestTokerHeader.substring(7);
            try{
                username = this.jwtUtils.extractUsername(jwtToken);
            }catch (ExpiredJwtException e){
                e.printStackTrace();
                System.out.println("JWT token is expired");
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            System.out.println("Invalid token, not starts with bearer string");
        }

        //-------------------------------------------------------------------------------------
        // Validate Token Here
        //-------------------------------------------------------------------------------------

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            final UserDetails userDetails= this.userDetailService.loadUserByUsername(username);

            if(this.jwtUtils.validateToken(jwtToken, userDetails)){
                //token is valid
                //------------------------------------------------------------------------------------------
                // if valid then Set Authentication
                // UsernamePasswordAuthenticationToken -> Provide 3 things, userdetails, null and Authorities
                //-------------------------------------------------------------------------------------------
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }else{
            System.out.println("Your Token is InValid!");
        }

        filterChain.doFilter(request,response);
    }
}
