/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import application.product.Product;
import application.storage.ProductContainer;

/**
 *
 * @author Stephen Kitto
 */
public class Mapping {
	
	private ProductContainer container;
	private Product product;

	public Mapping(ProductContainer container, Product product) {
		this.container = container;
		this.product = product;
	}

	public ProductContainer getContainer() {
		return container;
	}

	public void setContainer(ProductContainer container) {
		this.container = container;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
