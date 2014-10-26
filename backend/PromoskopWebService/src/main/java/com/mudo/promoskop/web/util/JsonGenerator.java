package com.mudo.promoskop.web.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.mudo.promoskop.web.model.ResponseBean;
import com.mudo.promoskop.web.model.Store;

public class JsonGenerator {

	public static Map<String, Object> generateJson(String[] ignorableFieldNames, List<Product> products) throws Exception {
		List<ResponseBean> responses = initResponseBeans(products);

		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		for (ResponseBean response : responses) {
			Field[] fields = response.getClass().getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				if (!isIgnoredField(ignorableFieldNames, f))
					jsonMap.put(getLowerCaseWithUnderscoresFieldName(f), f.get(response));
			}
		}
		return jsonMap;
	}

	public static String generateJackson(String[] ignorableFieldNames, List<Product> products) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.getSerializationConfig().addMixInAnnotations(Object.class, JsonGenerator.class);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

		FilterProvider filters = new SimpleFilterProvider().addFilter("filterResponseBean", SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
		ObjectWriter writer = mapper.writer(filters);

		return writer.writeValueAsString(initResponseBeans(products));
	}
	
	private static String getLowerCaseWithUnderscoresFieldName(Field f) {
		String input = f.getName();
		if (input == null)
			return input; // garbage in, garbage out
		int length = input.length();
		StringBuilder result = new StringBuilder(length * 2);
		int resultLength = 0;
		boolean wasPrevTranslated = false;
		for (int i = 0; i < length; i++) {
			char c = input.charAt(i);
			if (i > 0 || c != '_') // skip first starting underscore
			{
				if (Character.isUpperCase(c)) {
					if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
						result.append('_');
						resultLength++;
					}
					c = Character.toLowerCase(c);
					wasPrevTranslated = true;
				} else {
					wasPrevTranslated = false;
				}
				result.append(c);
				resultLength++;
			}
		}
		return resultLength > 0 ? result.toString() : input;
	}

	private static boolean isIgnoredField(String[] ignorableFieldNames, Field f) {
		for (String ignoredField : ignorableFieldNames)
			if (ignoredField.equals(f.getName()))
				return true;
		return false;
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
		bean.setUrl(product.getUrl());

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
