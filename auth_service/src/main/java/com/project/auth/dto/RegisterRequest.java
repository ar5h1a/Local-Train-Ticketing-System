package com.project.auth.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

    @NotBlank(message="Username required")
    @Size(min=3,max=20)
    private String username;

    @NotBlank(message="Password required")
    @Size(min=6,max=50)
    private String password;

    @Email(message="Invalid email")
    @NotBlank(message="Email required")
    private String email;

    @Pattern(regexp="[0-9]{10}", message="Invalid phone number")
    private String contact;

    // getters setters

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}
