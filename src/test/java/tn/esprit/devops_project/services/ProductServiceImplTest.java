package tn.esprit.devops_project.services;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {
  @Autowired
    private ProductServiceImpl productService;
  @Autowired
   private StockRepository stockRepository;
    @Autowired
    private ProductRepository productRepository;


    @Test
    public void testAddProduct() {

        // Create and save a new stock instance
        Stock stock = new Stock();
        stock.setIdStock(1L); // Set a valid ID for the stock
        // Optionally, set other fields of the Stock if necessary
        stockRepository.save(stock); // Save stock to the repository (if needed)

        // Create a new product
        Product product = new Product();
        product.setTitle("Test Product");
        product.setPrice(99.99f);
        product.setQuantity(10);
        product.setCategory(ProductCategory.ELECTRONICS);

        // Add product to stock by passing the stock ID
        Product savedProduct = productService.addProduct(product, stock.getIdStock());

        // Validate that product has been saved correctly
        assertNotNull(savedProduct.getIdProduct(), "Product ID should not be null after saving");
        assertEquals("Test Product", savedProduct.getTitle(), "Product title should match");
        assertEquals(99.99f, savedProduct.getPrice(), "Product price should match");
        assertEquals(10, savedProduct.getQuantity(), "Product quantity should match");
        assertEquals(stock.getIdStock(), savedProduct.getStock().getIdStock(), "Product should be associated with the correct stock");
    }
    @Test
        public void testRetrieveProduct_Success() {
            // Arrange: Save a product to the in-memory database
            Product product = new Product();
            product.setTitle("Test Product");
            Product savedProduct = productRepository.save(product);  // Save the product and get the saved object, which will have the generated ID

            Long productId = savedProduct.getIdProduct();  // The ID of the saved product

            // Act: Retrieve the product by ID
            Product retrievedProduct = productService.retrieveProduct(productId);

            // Assert: Ensure the retrieved product matches what was saved
            assertNotNull(retrievedProduct);  // Check that the retrieved product is not null
            assertEquals(productId, retrievedProduct.getIdProduct()
            );  // Verify the ID matches
            assertEquals("Test Product", retrievedProduct.getTitle());  // Verify the name matches
        }

        @Test
        public void testRetrieveProduct_ProductNotFound() {
            // Arrange: Use a non-existent product ID
            Long nonExistentProductId = 999L;  // A non-existent ID for testing

            // Act & Assert: Expect a NullPointerException when retrieving a non-existent product
            NullPointerException exception = assertThrows(NullPointerException.class, () -> {
                productService.retrieveProduct(nonExistentProductId);
            });

            // Assert: Check the exception message is as expected
            assertEquals("Product not found", exception.getMessage());
        }
    @Test
    public void testRetrieveAllProduct() {

        // Arrange: Save some products to the in-memory database
        Product product1 = new Product();
        product1.setTitle("Test Product 1");
        product1.setPrice(19.99f);
        product1.setQuantity(5);
        product1.setCategory(ProductCategory.ELECTRONICS);
        productRepository.save(product1); // Save product 1

        Product product2 = new Product();
        product2.setTitle("Test Product 2");
        product2.setPrice(29.99f);
        product2.setQuantity(10);
        product2.setCategory(ProductCategory.CLOTHING);
        productRepository.save(product2); // Save product 2

        // Act: Call the method to retrieve all products
        List<Product> retrievedProducts = productService.retreiveAllProduct();

        // Assert: Verify that the retrieved products match the expected results
        assertNotNull(retrievedProducts, "The retrieved product list should not be null");

        // Verify that the retrieved products contain the expected products
        assertTrue(retrievedProducts.stream().anyMatch(p -> p.getTitle().equals("Test Product 1")), "The retrieved products should contain 'Test Product 1'");
        assertTrue(retrievedProducts.stream().anyMatch(p -> p.getTitle().equals("Test Product 2")), "The retrieved products should contain 'Test Product 2'");
    }
    @Test
    public void testRetrieveProductByCategory() {
        productRepository.deleteAll();
        // Arrange: Create and save products with different categories
        Product product1 = new Product();
        product1.setTitle("Electronics Product 1");
        product1.setPrice(99.99f);
        product1.setQuantity(10);
        product1.setCategory(ProductCategory.ELECTRONICS);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setTitle("Clothing Product 1");
        product2.setPrice(49.99f);
        product2.setQuantity(20);
        product2.setCategory(ProductCategory.CLOTHING);
        productRepository.save(product2);

        Product product3 = new Product();
        product3.setTitle("Electronics Product 2");
        product3.setPrice(199.99f);
        product3.setQuantity(5);
        product3.setCategory(ProductCategory.ELECTRONICS);
        productRepository.save(product3);

        // Act: Call the method to retrieve products by category
        List<Product> electronicsProducts = productService.retrieveProductByCategory(ProductCategory.ELECTRONICS);
        List<Product> clothingProducts = productService.retrieveProductByCategory(ProductCategory.CLOTHING);

        // Assert: Verify that the retrieved products match the expected results
        assertNotNull(electronicsProducts, "The retrieved electronics product list should not be null");
        assertEquals(2, electronicsProducts.size(), "There should be 2 electronics products");
        assertTrue(electronicsProducts.stream().anyMatch(p -> p.getTitle().equals("Electronics Product 1")), "Should contain Electronics Product 1");
        assertTrue(electronicsProducts.stream().anyMatch(p -> p.getTitle().equals("Electronics Product 2")), "Should contain Electronics Product 2");

        assertNotNull(clothingProducts, "The retrieved clothing product list should not be null");
        assertEquals(1, clothingProducts.size(), "There should be 1 clothing product");
        assertTrue(clothingProducts.stream().anyMatch(p -> p.getTitle().equals("Clothing Product 1")), "Should contain Clothing Product 1");
    }
    @Test
    public void testDeleteProduct() {
        // Arrange: Create and save a new product
        Product product = new Product();
        product.setTitle("Test Product");
        product.setPrice(99.99f);
        product.setQuantity(10);
        product = productRepository.save(product);  // Save and get the saved object

        Long productId = product.getIdProduct(); // Get the ID of the saved product

        // Act: Call the method to delete the product
        productService.deleteProduct(productId);

        // Assert: Verify that the product no longer exists in the repository
        assertFalse(productRepository.findById(productId).isPresent(), "The product should be deleted and not present in the repository");
    }
    @Test
    public void testRetrieveProductStock() {
        
        // Arrange: Create a stock and save it
        Stock stock = new Stock();
        stock.setIdStock(1L); // Set a valid ID for the stock
        stockRepository.save(stock); // Save stock to the repository

        // Create products associated with the stock
        Product product1 = new Product();
        product1.setTitle("Product 1");
        product1.setPrice(10.99f);
        product1.setQuantity(5);
        product1.setStock(stock); // Associate with the created stock
        productRepository.save(product1); // Save the product

        Product product2 = new Product();
        product2.setTitle("Product 2");
        product2.setPrice(20.99f);
        product2.setQuantity(10);
        product2.setStock(stock); // Associate with the same stock
        productRepository.save(product2); // Save the second product

        // Act: Retrieve products by stock ID
        List<Product> productsInStock = productService.retreiveProductStock(stock.getIdStock());

        // Assert: Verify the retrieved products match what was saved
        assertNotNull(productsInStock);
        assertEquals(2, productsInStock.size(), "The size of the retrieved product list should match the number of saved products");
        assertTrue(productsInStock.stream().anyMatch(p -> p.getTitle().equals("Product 1")), "Product 1 should be in the retrieved list");
        assertTrue(productsInStock.stream().anyMatch(p -> p.getTitle().equals("Product 2")), "Product 2 should be in the retrieved list");
    }
    }




