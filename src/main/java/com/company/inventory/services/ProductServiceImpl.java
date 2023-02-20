package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

@Service
public class ProductServiceImpl implements IProductService {

	//En esta ocasion se usa el constructor, mas recomendado en vez de @autowired
	
	private ICategoryDao categoryDao;
	private IProductDao productDao;
	
	public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}



	/*Resivir un valor de producto a traves de un valor id y deveulve un response entity*/
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId){
		
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			// search categorOptional<T> in the product object
			Optional<Category> category = categoryDao.findById(categoryId);
			
			//Si encuentra el producto
			if (category.isPresent()) {
				product.setCategory(category.get());
			} else {
				response.setMetadata("Respuesta nok", "-1", "Categoria encontrada asociada al producto");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			//guardado del producto
			Product productSaved = productDao.save(product);
			
			if (productSaved != null) {
				list.add(productSaved);
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta nok", "00", "Producto guardado");
			} else {
				response.setMetadata("Respuesta nok", "-1", "Producto no guardado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "500", "Error al guardar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return 	new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);

	}
}
