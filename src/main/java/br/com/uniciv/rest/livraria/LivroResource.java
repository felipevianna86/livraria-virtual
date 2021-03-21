package br.com.uniciv.rest.livraria;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

@Path("livro")
public class LivroResource {
	
	private LivroRepository livroRepository = new LivroRepository();
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Livros getLivros(){
		Livros livros = new Livros();
		livros.setLivros(livroRepository.getLivros());
		return livros;
	}
	
	@GET
	@Path("/{isbn}")
	public Livro getLivroByIsbn(@PathParam("isbn") String isbn) {
		
		try {
			return livroRepository.getLivroByIsbn(isbn);
		}catch (LivroNaoEncontradoException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}		
		
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response adicionarLivro(Livro livro) {
		livroRepository.adicionarLivro(livro);
		
		URI uriLocation = UriBuilder.fromPath("livro/{isbn}").build(livro.getIsbn());
		
		return Response.created(uriLocation).entity(livro).build();
	}
	
	
}
