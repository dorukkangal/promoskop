package com.mudo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mudo.promoskop.service.JsonService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/applicationContext.xml" })
public class MainTest {

	@Autowired
	JsonService jsonService;

	private Time elapsedTime;

	@Test
	public void testFindById() {
		elapsedTime = new Time("findById");
		int productId = 11000036;
		ResponseEntity<String> json;

		json = jsonService.generateJsonForProduct(JsonService.PRODUCT_BY_ID_FILTER, productId);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
		elapsedTime.refreshTime();

		json = jsonService.generateJsonForProduct(JsonService.PRODUCT_BY_ID_FILTER, productId);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
	}

	@Test
	public void testFindBySubString() {
		elapsedTime = new Time("findBySubString");
		String containText = "YU";
		ResponseEntity<String> json;

		json = jsonService.generateJsonForProducts(JsonService.PRODUCT_BY_NAME_FILTER, containText);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
		elapsedTime.refreshTime();

		json = jsonService.generateJsonForProducts(JsonService.PRODUCT_BY_NAME_FILTER, containText);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
	}
}
