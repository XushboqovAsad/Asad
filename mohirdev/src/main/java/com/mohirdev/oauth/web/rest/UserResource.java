package com.mohirdev.oauth.web.rest;

import com.mohirdev.oauth.service.UserService;
import com.mohirdev.oauth.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/hello")
    public String hello(){
        return "Hello oauth2 mohirdev";
    }
    @GetMapping("/user")
    public ResponseEntity<User> getUser(Principal principal){
        if (principal instanceof AbstractAuthenticationToken){
            return ResponseEntity.ok(userService.getUserFromAuthentication((AbstractAuthenticationToken) principal));
        }else {
            throw new IllegalArgumentException("Error");
        }
    }
    @GetMapping("/user-mail")
    @ResponseBody
    public String getUserInfo(@AuthenticationPrincipal OAuth2User principal){
        String email = principal.getAttribute("email");
        String subject = "Avtorizatsiyadan o'tish";
        String message = "Ushbu akkount " + email + " tizimdan avtorizatsiyadan o'tdi";
        userService.sendEmail(email, subject, message);
        return "Ushbu akkount " + email + " tizimdan avtorizatsiyadan o'tdi";

    }
}
