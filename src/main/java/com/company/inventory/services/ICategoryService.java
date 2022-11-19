package com.company.inventory.services;

import org.springframework.http.ResponseEntity;

import com.company.inventory.response.CategoryResponseRest;

//does an HTTP an create a custom response
public interface ICategoryService {
	
	public ResponseEntity<CategoryResponseRest> search();

}
