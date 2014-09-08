package com.ceteva.mosaic;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProductProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductProvider.
 */
public class ProductProvider implements IProductProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProductProvider#getName()
	 */
	public String getName() {
		return "com.ceteva.mosaic.ProductProvider";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProductProvider#getProducts()
	 */
	public IProduct[] getProducts() {
		IProduct[] products = new IProduct[1];
		// products[0] = new Product();
		return products;
	}
}
