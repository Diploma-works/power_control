package ru.guybydefault.foody.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.guybydefault.foody.repository.UserRepository;
import ru.guybydefault.foody.service.auth.UserPrincipalImpl;

import java.security.Principal;

@ControllerAdvice
public class AuthControllerAdvice {
    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public void initUserAndFocus(ModelMap modelMap, Principal principal) {
        if (principal != null) {
            modelMap.put("user", userRepository.findByLogin(principal.getName()));
        }
    }
}
