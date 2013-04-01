package fr.jerep6.ogi.transfert.database;

import java.util.HashSet;
import java.util.Set;

import fr.jerep6.ogi.persistance.bo.Author;

public class CustomObj {
	private Integer		techid;
	private Set<Author>	author	= new HashSet<>();

	public CustomObj(Integer techid, Author author) {
		super();
		this.techid = techid;
		this.author.add(author);
	}

	public CustomObj(Integer techid, Set<Author> author) {
		super();
		this.techid = techid;
		this.author = author;
	}

	public Set<Author> getAuthor() {
		return author;
	}

	public Integer getTechid() {
		return techid;
	}

	public void setAuthor(Set<Author> author) {
		this.author = author;
	}

	public void setTechid(Integer techid) {
		this.techid = techid;
	}

}
