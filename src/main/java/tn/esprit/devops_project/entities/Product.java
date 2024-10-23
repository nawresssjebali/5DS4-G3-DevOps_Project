package tn.esprit.devops_project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product implements Serializable {

    // Adding a logger for the Product class
    private static final Logger logger = LogManager.getLogger(Product.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idProduct;

    String title;

    float price;

    int quantity;

    @Enumerated(EnumType.STRING)
    ProductCategory category;

    @ManyToOne
    @JsonIgnore
    Stock stock;

    // Additional business logic methods (optional)
    public void logProductInfo() {
        logger.info("Product Info: Title: {}, Price: {}, Quantity: {}, Category: {}", title, price, quantity, category);
    }
}
