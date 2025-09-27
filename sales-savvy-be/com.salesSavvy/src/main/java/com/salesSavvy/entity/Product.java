package com.salesSavvy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, max = 50, message = "Product name must be between 3 and 50 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Min(value = 1, message = "Price must be greater than 0")
    private BigDecimal price;

    private String photo;     // Product image URL/path

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @ElementCollection
    private List<String> reviews;  // List of reviews (just text)

    /* ---------- constructors ---------- */
    public Product() {}

    public Product(Long id, String name, String description, @Min(value = 1, message = "Price must be greater than 0") BigDecimal price,
                   String photo, String category, List<String> reviews) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photo = photo;
        this.category = category;
        this.reviews = reviews;
    }

    /* ---------- getters / setters ---------- */
    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public String getName()              { return name; }
    public void setName(String name)     { this.name = name; }

    public String getDescription()       { return description; }
    public void setDescription(String d) { this.description = d; }

    public BigDecimal getPrice()                { return price; }
    public void setPrice(BigDecimal price)      { this.price = price; }

    public String getPhoto()             { return photo; }
    public void setPhoto(String photo)   { this.photo = photo; }

    public String getCategory()          { return category; }
    public void setCategory(String c)    { this.category = c; }

    public List<String> getReviews()     { return reviews; }
    public void setReviews(List<String> r){ this.reviews = r; }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", description=" + description +
               ", price=" + price + ", photo=" + photo + ", category=" + category +
               ", reviews=" + reviews + "]";
    }
}
