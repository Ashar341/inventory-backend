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
import com.company.inventory.util.Util;

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



	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		
		//Se copia el mismo codigo anterior
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			// Se busca producto por ID
			Optional<Product> product= productDao.findById(id);
			
			//Si encuentra el producto, obeneter la imagen en b64 y descomprimir
			if (product.isPresent()) {
				
				byte [] imageDescompressed = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(imageDescompressed);
				list.add(product.get());
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta ok", "00", "Producto encontrado");
				
			} else {
				response.setMetadata("Respuesta nok", "-1", "Producto no encontrado asociada al producto");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "500", "Error al guardar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return 	new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}



	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		//Se copia el mismo codigo anterior
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();		
		try {
			// Se busca producto por name similar
			listAux = productDao.findByNameContainingIgnoreCase(name);
					
			//Si encuentra el producto, obeneter la imagen en b64 y descomprimir
			if (listAux.size() > 0) {
				//Para ver si encuentra un nombre similar y pasarlo a la busqueda normal
				listAux.stream().forEach((p) ->{
					byte [] imageDescompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(imageDescompressed);
					list.add(p);
				});
				
				//La respuesta sera con la lista normal
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta ok", "00", "Productos encontrados");
						
			} else {
				response.setMetadata("Respuesta nok", "-1", "Productos no encontrados ");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
					
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "400", "Error al buscar producto por nombre");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		return 	new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}



	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		//Se copia el mismo codigo anterior y se adapta para eliminar
				ProductResponseRest response = new ProductResponseRest();
				
				try {
					// eliminar producto por ID
					productDao.deleteById(id);
					response.setMetadata("Respuesta ok", "00", "Producto eliminado");
					
					//Si encuentra el producto, obeneter la imagen en b64 y descomprimir
					
				} catch (Exception e) {
					e.getStackTrace();
					response.setMetadata("Respuesta nok", "500", "Error al eliminar producto");
					return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				return 	new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}



	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<ProductResponseRest> search() {
		//Se copia el mismo codigo anterior
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();		
		try {
			// Se busca producto 
			listAux = (List<Product>) productDao.findAll();
					
			//Si encuentra el producto, obeneter la imagen en b64 y descomprimir
			if (listAux.size() > 0) {
				//Para ver si encuentra un nombre similar y pasarlo a la busqueda normal
				listAux.stream().forEach((p) ->{
					byte [] imageDescompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(imageDescompressed);
					list.add(p);
				});
				//La respuesta sera con la lista normal
				response.getProduct().setProducts(list);
				response.setMetadata("Respuesta ok", "00", "Productos encontrados");	
			} else {
				response.setMetadata("Respuesta nok", "-1", "Productos no encontrados ");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "400", "Error al buscar productos");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		return 	new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
		
	}



	@Override
	@Transactional
	
	//Transactional es el commit
	public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {
		
		//Se copia el contenido una vez mas
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
			
			//actualizar el producto
			//se busca el producto a actualizar
			Optional <Product> productSearch = productDao.findById(id);
			
			if (productSearch.isPresent()) {
				// se actiañoza eñ producto
				productSearch.get().setAccount(product.getAccount());
				productSearch.get().setCategory(product.getCategory());
				productSearch.get().setName(product.getName());
				productSearch.get().setPicture(product.getPicture());
				productSearch.get().setPrice(product.getPrice());
				
				//Se guardan los productos con los nuevos valores
				Product productToSave = productDao.save(productSearch.get());
				
				if(productToSave != null) {
					list.add(productToSave);
					response.getProduct().setProducts(list);
					response.setMetadata("Respuesta nok", "00", "Producto actualizado");
				} else {
					response.setMetadata("Respuesta nok", "-1", "Producto no actualizado");
					return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
				}
				
			} else {
				response.setMetadata("Respuesta nok", "-1", "Producto no actualizado 2");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "500", "Error al guardar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return 	new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);

	}
}
