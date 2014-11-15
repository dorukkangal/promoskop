package com.mudo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mudo.promoskop.service.JsonService;
import com.mudo.promoskop.util.JsonFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/applicationContext.xml" })
public class MainTest {

	@Autowired
	JsonService jsonService;

	private Time elapsedTime;

	@Test
	public void testFindById() throws Exception {
		elapsedTime = new Time("findById");
		String productId = "11000036";
		String json;

		json = jsonService.generateJsonForProduct(JsonFilter.PRODUCT_BY_ID_FILTER, productId);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
		elapsedTime.refreshTime();

		json = jsonService.generateJsonForProduct(JsonFilter.PRODUCT_BY_ID_FILTER, productId);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
	}

	@Test
	public void testFindBySubString() throws Exception {
		elapsedTime = new Time("findBySubString");
		String containText = "YU";
		String json;

		json = jsonService.generateJsonForProducts(JsonFilter.PRODUCT_BY_NAME_FILTER, containText);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
		elapsedTime.refreshTime();

		json = jsonService.generateJsonForProducts(JsonFilter.PRODUCT_BY_NAME_FILTER, containText);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
	}
}
