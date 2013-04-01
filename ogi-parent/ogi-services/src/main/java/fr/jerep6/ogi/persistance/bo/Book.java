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
/**
 * @author jerep6 Feb 26, 2013
 */
// @Entity
@Table(name = "TA_BOOK")
public class Book {
	@Id
	@Column(name = "BOO_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer		techid;

	@Column(name = "BOO_TITLE", nullable = false, unique = true, length = 64)
	private String		title;

	@ManyToMany
	@JoinTable(name = "TJ_BOO_AUT", joinColumns = @JoinColumn(name = "BOO_ID"), inverseJoinColumns = @JoinColumn(name = "AUT_ID"))
	private Set<Author>	authors;

	public Set<Author> getAuthors() {
		return authors;
	}

	public Integer getTechid() {
		return techid;
	}

	public String getTitle() {
		return title;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	public void setTechid(Integer techid) {
		this.techid = techid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Book [techid=" + techid + ", title=" + title + "]";
	}

}
