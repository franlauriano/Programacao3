package prefeitura.siab.apresentacao;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import prefeitura.siab.controller.BusinessException;
import prefeitura.siab.controller.UsuarioController;
import prefeitura.siab.tabela.Usuario;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SearchUsuario {

	//ATRIBUTOS
	private @Autowired UsuarioController controller;
	private Usuario usuario;
	private List<Usuario> result;
	private Usuario options;
	private String senha1;
	private String senha2;

	//PROPRIEDADES
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<Usuario> getResult() {
		return result;
	}
	public void setResult(List<Usuario> result) {
		this.result = result;
	}
	public Usuario getOptions() {
		return options;
	}
	public void setOptions(Usuario options) {
		this.options = options;
	}
	public String getSenha1() {
		return senha1;
	}
	public void setSenha1(String senha1) {
		this.senha1 = senha1;
	}
	public String getSenha2() {
		return senha2;
	}
	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}
	
	
	//CONSTRUTOR
	public SearchUsuario() {
		reset();
	}
	
	

	//MÉTODOS
	public String search(){
		result = controller.searchListUsuario(options);
		return null;
	}
	
	public String update(Usuario usuario){
		Usuario auxiliar = new Usuario();
		auxiliar.setMatricula(usuario.getMatricula());
		auxiliar.setTipo(usuario.getTipo());
		this.usuario = auxiliar;
		return "updateUsuario";
	}
	
	public void reset() {
		options = new Usuario();
		result = null;
	}
	
	public String confirmUpdate(){
		FacesMessage message = new FacesMessage();
		if(senha1.equals(senha2)){
			usuario.setSenha(senha1);
			try{
				controller.updateUsuario(usuario);
				reset();
				message.setSummary("Usuario atualizado com Sucesso!");
				message.setSeverity(FacesMessage.SEVERITY_INFO);
			}catch(BusinessException e){
				message.setSummary(e.getMessage());
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
			}
		}else{
			senha1 = null;
			senha2 = null;
			message.setSummary("As senhas informadas estão incorretas");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, message);
		return null;
	}
	
	
	public String confirmDeletion(Usuario usuario) throws BusinessException{
		controller.deleteUsuario(usuario);
		options = new Usuario();	
		boolean achou = false;
		for(Usuario usu: result){
			if(usu.getMatricula().equals(usuario.getMatricula())){
				achou = true;
			}
		}
		if(achou){
			result.remove(usuario);
		}
		FacesMessage message = new FacesMessage();
		message.setSummary("Usuario Deletado com Sucesso!");
		message.setSeverity(FacesMessage.SEVERITY_INFO);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, message);
		return null;
	}
	
	public String back(){
		reset();
		return "searchRaca";
	}
	
}