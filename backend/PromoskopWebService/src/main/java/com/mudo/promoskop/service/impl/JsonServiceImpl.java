package com.mudo.promoskop.service.impl;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.mudo.promoskop.exception.InternalServerErrorException;
import com.mudo.promoskop.response.BranchResponse;
import com.mudo.promoskop.response.ProductResponse;
import com.mudo.promoskop.service.BranchResponseService;
import com.mudo.promoskop.service.JsonService;
import com.mudo.promoskop.service.ProductResponseService;
import com.mudo.promoskop.util.JsonFilter;

@Service
public class JsonServiceImpl implements JsonService {
	private static Logger LOG = LoggerFactory.getLogger(JsonServiceImpl.class);

	private ObjectMapper mapper;
	private SimpleFilterProvider filters;

	@Autowired
	private ProductResponseService productResponseService;

	@Autowired
	private BranchResponseService branchResponseService;

	public JsonServiceImpl() {
		mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.getSerializationConfig().addMixInAnnotations(Object.class, JsonServiceImpl.class);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

		filters = new SimpleFilterProvider();
	}

	@Override
	public String generateJsonForProduct(JsonFilter filter, int id) throws Exception {
		ProductResponse product = productResponseService.findById(id);

		ObjectWriter writer = getFilteredWriter(filter);
		return writer.writeValueAsString(product);
	}

	@Override
	public String generateJsonForProducts(JsonFilter filter, String containText) throws Exception {
		List<ProductResponse> matchingProducts = productResponseService.findBySubString(containText);

		ObjectWriter writer = getFilteredWriter(filter);
		return writer.writeValueAsString(matchingProducts);
	}

	@Override
	public String generateJsonForPopularProducts(JsonFilter filter, int count) throws Exception {
		List<ProductResponse> popularProducts = productResponseService.findMaxQueried(count);

		ObjectWriter writer = getFilteredWriter(filter);
		return writer.writeValueAsString(popularProducts);
	}

	@Override
	public String generateJsonForBasket(JsonFilter filter, double currentLatitude, double currentLongitude, double maxDistance, int[] barcodeIds) throws Exception {
		LatLng start = new LatLng(currentLatitude, currentLongitude);

		List<BranchResponse> branchResponses = branchResponseService.findProductBranchWithMinPrice(barcodeIds,
				LatLngTool.travel(start, LatLngTool.Bearing.SOUTH, maxDistance, LengthUnit.KILOMETER).getLatitude(),
				LatLngTool.travel(start, LatLngTool.Bearing.WEST, maxDistance, LengthUnit.KILOMETER).getLongitude(),
				LatLngTool.travel(start, LatLngTool.Bearing.NORTH, maxDistance, LengthUnit.KILOMETER).getLatitude(),
				LatLngTool.travel(start, LatLngTool.Bearing.EAST, maxDistance, LengthUnit.KILOMETER).getLongitude());

		ObjectWriter writer = getFilteredWriter(filter);
		return writer.writeValueAsString(branchResponses);
	}

	@Override
	public String generateJsonForException(Exception ex) {
		try {
			ObjectWriter writer = getFilteredWriter(JsonFilter.EXCEPTION_FILTER);
			return writer.writeValueAsString(ex);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return generateJsonForException(new InternalServerErrorException());
		}
	}
	
	@Override
	public String generateJsonForAppConfiguration(HashMap<String, Object> conf)
			throws Exception {
		return mapper.writeValueAsString(conf);
	}
	

	private ObjectWriter getFilteredWriter(JsonFilter filter) {
		filters.addFilter("filterResponseBean", SimpleBeanPropertyFilter.serializeAllExcept(filter.getFilteredFields()));
		return mapper.writer(filters);
	}
}
