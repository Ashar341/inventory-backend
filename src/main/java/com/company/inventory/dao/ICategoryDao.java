package com.company.inventory.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Category;

//Con esta interfaz you can access to the data, ctrl click to check what the class crud does
//Basically you can read, delete and analyze
public interface ICategoryDao extends CrudRepository<Category, Long>{

}
