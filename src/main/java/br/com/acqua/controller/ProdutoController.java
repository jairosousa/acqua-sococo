package br.com.acqua.controller;

import br.com.acqua.entity.AvatarProd;
import br.com.acqua.entity.Produto;
import br.com.acqua.entity.enuns.Categoria;
import br.com.acqua.service.AvatarProdService;
import br.com.acqua.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	private static final String CADASTRO_VIEW = "produto/produto-cadastro";

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private AvatarProdService avatarService;

	@GetMapping("/novo")
	public ModelAndView novo(@ModelAttribute("avatar") AvatarProd avatar) {
		ModelAndView view = new ModelAndView(CADASTRO_VIEW);
		view.addObject(new Produto());
		return view;
	}

	@GetMapping
	public ModelAndView listar() {
		ModelAndView view = new ModelAndView("produto/produtos");
		List<Produto> produtos = produtoService.findAll();
		view.addObject("produtos", produtos);
		return view;
	}

	@PostMapping
	public String salvar(@Validated Produto produto, Errors erros, RedirectAttributes attributes,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		AvatarProd avatar = new AvatarProd();
		if (erros.hasErrors()) {
			return CADASTRO_VIEW;
		}
			avatar = avatarService.getAvatarByUpload(file);
			produto.setAvatar(avatar);

		try {
			produtoService.salvar(produto);
			attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
			return "redirect:/produtos";

		} catch (IllegalArgumentException e) {
			erros.rejectValue("dataCadastro", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}

	@PostMapping("/update")
	public String update(@Validated Produto produto, Errors erros, RedirectAttributes attributes,
						 @RequestParam(value = "file", required = false) MultipartFile file) {
		AvatarProd avatar = new AvatarProd();
		if (erros.hasErrors()) {
			return CADASTRO_VIEW;
		}
		if (!file.isEmpty()) {
			avatar = avatarService.getAvatarByUpload(file);
			avatar.setId(produto.avatar.getId());
			produto.setAvatar(avatar);
		}
		try {
			produtoService.update(produto);
			attributes.addFlashAttribute("mensagem", "Produto atualizado com sucesso!");
			return "redirect:/produtos";

		} catch (IllegalArgumentException e) {
			erros.rejectValue("dataCadastro", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}
	
	@GetMapping("/detalhes/{id}")
	public ModelAndView perfil(@PathVariable("id") Produto produto){
		ModelAndView view = new ModelAndView();
		view.setViewName("produto/produto-detalhes");
		view.addObject("produto",produto);
		return view;
	}

	@RequestMapping(value = {"/{id}", "/"}, method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView editar(@PathVariable("id") Optional<Long> id, @ModelAttribute("produto") Produto produto) {
		ModelAndView view = new ModelAndView();

		if (id.isPresent()) {
					
			produto = produtoService.findById(id.get());
			view.addObject("produto", produto);
			view.setViewName("produto/produto-atualizar");
			
			return view;
		}
		
		produtoService.updateNomeAndDescricaoAndCodigoBarra(produto);
		
		//view.setViewName("redirect:/produtos/detalhes/" + produto.getId());
		view.setViewName("redirect:/produtos");
		return view;
		
	}
	
	@DeleteMapping("{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attributes) {
		produtoService.delete(id);
		attributes.addFlashAttribute("mensagem", "Produto excluido com sucesso!");
		return "redirect:/produto";
	}

	@ModelAttribute("categorias")
	public List<Categoria> todoStatusTitulo() {
		return Arrays.asList(Categoria.values());
	}
	

}
