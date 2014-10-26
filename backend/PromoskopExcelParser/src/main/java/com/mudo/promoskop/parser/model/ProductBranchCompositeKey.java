package com.mudo.promoskop.parser.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ProductBranchCompositeKey implements Serializable {
	private static final long serialVersionUID = 1L;

	private Product product;

	private Branch branch;

	private transient int productId;
	private transient int branchId;

	public ProductBranchCompositeKey() {
	}

	public ProductBranchCompositeKey(Product product, Branch branch) {
		this.product = product;
		if (product != null)
			this.productId = product.getId();

		this.branch = branch;
		if (branch != null)
			this.branchId = branch.getId();
	}

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

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
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
		return ((product != null && this.productId == product.getId()) && (branch != null && this.branchId == branch.getId()));
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}