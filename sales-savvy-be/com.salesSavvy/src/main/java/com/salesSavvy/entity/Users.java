package com.salesSavvy.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String gender;

    private String dob;

    @NotBlank(message = "Role cannot be blank")
    private String role;   // USER / ADMIN

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cart")
    private Cart cart;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Orders> orders;

    /* ---------- constructors ---------- */
    public Users() {}

    public Users(Long id, String username, String email, String password,
                 String gender, String dob, String role, Cart cart, List<Orders> orders) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.role = role;
        this.cart = cart;
        this.orders = orders;
    }


    /* ---------- getters / setters ---------- */
    public Long getId()                { return id; }
    public void setId(Long id)         { this.id = id; }

    public String getUsername()        { return username; }
    public void setUsername(String u)  { this.username = u; }

    public String getEmail()           { return email; }
    public void setEmail(String e)     { this.email = e; }

    public String getPassword()        { return password; }
    public void setPassword(String p)  { this.password = p; }

    public String getGender()          { return gender; }
    public void setGender(String g)    { this.gender = g; }

    public String getDob()             { return dob; }
    public void setDob(String d)       { this.dob = d; }

    public String getRole()            { return role; }
    public void setRole(String r)      { this.role = r; }

    public Cart getCart()              { return cart; }
    public void setCart(Cart cart)     { this.cart = cart; }

    public List<Orders> getOrders() { return orders; }
    public void setOrders(List<Orders> orders) { this.orders = orders; }
    
    @Override
    public String toString() {
        return "Users [id=" + id + ", username=" + username + ", email=" + email +
               ", password=" + password + ", gender=" + gender + ", dob=" + dob +
               ", role=" + role + ", cart=" + cart + ", orders=" + orders + "]";
    }
}
