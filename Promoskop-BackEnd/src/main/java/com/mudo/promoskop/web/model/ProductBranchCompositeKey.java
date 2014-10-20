package com.mudo.promoskop.web.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ProductBranchCompositeKey implements Serializable {
	private static final long serialVersionUID = 1L;

	private Product product;

	private Branch branch;

	public ProductBranchCompositeKey() {
	}

	public ProductBranchCompositeKey(Product product, Branch branch) {
		this.product = product;
		this.branch = branch;
	}

	public Product getProduct() {
		return product;
	}

	public void setProductId(Product product) {
		this.product = product;
	}

	public Branch getBranchId() {
		return branch;
	}

	public void setBranchId(Branch branch) {
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
		return true;//(this.product.equals(compositeKey.getProductId()) && this.branchId.equals(compositeKey.getBranchId()));
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}