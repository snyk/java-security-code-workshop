package io.snyk.demo.controller;


import io.snyk.demo.domain.User;
import io.snyk.demo.repo.SearchRepo;
import io.snyk.demo.repo.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Controller
public class UserController {

    @Autowired
    SearchRepo searchRepo;
    @Autowired
    UserRepo userRepo;

    private static final Logger logger = LogManager.getLogger(UserController.class);


    @GetMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("users", searchRepo.findAllUsers());
        return "users";
    }

    @PostMapping("/users")
    public String searchSubmit(Model model, @RequestParam String username) {
        if (username.isEmpty()) return allUsers(model);
        model.addAttribute("users", searchRepo.findUsersByUsername(username));
        return "users";
    }


    @GetMapping("/addUser")
    public String addUser(Model model, HttpServletResponse response) {
        setCookie(response);
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PostMapping("/addUser")
    public String addUser(Model model, @ModelAttribute User user) {
        logger.info("New user submitted: {}", user);
        userRepo.save(user);
        model.addAttribute("message", "user "+ user.getUsername() + " added");
        return "addUser";
    }

    private void setCookie(HttpServletResponse response) {
        Cookie newCookie = new Cookie("token", "java-code-workshop-12345");
        newCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(newCookie);
    }

}
