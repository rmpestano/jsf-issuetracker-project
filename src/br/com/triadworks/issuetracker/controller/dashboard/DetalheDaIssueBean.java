package br.com.triadworks.issuetracker.controller.dashboard;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;

import br.com.triadworks.issuetracker.controller.UsuarioWeb;
import br.com.triadworks.issuetracker.controller.util.FacesUtils;
import br.com.triadworks.issuetracker.dao.IssueDao;
import br.com.triadworks.issuetracker.model.Comentario;
import br.com.triadworks.issuetracker.model.Issue;
import br.com.triadworks.issuetracker.qualifier.UsuarioLogado;

@Named
@ViewAccessScoped
public class DetalheDaIssueBean {

	private Long id;
	private Issue issue;
	private Comentario comentario = new Comentario();
	
	private IssueDao issueDao;
	private UsuarioWeb usuarioWeb;
	private FacesUtils facesUtils;
	
	@Inject
	public DetalheDaIssueBean(IssueDao issueDao, @UsuarioLogado UsuarioWeb usuarioWeb, FacesUtils facesUtils) {
		this.issueDao = issueDao;
		this.usuarioWeb = usuarioWeb;
		this.facesUtils = facesUtils;
	}

	/**
	 * Maneira simples de tratar requisições <code>GET</code> sem aproveitar os
	 * recursos do componente <code>f:viewParam</code>.
	 */
	public void init() {
		if (id == null) {
			String mensagem = "ID da issue inválida, por favor use um link de dentro do sistema.";
			facesUtils.adicionaMensagemDeErro(mensagem);
			return;
		}
		
		issue = issueDao.carrega(id);
		if (issue == null) {
			String mensagem = "Issue com ID #" + id + " não encontrada.";
			facesUtils.adicionaMensagemDeErro(mensagem);
		}
	}
	
	/**
	 * Comenta a issue em questão.
	 */
	public void comentaIssue() {
		comentario.setAutor(usuarioWeb.getUsuario());
		issueDao.comenta(issue.getId(), comentario);
		limpa();
	}
	
	/**
	 * Comenta e Fecha a issue em questão.
	 */
	public void fechaIssue() {
		comentario.setAutor(usuarioWeb.getUsuario());
		issueDao.fecha(issue.getId(), comentario);
		limpa();
	}
	
	/**
	 * Limpa comentário e recarrega issue, pois a issue estava detached.
	 */
	private void limpa() {
		issue = issueDao.carrega(issue.getId());
		comentario = new Comentario();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Issue getIssue() {
		return issue;
	}
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	public Comentario getComentario() {
		return comentario;
	}
	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}
	
}
