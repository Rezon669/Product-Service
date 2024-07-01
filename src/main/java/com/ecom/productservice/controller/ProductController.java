package com.ecom.productservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.productservice.exception.CustomException;
import com.ecom.productservice.model.Product;
import com.ecom.productservice.service.ProductService;

@RestController
@RequestMapping("/easybuy/product")
public class ProductController {

	private static final Logger logger = LogManager.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@GetMapping("/admin/addproducts")
	public String addProduct() {

		return "product";
	}

	@GetMapping("/public/homepage")
	public String home() {

		return "welcome";
	}

	@PostMapping("/admin/addproduct")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addProduct(Product product, Model model) {
		try {
			productService.addProducts(product);
			logger.info("Product details are added");
			// return "thankyou";
			return ResponseEntity.accepted().body("Product details added Successfully...!");
		} catch (CustomException e) {

			model.addAttribute("errorMessage", e.getMessage());
			logger.error(e);

			// return "product";

			return ResponseEntity.badRequest().body(e + " ");

		}

	}

	@GetMapping("/public/getproducts")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getProducts(Model model) {
		try {
			List<Product> products = productService.getProducts();

			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (CustomException e) {
			model.addAttribute("errorMessage", e.getMessage());
			logger.error(e);

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/public/searchproduct")
	public String searchProduct() {

		return "searchproduct";

	}

	@GetMapping("/public/searchproducts")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam("searchkeyword") String searchkeyword)
			throws CustomException {
		try {
			System.out.println(searchkeyword);
			List<Product> products = productService.searchProduct(searchkeyword);

			if (products.isEmpty()) {
				return new ResponseEntity<>(products, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(products, HttpStatus.OK);
			}
		} catch (CustomException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // You can return a custom error
																						// response if needed
		}

	}

	@PutMapping("/admin/updateproduct/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct)
			throws CustomException {

		try {
			Product existingProduct = productService.getProductById(id);

			if (existingProduct == null) {
				Map<String, String> response = new HashMap<>();
				response.put("message", "Product not found");
				// return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			} else {

				productService.updateProduct(updatedProduct, id);
				Map<String, String> response = new HashMap<>();
				response.put("message", "Product details updated successfully");

				// return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
		} catch (CustomException e) {
			e.printStackTrace();
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "An error occurred while updating the product");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@DeleteMapping("/admin/deleteproduct/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) throws CustomException {

		Product existingProduct = productService.getProductById(id);

		if (existingProduct == null) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Product not found");
			// return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} else {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Product details deleted successfully");

			productService.deleteProduct(existingProduct);

			// return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

	}
	
	@GetMapping("/public/{id}")
	public boolean findById(Long productId) {
		Product existingProduct = null;
		try {
			existingProduct = productService.getProductById(productId);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(existingProduct==null) {
			return false;
		}
		else {
			return true;
		}
	}
}