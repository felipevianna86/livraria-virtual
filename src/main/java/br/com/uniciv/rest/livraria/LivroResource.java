package br.com.uniciv.rest.livraria;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("livro")
public class LivroResource {
	
	private LivroRepository livroRepository = new LivroRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Livros getLivros(){
		Livros livros = new Livros();
		livros.setLivros(livroRepository.getLivros());
		return livros;
	}
}
