package com.company.inventory.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Product; //Es el Product.java

public interface IProductDao extends CrudRepository< Product, Long>{

}
