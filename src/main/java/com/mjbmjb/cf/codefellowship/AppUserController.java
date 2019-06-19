package com.mjbmjb.cf.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AppUserController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/signup")
    public String getSignup() {
        return "signup";
    }
    @PostMapping("/signup")
    public RedirectView createUser(String username, String password, String firstName, String lastName, String dateOfBirth, String bio) {
        AppUser newUser = new AppUser(username, encoder.encode(password), firstName, lastName, dateOfBirth, bio);
        appUserRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/");
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/logoutSuccess")
    public String getLogout() {
        return "logoutsuccess";
    }

    @GetMapping("/myProfile")
    public String getMyProfile(Principal p, Model m) {
        AppUser user = appUserRepository.findByUsername(p.getName() );
        m.addAttribute("user", user);
        Iterable<Post> myPosts = postRepository.findByUsername(p.getName());
        m.addAttribute("myPosts", myPosts);

        return "myprofile";
    }
    @GetMapping("/users/{username}")
    public String getUserData(@PathVariable String username, Model m) {
        AppUser user = appUserRepository.findByUsername(username);
        m.addAttribute("user", user);
        return "myprofile";
    }

    @GetMapping("/createPost")
    public String getCreatePost() { return "createpost"; }

    @PostMapping("/createPost")
    public RedirectView createPost(Principal p, String body) {
        AppUser user = appUserRepository.findByUsername(p.getName());
        Post newPost = new Post(body, user, p.getName());
        postRepository.save(newPost);
        return new RedirectView("/myProfile");
    }

}
