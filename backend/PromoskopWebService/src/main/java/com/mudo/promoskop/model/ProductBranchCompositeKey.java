package com.mudo.promoskop.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class ProductBranchCompositeKey implements Serializable {
	private static final long serialVersionUID = 1L;

	private Product product;

	private Branch branch;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof ProductBranchCompositeKey))
			return false;
		ProductBranchCompositeKey compositeKey = (ProductBranchCompositeKey) obj;
		Product product = compositeKey.getProduct();
		Branch branch = compositeKey.getBranch();
		return ((product != null && this.product.getId() == product.getId()) && (branch != null && this.branch.getId() == branch.getId()));
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(product.getId()).append(branch.getId()).toHashCode();
	}
}