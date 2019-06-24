package com.mjbmjb.cf.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.*;

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

    @GetMapping("/")
    public String getCodefellowship(Principal p, Model m) {

//        System.out.println(p.getName());

        if(p == null) {
            m.addAttribute("principal", null);
        } else {
            m.addAttribute("principal", p.getName());
        }
        return "codefellowship";
    }

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
        return new RedirectView("/myProfile");
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
        List<AppUser> followed = new ArrayList<>(user.followedUsers);
        List<AppUser> allUsers = new ArrayList<>();
        Iterable<AppUser> usersToFollowIterable = appUserRepository.findAll();
        List<AppUser> usersToFollowList = new ArrayList<>();

        for(AppUser i : usersToFollowIterable) {
            usersToFollowList.add(i);
        }
        Set<AppUser> usersToFollow = new HashSet(usersToFollowList);
        usersToFollow.remove(user);
        usersToFollow.remove(user.followedUsers);
        List<AppUser> usrToFollow = new ArrayList<>(usersToFollow);
        m.addAttribute("user", user);
        m.addAttribute("posts", user.posts);
        m.addAttribute("usrToFollow", usrToFollow);
        m.addAttribute("principal", p.getName());


        return "myprofile";
    }

    @GetMapping("/myProfile/feed")
    public String getMyFeed(Principal p, Model m) {
        AppUser user = appUserRepository.findByUsername(p.getName() );
        List<AppUser> followedUsers = new ArrayList<>(user.followedUsers);
        List<Post> followedPosts = new ArrayList<>();
        for(int i = 0; i < followedUsers.size(); i++) {
            for(int j = 0; j < followedUsers.get(i).posts.size(); j++ ) {
                followedPosts.add(followedUsers.get(i).posts.get(j));
            }
        }
        m.addAttribute("followedPosts", followedPosts );
        m.addAttribute("principal", p.getName());

        return "feed";
    }

    @GetMapping("/users/{id}")
    public String getUserData(@PathVariable Long id, Model m, Principal p) {
        AppUser user = appUserRepository.findById(id).get();

//        if(user.username.equals(p.getName()) ){
//            return "myProfile";
//        }
        m.addAttribute("user", user);
        m.addAttribute("principal", p.getName());
        return "users";
    }

    @PostMapping("/users/{id}/follow")
    public RedirectView followUser(@PathVariable Long id, Principal p, Model m) {
        AppUser currUser = appUserRepository.findByUsername(p.getName());
        AppUser userToFollow = appUserRepository.findById(id).get();

        if( userToFollow.username.equals(p.getName() ) ) {

            throw new CannotFollowYourselfException("You cannot follow yourself.");
        }
        currUser.followedUsers.add(userToFollow);
        appUserRepository.save(currUser);

        return new RedirectView("/myProfile");
    }

    @PostMapping("/createPost")
    public RedirectView createPost(Principal p, String body) {
        AppUser user = appUserRepository.findByUsername(p.getName());
        Post newPost = new Post(body, user, p.getName());
        postRepository.save(newPost);
        return new RedirectView("/myProfile");
    }

}@ResponseStatus(value = HttpStatus.FORBIDDEN)
class CannotFollowYourselfException extends RuntimeException {
    public CannotFollowYourselfException(String s) {
        super(s);
    }
}
