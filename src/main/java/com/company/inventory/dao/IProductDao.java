package com.company.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Product; //Es el Product.java

//El metodo crud tiene todos los metodos de buscar, actualizar, etc.
public interface IProductDao extends CrudRepository< Product, Long>{

	//es para hacer un request SQL con comodin
	//Se usan los find by name de spring para mejorar la consulta
	@Query("select p from Product p where p.name like %?1%")
	List<Product> findByNameLike(String name);
		
	List<Product> findByNameContainingIgnoreCase(String name);
}
