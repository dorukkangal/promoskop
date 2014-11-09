package com.mudo.promoskop.service.impl;

import java.io.IOException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mudo.promoskop.exception.InternalServerErrorException;
import com.mudo.promoskop.exception.ResourceNotFoundException;
import com.mudo.promoskop.response.BranchResponse;
import com.mudo.promoskop.response.ProductResponse;
import com.mudo.promoskop.service.BranchResponseService;
import com.mudo.promoskop.service.JsonService;
import com.mudo.promoskop.service.ProductResponseService;
import com.mudo.promoskop.util.DistanceUtil;
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
	public ResponseEntity<String> generateJsonForProduct(JsonFilter filter, int id) {
		try {
			ProductResponse product = productResponseService.findById(id);

			ObjectWriter writer = getFilteredWriter(filter);
			String json = writer.writeValueAsString(product);
			return new ResponseEntity<String>(json, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			String json = generateJsonForException(e);
			return new ResponseEntity<String>(json, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			String json = generateJsonForException(new InternalServerErrorException());
			return new ResponseEntity<String>(json, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> generateJsonForProducts(JsonFilter filter, String containText) {
		try {
			List<ProductResponse> matchingProducts = productResponseService.findBySubString(containText);

			ObjectWriter writer = getFilteredWriter(filter);
			String json = writer.writeValueAsString(matchingProducts);
			return new ResponseEntity<String>(json, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			String json = generateJsonForException(new InternalServerErrorException());
			return new ResponseEntity<String>(json, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> generateJsonForPopularProducts(JsonFilter filter, int count) {
		try {
			List<ProductResponse> popularProducts = productResponseService.findMaxQueried(count);

			ObjectWriter writer = getFilteredWriter(filter);
			String json = writer.writeValueAsString(popularProducts);
			return new ResponseEntity<String>(json, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			String json = generateJsonForException(new InternalServerErrorException());
			return new ResponseEntity<String>(json, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> generateJsonForBasket(JsonFilter filter, double currentLatitude, double currentLongitude, double maxDistance, int[] barcodeIds) {
		try {
			double[] minMaxLatitudes = DistanceUtil.minMaxLatitudes(currentLatitude, currentLongitude, maxDistance);
			double[] minMaxLongitudes = DistanceUtil.minMaxLongitudes(currentLatitude, currentLongitude, maxDistance);
			List<BranchResponse> branchResponses = branchResponseService.findProductBranchWithMinPrice(barcodeIds, minMaxLatitudes[0], minMaxLatitudes[1],
					minMaxLongitudes[0], minMaxLongitudes[1]);
			ObjectWriter writer = getFilteredWriter(filter);
			String json = writer.writeValueAsString(branchResponses);
			return new ResponseEntity<String>(json, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			String json = generateJsonForException(new InternalServerErrorException());
			return new ResponseEntity<String>(json, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public String generateJsonForException(Exception ex) {
		try {
			LOG.error(ex.getMessage(), ex);
			ObjectWriter writer = getFilteredWriter(JsonFilter.EXCEPTION_FILTER);
			return writer.writeValueAsString(ex);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return generateJsonForException(new InternalServerErrorException());
		}
	}

	private ObjectWriter getFilteredWriter(JsonFilter filter) {
		filters.addFilter("filterResponseBean", SimpleBeanPropertyFilter.serializeAllExcept(filter.getFilteredFields()));
		return mapper.writer(filters);
	}
}
