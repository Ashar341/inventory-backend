package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;

@Service
public class CategoryServiceImpl implements ICategoryService{

	//this object inject from spring to be ready use
	@Autowired
	private ICategoryDao categoryDao;
	
	@Override
	@Transactional(readOnly = true) //If DB not work it would be a transaction
	public ResponseEntity<CategoryResponseRest> search() {
		
		// Implementar metodo search
		CategoryResponseRest response = new CategoryResponseRest();
		
		try {
			List<Category> category = (List<Category>) categoryDao.findAll();
			
			//Ademas de crear el metodo setCategor en category response
			response.getCategoryResponse().setCategory(category);
			response.setMetadata("Respuesta Ok", "00" , "Respuesta exitosa" );
			
			
		} catch (Exception e) {

			
			response.setMetadata("Respuesta Not Ok", "-1" , "Error al consultar" );
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
			
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			//Para devolver objeto optional de un ID
			Optional<Category> category = categoryDao.findById(id);
			if(category.isPresent()) {
				list.add(category.get());
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta Ok", "00" , "Categoria encontrada" );

			}else {
				response.setMetadata("Respuesta Not Ok", "-1" , "Categoria no encontrada" );
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			
			
		} catch (Exception e) {

			
			response.setMetadata("Respuesta Not Ok", "-1" , "Error al consultar por ID" );
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
			
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	/** 
	 * Con esta cochinada de codigo deberia se capaz de guardar lo que se envie
	 * En formato json a traves de la implementacion del get and set
	 * Pero por alguna razon no esta obteniendo la categoria y obtiene un valor empty
	 * lo cual deberia ser igual a null pero quien sabe
	 */
	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			
			Category categorySaved = categoryDao.save(category);
			
			if (categorySaved != null) {
				list.add(categorySaved);
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Respuesta ok", "00", "Categoria guardada");
			} else {
				response.setMetadata("Respuesta nok", "-1", "Categoria no guardada");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
			
		} catch (Exception e) {
			
			response.setMetadata("Respuesta nok", "-1", "Error al grabar categoria");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
			
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}
}
