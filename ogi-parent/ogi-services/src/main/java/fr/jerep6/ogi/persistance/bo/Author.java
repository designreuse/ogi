package fr.jerep6.ogi.persistance.bo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

// Hibernate use the name of then entity to determine the table name. But the purpose of this name is to use in JPQL
//@Entity
@Table(name = "TA_AUTHOR")
public class Author {
	@Id
	@Column(name = "AUT_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer		techid;

	@Column(name = "AUT_NAME", nullable = false, unique = true, length = 64)
	private String		name;

	@ManyToMany
	@JoinTable(name = "TJ_BOO_AUT", joinColumns = @JoinColumn(name = "BOO_ID"), inverseJoinColumns = @JoinColumn(name = "AUT_ID"))
	private Set<Book>	books;

	public Set<Book> getBooks() {
		return books;
	}

	public String getName() {
		return name;
	}

	public Integer getTechid() {
		return techid;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTechid(Integer techid) {
		this.techid = techid;
	}

}
