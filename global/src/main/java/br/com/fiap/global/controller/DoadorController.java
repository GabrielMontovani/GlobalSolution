package br.com.fiap.global.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import br.com.fiap.global.model.Doador;
import br.com.fiap.global.model.User;
import br.com.fiap.global.repository.DoadorRepository;
import br.com.fiap.global.exception.DoadorNotFoundException;


@Controller
@RequestMapping("/doador")
public class DoadorController {
	
	@Autowired
	private DoadorRepository repository;
	
	@Autowired
	private MessageSource message;
	
	@GetMapping
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("doadores");
		List<Doador> doadores = repository.findAll();
		modelAndView.addObject("doadores", doadores);
		return modelAndView;
	}
	
	@PostMapping
	public String save(@Valid Doador doador, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) return "doador-form";
		repository.save(doador);
		redirect.addFlashAttribute("message", message.getMessage("doador.new.success", null, LocaleContextHolder.getLocale()));
		return "redirect:/doador";
	}
	
	@RequestMapping("new")
	public String create(Doador doador) {
		return "doador-form";
	}
	
	@GetMapping("/hold/{id}")
	public String hold(@PathVariable Long id, Authentication auth) {
		Optional<Doador> optional = repository.findById(id);
		
		if(optional.isEmpty())
			throw new DoadorNotFoundException("Doador não encontrada"); 
		
		Doador doador = optional.get();
		
		if(doador.getUser() != null) 
			throw new NotAllowedException("Doador já atribuído no sistema");
		
		
		User user = (User) auth.getPrincipal();
		doador.setUser(user);
		
		repository.save(doador);
		
		return "redirect:/doador";
	}
	
	@GetMapping("/release/{id}")
	public String release(@PathVariable Long id, Authentication auth) {
		Optional<Doador> optional = repository.findById(id);
		
		if(optional.isEmpty())
			throw new DoadorNotFoundException("Doador não encontrada"); 
		
		Doador doador = optional.get();
		User user = (User) auth.getPrincipal();
		
		if(!doador.getUser().equals(user)) 
			throw new NotAllowedException("Doador está com outro usuário");
		
		doador.setUser(null);
		
		repository.save(doador);
		
		return "redirect:/doador";
	}
	@RequestMapping("/delete/{id}")
	public String deleteDoador(@PathVariable String id) {
		repository.deleteById(Long.parseLong(id));
		return "redirect:/doador";
	}
	
	@RequestMapping("/update/{id}")
	public String update(@PathVariable Long id, @Valid Doador doador, BindingResult result, Model model) {
		Doador teste = repository.findById(id).orElse(null);
		if (result.hasErrors()) {
			
	        doador.setId(id);
	        doador.setTitle(teste.getTitle());
	        doador.setDescription(teste.getDescription());
	        doador.setUser(teste.getUser());
	        doador.setUsuario(teste.getUsuario());
	        doador.setCpf(teste.getCpf());
	        return "doador-update-form";
	    }
		repository.save(doador);
		return "redirect:/doador";
	}
	
	
	
	
	
	
	
	
}
