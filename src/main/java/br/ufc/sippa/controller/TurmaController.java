package br.ufc.sippa.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.sippa.model.Turma;
import br.ufc.sippa.model.Usuario;
import br.ufc.sippa.service.DisciplinaService;
import br.ufc.sippa.service.TurmaService;
import br.ufc.sippa.service.UsuarioService;

@Controller
@RequestMapping(path="/turma/")
public class TurmaController {
	
	@Autowired
	TurmaService service;
	
	@Autowired
	UsuarioService serviceUser;
	@Autowired
	DisciplinaService serviceDisc;
	
	@RequestMapping(path="/listar")
	public ModelAndView index(){
		ModelAndView model = new ModelAndView("listarTurma");
		List<Turma> turmas = service.findAll();
		
		model.addObject("turmas",turmas);
		return model;
	}
	
	@RequestMapping(path="/cadastrar")
	public String cadastrarTurma(Model model){
		model.addAttribute("disciplinas", serviceDisc.findAll());
		model.addAttribute("professores", serviceUser.findByPapel("professor"));
		return "cadastrarTurma";
	}
	
	
	@RequestMapping(path="/cadastrar", method=RequestMethod.POST)
	public String alocar(Turma turma, BindingResult result, RedirectAttributes attributes){
		if (result.hasErrors()){
			attributes.addAttribute("error",result.getAllErrors().get(0));
			return "redirect:/disciplina/cadastrar";
		}
		service.save(turma);
		attributes.addFlashAttribute("mensagemSucesso", "Turma cadastrado com sucesso!");
		return "redirect:/turma/listar";
	}	
	
	@RequestMapping(path="/{id}")
	public ModelAndView locacoes(@PathVariable("id") Integer id){
		ModelAndView model = new ModelAndView("alocarDesalocar");
		Turma turma = service.findOne(id);
		List<Usuario> alunosAll = serviceUser.findByPapel("aluno");
		List<Usuario> alunosNotAloc = null;
		for (Usuario aluno : alunosAll) {
			if(!turma.getAlunos().contains(aluno)){
				if(alunosNotAloc == null){
					alunosNotAloc = new ArrayList<Usuario>();
				}
				alunosNotAloc.add(aluno);
			}
		}
		model.addObject("alunosAll", alunosNotAloc);
		model.addObject("turma", turma);
		return model;
	}	
	
	@RequestMapping(path="/{idTurma}/alocarAluno/{idAluno}", method=RequestMethod.GET)
	public String alocarAluno(@PathVariable("idTurma") Integer idTurma, 
			@PathVariable("idAluno") Long idAluno){
		service.alocarAlunos(idTurma, idAluno);
		
		return "redirect:/turma/"+idTurma;
	}
	
	@RequestMapping(path="/{idTurma}/desalocarAluno/{idAluno}", method=RequestMethod.GET)
	public String desalocarAluno(@PathVariable("idTurma") Integer idTurma, 
			@PathVariable("idAluno") Long idAluno){
		
		service.desalocarAlunos(idTurma, idAluno);
		
		return "redirect:/turma/"+idTurma;
	}
	
	@RequestMapping(path="/editar/{id}", method=RequestMethod.GET)
	public 	String editar(@PathVariable("id") Integer id, Model model){
		model.addAttribute("current", service.findOne(id));
		model.addAttribute("disciplinas", serviceDisc.findAll());
		model.addAttribute("professores", serviceUser.findByPapel("professor"));
		return "editarTurma";
	}
	
	@RequestMapping(path={"/editar/{id}"}, method=RequestMethod.POST)
	public String editar_post(@PathVariable("id") Integer id, Turma turma, BindingResult result, RedirectAttributes attributes){
		if (result.hasErrors()){
			attributes.addAttribute("erro",result.getAllErrors().get(0));
			return "redirect:/turma/editar/"+id;
		}
		turma.setId(id);
		service.save(turma);
		attributes.addFlashAttribute("mensagemSucesso", "Disciplina editada com sucesso!");
		return "redirect:/turma/listar";

	}
	
	@RequestMapping(path="/remover/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes attributes){
		service.delete(id);
		if(service.findOne(id)==null){
			attributes.addFlashAttribute("mensagemSucesso", "Usuário removido com sucesso!");
		}
		return "redirect:/turma/listar";
	}
	
	
//	@RequestMapping(path="/aluno")
//	public String MostrarTurmas(Model model){
//		model.addAttribute("turma", service.findAll());
//		model.addAttribute("professores", serviceUser.getUsuarioPorPapel("professor"));
//		return "cadastrarTurma";
//	}
//	
	
}
