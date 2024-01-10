package com.mohirdev.oauth.service;

import com.mohirdev.oauth.domain.User;
import com.mohirdev.oauth.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    public User getUserFromAuthentication(AbstractAuthenticationToken authToken){
        Map<String, Object> attributes;
        if (authToken instanceof OAuth2AuthenticationToken){
            attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
        }else {
            throw new IllegalArgumentException("Error ");
        }
        User user = getUser(attributes);
        if (user != null){
            userRepository.save(user);
        }
        return user;
    }
    private User getUser(Map<String, Object> attributes){
        User user = new User();
        if (attributes.get("uid") != null){
            user.setUid((String) attributes.get("uid"));
        }
        if (attributes.get("given_name") != null){
            user.setFirstName((String) attributes.get("given_name"));
        }
        if (attributes.get("family_name") != null){
            user.setLastName((String) attributes.get("family_name"));
        }
        if (attributes.get("locale") != null){
            user.setLangKey((String) attributes.get("locale"));
        }
        if (attributes.get("email_verified") != null){
            user.setActivated((Boolean) attributes.get("email_verified"));
        }
        if (attributes.get("email") != null){
            user.setEmail((String) attributes.get("email"));
        }
        if (attributes.get("picture") != null){
            user.setImageUrl((String) attributes.get("picture"));
        }
        return user;
    }
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String message){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try{
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message);

            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
