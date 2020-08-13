package br.com.acqua.controller;

import br.com.acqua.entity.AvatarProd;
import br.com.acqua.entity.LayoutProd;
import br.com.acqua.entity.Produto;
import br.com.acqua.entity.enuns.Categoria;
import br.com.acqua.service.AvatarProdService;
import br.com.acqua.service.ProdutoService;
import br.com.acqua.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
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

    @Autowired
    private StorageService storageService;

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
                         @RequestParam(value = "fileAvt", required = false) MultipartFile fileAvt,
                         @RequestParam(value = "fileLayout", required = false) MultipartFile fileLayout) {

        if (erros.hasErrors()) {
            return CADASTRO_VIEW;
        }

        if (!fileAvt.isEmpty()) {
            AvatarProd avatar = avatarService.getAvatarByUpload(fileAvt);
            produto.setAvatar(avatar);
        }

        if (!fileLayout.isEmpty()) {
            LayoutProd layout = storageService.store(fileLayout);
            produto.setLayout(layout);
        }

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
                         @RequestParam(value = "fileAvt", required = false) MultipartFile fileAvt,
                         @RequestParam(value = "fileLayout", required = false) MultipartFile fileLayout) {
        if (erros.hasErrors()) {
            return CADASTRO_VIEW;
        }
        if (!fileAvt.isEmpty()) {
            AvatarProd avatar = avatarService.getAvatarByUpload(fileAvt);
            avatar.setId(produto.getAvatar().getId());
            produto.setAvatar(avatar);
        }

        if (!fileLayout.isEmpty()) {
            LayoutProd layout = storageService.store(fileLayout);
            if (produto.getLayout().getId() != null) layout.setId(produto.getLayout().getId());
            if (produto.getLayout().getFilename().equals("")) produto.getLayout().setFilename(null);
            if (produto.getLayout().getFilename() != null) {
                storageService.delete(produto.getLayout().getFilename());
            }

            produto.setLayout(layout);
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
    public ModelAndView perfil(@PathVariable("id") Produto produto) {
        ModelAndView view = new ModelAndView();
        view.setViewName("produto/produto-detalhes");
        view.addObject("produto", produto);
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

    private LayoutProd createFileLayout(MultipartFile files) {
        LayoutProd layout = storageService.store(files);
        Path path = storageService.load(layout.getFilename());
        String url = MvcUriComponentsBuilder.fromMethodName(ProdutoController.class, "serveFile",
                path.getFileName().toString()).build().toUri().toString();
        layout.setUrl(url);

        return layout;
    }

}
