package br.com.acqua.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.acqua.entity.Usuario;
import br.com.acqua.entity.paginator.Pager;
import br.com.acqua.service.UsuarioService;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;
	private static final int[] PAGE_SIZES = { 5, 10, 20 };

	private static final String CADASTRO_VIEW = "usuario/usuario-cadastro";
	
	String naoEditarSenha = "";
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/novo")
	public ModelAndView novo(){
		Usuario usuario = new Usuario();
	
		ModelAndView view = new ModelAndView(CADASTRO_VIEW);
		view.addObject("usuario", usuario);
		return view;
	}
	
	@GetMapping
	public ModelAndView showPersonsPage(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) {
		
		ModelAndView modelAndView = new ModelAndView("usuario/usuarios");

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		Page<Usuario> usuarios = usuarioService.findByPagination(evalPage, evalPageSize);
		Pager pager = new Pager(usuarios.getTotalPages(), usuarios.getNumber(), BUTTONS_TO_SHOW);

		modelAndView.addObject("usuarios", usuarios);
		modelAndView.addObject("selectedPageSize", evalPageSize);
		modelAndView.addObject("pageSizes", PAGE_SIZES);
		modelAndView.addObject("pager", pager);
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST )
	public String salvar(@Validated Usuario usuario, Errors errors, RedirectAttributes attributes  ){

		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}

		System.out.println("naoEditarSenha: " + naoEditarSenha);
		if (naoEditarSenha.equals("")) {
			try {
				System.out.println("NOVO USUARIO");
				usuarioService.salvar(usuario);
				attributes.addFlashAttribute("mensagem", "Operador cadastrado com sucesso!");
				return "redirect:/usuarios";
			} catch (IllegalArgumentException e) {
				attributes.addFlashAttribute("mensagem", "Desculpe, mas algo deu errado.");
				return CADASTRO_VIEW;
			}
		} else {
			try {
				System.out.println("Atualizando USUARIO");
				usuario.setPassword(naoEditarSenha);
				usuarioService.salvar(usuario);
				attributes.addFlashAttribute("mensagem", "Operador cadastrado com sucesso!");
				naoEditarSenha = "";
				return "redirect:/usuarios";
			} catch (IllegalArgumentException e) {
				attributes.addFlashAttribute("mensagem", "Desculpe, mas algo deu errado.");
				return CADASTRO_VIEW;
			}

		}
	}
	
	
	@RequestMapping(value = "/atualizar-senha", method = RequestMethod.POST)
	public String atualizarSenha(@RequestParam("password") String password, @Validated Usuario usuario, Errors errors, RedirectAttributes attributes  ){
		
//		if(errors.hasErrors()){
//			return CADASTRO_VIEW;
//		}
//		
		System.out.println("senha "+password);
		String userName = "";
		userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuarioTemp = new Usuario();
		usuarioTemp.setUsername(userName);
		usuarioTemp.setPassword(password);
		try{
			usuarioService.atualizarSenha(usuarioTemp);
			attributes.addFlashAttribute("mensagem", "Senha atualizada com sucesso!");
			return "redirect:/index";
		}catch (IllegalArgumentException e) {
			attributes.addFlashAttribute("mensagem", "Desculpe, mas algo deu errado.");
			return "";
		}
	}

	
	@GetMapping(value = {"{id}"})
	public ModelAndView editar(@PathVariable("id") Optional<Long> id, @ModelAttribute("usuario") Usuario usuario) {
		
		
		
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		if(id.isPresent()){
			usuario = usuarioService.findById(id.get());
			usuario.setPerfil(usuario.getPermissoes().get(0).getNome());
			mv.addObject("usuario", usuario);
			
			naoEditarSenha = usuario.getPassword();
			System.out.println(">> Pass " + usuario.getPassword());
			System.out.println(">> nome " + usuario.getNome());
			System.out.println(">> sobrenome " + usuario.getSobrenome());
		}
		return mv;
	}
	
	@DeleteMapping(value="{id}")
	private String excluir(@PathVariable Long id, RedirectAttributes attributes) {
		
		usuarioService.delete(id);
		attributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");	
		return "redirect:/usuarios";
	}
	
}
