package br.com.uniciv.rest.livraria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LivroRepository {
	
	private Map<Long, Livro> livros = new HashMap<>();
	
	public LivroRepository() {
		Livro livro1 = new Livro(1L,"Livro A", "ISBN-123", "GENERO A", 19.99, "Autor 1");
		
		livros.put(livro1.getId(), livro1);
	}
	
	public List<Livro> getLivros(){
		return new ArrayList<Livro>(livros.values());
	}
	
	public Livro getLivroByIsbn(String isbn) {
		Optional<Livro> livro = livros.values().stream().filter(l -> l.getIsbn().equals(isbn)).findAny();
		
		if(!livro.isPresent()) {
			throw new LivroNaoEncontradoException();
		}
		
		return livro.get();
	}

}
