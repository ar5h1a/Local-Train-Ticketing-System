package com.project.auth.controller;

import com.project.auth.dto.LoginRequest;
import com.project.auth.dto.RegisterRequest;
import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.auth.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest req,
                                   HttpServletResponse response) {

        User dbUser = repo.findByUsername(req.getUsername()).orElse(null);

        if (dbUser == null)
            return ResponseEntity.status(401).body("User not found");

        if (!encoder.matches(req.getPassword(), dbUser.getPassword()))
            return ResponseEntity.status(401).body("Invalid password");

        String token = JwtUtil.generateToken(dbUser.getId(), dbUser.getUsername());

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);

        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Login successful");
    }



    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String contact,
            Model model
    ) {

        // check existing username
        if (repo.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        // create user
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEmail(email);
        user.setContact(contact);
        user.setRole("USER");
        user.setCreatedAt(java.time.LocalDateTime.now());

        // save to DB
        repo.save(user);

        // redirect to login page
        return "redirect:/auth/login";
    }

	/*
	 * @PostMapping("/login") public ResponseEntity<?> login(@RequestBody
	 * LoginRequest req) {
	 * 
	 * User dbUser = repo.findByUsername(req.getUsername()).orElse(null);
	 * 
	 * if (dbUser == null) return ResponseEntity.status(401).body("User not found");
	 * 
	 * if (!encoder.matches(req.getPassword(), dbUser.getPassword())) return
	 * ResponseEntity.status(401).body("Invalid password");
	 * 
	 * String token = JwtUtil.generateToken(dbUser.getId(), dbUser.getUsername());
	 * 
	 * return ResponseEntity.ok(token); }
	 */


   /*
    @PostMapping("/register")
    public String register(
    		
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String contact,
            Model model
    ) {
        if (repo.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole("USER");
        user.setEmail(email);
        user.setContact(contact);
        user.setCreatedAt(LocalDateTime.now());

        repo.save(user);
        
        // For now just print profile info
        System.out.println("Email: " + email);
        System.out.println("Contact: " + contact);

        // Later we will send this to User Service


        return "redirect:/auth/login";
    }
*/

    /*
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        User dbUser = repo.findByUsername(user.getUsername()).orElse(null);

        if (dbUser == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        if (!encoder.matches(user.getPassword(), dbUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        String token = JwtUtil.generateToken(dbUser.getUsername());

        return ResponseEntity.ok(token);
    }
*/



}
