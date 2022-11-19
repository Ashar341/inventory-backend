package com.company.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.services.ICategoryService;

//Esta es una clase controlladora, que se connectara primero
// al api/v1 y luego se crea un metodo que busca todas las categorias
//con el metodo search y se conecta a categoryresponse
//Todo esto para realizar un servicio, para los demas servicios solo les agregas
@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {
	
	@Autowired
	private ICategoryService service;
	
	/**
	 * Get all the categories
	 * @return
	 */
	
	@GetMapping("/categories")
	public ResponseEntity<CategoryResponseRest> searchCategory(){
		
		ResponseEntity<CategoryResponseRest> response = service.search();
		return response;
	}
	
	/**
	 * Get categories by id
	 * @return
	 */
	//De esta manera pasamos el id a la categoria
	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> searchCategoryById(@PathVariable Long id){
		
		ResponseEntity<CategoryResponseRest> response = service.searchById(id);
		return response;
	}
	

}
