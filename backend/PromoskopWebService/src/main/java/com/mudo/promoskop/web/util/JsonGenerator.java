package com.mudo.promoskop.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import com.mudo.promoskop.web.model.Branch;
import com.mudo.promoskop.web.model.Product;
import com.mudo.promoskop.web.model.ProductBranch;
import com.mudo.promoskop.web.model.Store;
import com.mudo.promoskop.web.response.BranchResponse;
import com.mudo.promoskop.web.response.ProductResponse;

public class JsonGenerator {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.getSerializationConfig().addMixInAnnotations(Object.class, JsonGenerator.class);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	}

	public static String generateJson(String[] ignorableFieldNames, List<Product> products) throws JsonGenerationException, JsonMappingException, IOException {

		FilterProvider filters = new SimpleFilterProvider().addFilter("filterResponseBean", SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
		ObjectWriter writer = mapper.writer(filters);

		return writer.writeValueAsString(initResponseBeans(products));
	}

	private static List<ProductResponse> initResponseBeans(List<Product> products) {
		List<ProductResponse> responseObjects = new ArrayList<ProductResponse>();
		for (Product product : products)
			responseObjects.add(fillResponseBeanFields(product));
		return responseObjects;
	}

	private static ProductResponse fillResponseBeanFields(Product product) {
		ProductResponse productResponse = new ProductResponse();
		productResponse.setBarcodeId(product.getId());
		productResponse.setProductName(product.getName());
		productResponse.setProductUrl(product.getUrl());

		for (ProductBranch productBranch : product.getProductBranchs()) {
			BranchResponse branchResponse = new BranchResponse();
			branchResponse.setPrice(productBranch.getPrice());

			Branch branch = productBranch.getBranch();
			if (branch != null) {
				branchResponse.setBranchName(branch.getName());
				branchResponse.setBranchAddress(branch.getAddress());
				branchResponse.setLatitude(branch.getLatitude());
				branchResponse.setLongtitude(branch.getLongtitude());

				Store store = branch.getStore();
				if (store != null) {
					branchResponse.setStoreName(store.getName());
					branchResponse.setStoreLogo(store.getLogo());
				}
			}
			productResponse.getBranches().add(branchResponse);
		}
		return productResponse;
	}
}
