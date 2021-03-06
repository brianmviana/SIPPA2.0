package br.ufc.sippa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.ufc.sippa.service.UsuarioService;

@Controller
public class RoutingController {
	
	@Autowired
	UsuarioService service;
	
//	sessão, ta com bug
//	@RequestMapping(value = "/login",  method = { RequestMethod.GET, RequestMethod.POST })
//	public String login(HttpServletRequest request, HttpSession session){
//		String login = request.getUserPrincipal().getName();
//		//Buscar usuario pelo login
//		Usuario usr = userSvc.findByLogin(login);
//		if (usr == null)
//			//Tratar se tiver nulo
//
//		session.setAttribute("user_logged", usr);
//		return "/login";
//	}

	
	@RequestMapping(value = {"/","/login"},  method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(){
		ModelAndView model = new ModelAndView("/login");
		model.addObject("usuarios", service.findByPapel("administrador"));
		return model;		
	}
	
	
	@RequestMapping(value={"/home"})
	public String home(ModelAndView model){
		return "tabs/index";
	}
	
	@RequestMapping(value={"/administrador"})
	public String administrador(ModelAndView model){
		return "tabs/index";
	}
	
	@RequestMapping(value={"/professor"})
	public String professor(ModelAndView model){
		return "tabs/indexProf";
	}
	
	@RequestMapping(value={"/aluno"})
	public String aluno(ModelAndView model){
		return "tabs/indexAluno";
	}
	
	@RequestMapping(path="/usuario")
	public String usuario(){
		return "usuario/";
	}

	
	@RequestMapping(path="/disciplina")
	public String disciplinas(){
		return "disciplina";
	}
	
	@RequestMapping(path="/turma")
	public String turmas(){
		return "turma";
	}

	
}
