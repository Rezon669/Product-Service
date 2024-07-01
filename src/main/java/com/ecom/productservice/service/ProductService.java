package com.ecom.productservice.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.productservice.exception.CustomException;
import com.ecom.productservice.model.Product;
import com.ecom.productservice.repository.ProductRepository;

@Service
public class ProductService {

	private static final Logger logger = LogManager.getLogger(ProductService.class);
	@Autowired
	ProductRepository productRepository;

	public String addProducts(Product product) throws CustomException {
		if (product.getProductname().isEmpty() || product.getCategory().isEmpty()
				|| product.getSearchkeyword().isEmpty() || product.getPrice() <= 0 || product.getQuantity() <= 0) {
			logger.warn("All the fields are mandatory fields");
			throw new CustomException("All the fields are mandatory fields");
		} else if (product.getCategory().equalsIgnoreCase("select one")) {
			throw new CustomException("Please select the category type");
		}

		productRepository.save(product);
		return "Product Details are added";

	}

	public List<Product> getProducts() throws CustomException {

		List<Product> list = productRepository.findAll();
		if (list.isEmpty()) {
			throw new CustomException("No Products are found");
		} else {
			return list;
		}
	}

	public List<Product> searchProduct(String searchkeyword) throws CustomException {
		List<Product> list = productRepository.searchProduct(searchkeyword);
		if (list.isEmpty()) {
			throw new CustomException("No Products are found with the given search keyword");
		} else {
			return list;
		}

	}

	public Product getProductById(Long id) throws CustomException {
		Product productDetails = productRepository.getProductDetails(id);

		return productDetails;

	}

	public void updateProduct(Product updatedProduct, Long id) {

		productRepository.updateProduct(updatedProduct.getProductname(), updatedProduct.getPrice(),
				updatedProduct.getQuantity(), updatedProduct.getCategory(), updatedProduct.getSearchkeyword(), id);

	}

	public void deleteProduct(Product existingProduct) {
		productRepository.delete(existingProduct);

	}

	public Product getProductbyId(Long id) throws CustomException {

		return productRepository.findById(id).orElseThrow(() -> new CustomException("Product not found"));

	}

}
