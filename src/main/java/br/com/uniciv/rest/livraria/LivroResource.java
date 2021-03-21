package br.com.uniciv.rest.livraria;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

@Path("livro")
public class LivroResource {
	
	private LivroRepository livroRepository = LivroRepository.getInstance();
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Livros getLivros(){
		Livros livros = new Livros();
		livros.setLivros(livroRepository.getLivros());
		return livros;
	}
	
	@GET
	@Path("/{isbn}")
	public ItemBusca getLivroByIsbn(@PathParam("isbn") String isbn) {
		
		try {
			Livro livro = livroRepository.getLivroByIsbn(isbn);
			ItemBusca item = new ItemBusca();
			item.setLivro(livro);
			
			Link link = Link.fromUri("/carrinho/" + livro.getId())
					.rel("carrinho")
					.type("POST").build();
			
			item.addLink(link);
			
			return item;
		}catch (LivroNaoEncontradoException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}		
		
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response adicionarLivro(Livro livro) {
		
		try {
			livroRepository.adicionarLivro(livro);
		}catch (LivroExistenteException e) {
			throw new WebApplicationException(Status.CONFLICT);
		}		
		
		URI uriLocation = UriBuilder.fromPath("livro/{isbn}").build(livro.getIsbn());
		
		return Response.created(uriLocation).entity(livro).build();
	}
	
	@PUT
	@Path("/{isbn}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response atualizarLivro(@PathParam("isbn") String isbn, Livro livro) {
			
		Livro livroDoEstoque = livroRepository.getLivroByIsbn(isbn);
		
		try {
			
			livroDoEstoque.setAutores(livro.getAutores());
			livroDoEstoque.setGenero(livro.getGenero());
			livroDoEstoque.setPreco(livro.getPreco());
			livroDoEstoque.setTitulo(livro.getTitulo());
			livroDoEstoque.setIsbn(livro.getIsbn());
			
			livroRepository.atualizarLivro(livroDoEstoque);
			
		}catch (LivroNaoEncontradoException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}		
		
		return Response.ok().entity(livroDoEstoque).build();
	}
	
	@DELETE
	@Path("/{id}")
	public void removerLivro(@PathParam("id") Long id) {
		try {
			livroRepository.removerLivro(id);
		}catch (LivroNaoEncontradoException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	
}
