package com.mudo.promoskop.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
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
import com.mudo.promoskop.web.model.ResponseBean;
import com.mudo.promoskop.web.model.Store;

public class JsonPropertyFilter {

	public static String generateJson(String[] ignorableFieldNames, List<Product> products) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.getSerializationConfig().addMixInAnnotations(Object.class, JsonPropertyFilter.class);
		mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

		FilterProvider filters = new SimpleFilterProvider().addFilter("filterResponseBean", SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
		ObjectWriter writer = mapper.writer(filters);

		return writer.writeValueAsString(initResponseBeans(products));
	}

	private static List<ResponseBean> initResponseBeans(List<Product> products) {
		List<ResponseBean> beans = new ArrayList<ResponseBean>();
		for (Product product : products)
			beans.add(fillResponseBeanFields(product));
		return beans;
	}

	private static ResponseBean fillResponseBeanFields(Product product) {
		ResponseBean bean = new ResponseBean();
		bean.setBarcodeId(product.getId());
		bean.setProductName(product.getName());

		for (ProductBranch productBranch : product.getProductBranchs()) {
			bean.setPrice(productBranch.getPrice());

			Branch branch = productBranch.getBranch();
			if (branch != null) {
				bean.setBranchName(branch.getName());
				bean.setAddress(branch.getAddress());
				bean.setLatitude(branch.getLatitude());
				bean.setLongtitude(branch.getLongtitude());

				Store store = branch.getStore();
				if (store != null)
					bean.setStoreName(store.getName());
			}
		}
		return bean;
	}
}
