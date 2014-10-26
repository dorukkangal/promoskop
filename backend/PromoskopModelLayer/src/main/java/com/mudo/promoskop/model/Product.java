package com.mudo.promoskop.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String name;

	private String url;

	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	private Set<ProductBranch> productBranchs = new HashSet<ProductBranch>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<ProductBranch> getProductBranchs() {
		return productBranchs;
	}

	public void setProductBranchs(Set<ProductBranch> productBranchs) {
		this.productBranchs = productBranchs;
	}
}
