package com.mudo.promoskop.service;

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
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.exception.InternalServerErrorException;
import com.mudo.promoskop.exception.ResourceNotFoundException;
import com.mudo.promoskop.response.ProductResponse;

@Service
@Transactional
public class JsonServiceImpl implements JsonService {
	private static Logger LOG = LoggerFactory.getLogger(JsonServiceImpl.class);

	private ObjectMapper mapper;
	private SimpleFilterProvider filters;

	@Autowired
	private ProductResponseService productResponseService;

	public JsonServiceImpl() {
		mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.getSerializationConfig().addMixInAnnotations(Object.class, JsonServiceImpl.class);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

		filters = new SimpleFilterProvider();
	}

	@Override
	public ResponseEntity<String> generateJsonForProduct(String[] ignorableFieldNames, int id) {
		try {
			ProductResponse response = productResponseService.findById(id);

			ObjectWriter writer = getFilteredWriter(ignorableFieldNames);
			String json = writer.writeValueAsString(response);
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
	public ResponseEntity<String> generateJsonForProducts(String[] ignorableFieldNames, String containText) {
		try {
			List<ProductResponse> matchingResponses = productResponseService.findBySubString(containText);
			
			ObjectWriter writer = getFilteredWriter(ignorableFieldNames);
			String json = writer.writeValueAsString(matchingResponses);
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
			ObjectWriter writer = getFilteredWriter(JsonService.EXCEPTION_FILTER);
			return writer.writeValueAsString(ex);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return generateJsonForException(new InternalServerErrorException());
		}
	}

	private ObjectWriter getFilteredWriter(String[] ignorableFieldNames) {
		filters.addFilter("filterResponseBean", SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
		return mapper.writer(filters);
	}
}
