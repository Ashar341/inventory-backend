package com.company.inventory.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity 
@Table(name="product")
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7461389651533509262L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private int price;
	private int account;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
	private Category category;
	
	/**De esta manera puedes convertir las fotos a 64 y evitar consumir 
	 * espacio de mas en un servidor, el length de 1000 es para 64
	 * El lob y basic son para ralentizar la carga y no tronar el servidor*/
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column ( name= "picture", length = 1000)
	private byte[] picture;
}
