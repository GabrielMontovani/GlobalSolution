package br.com.fiap.global.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Doador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "{comerciante.title.blank}")
	private String title;
	
	@Size(min=15, message = "A descrição deve ter pelo menos 15 caracteres")
	private String description;
	
	@NotBlank @Email
	private String usuario;
	
	private String cpf;
	
	private float price;
	
	@ManyToOne
	private User user;
	

}
