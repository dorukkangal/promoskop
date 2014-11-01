package com.mudo.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.service.ProductService;
import com.mudo.promoskop.util.JsonGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/applicationContext.xml" })
public class MainTest {

	@Autowired
	ProductService productService;

	private Time elapsedTime;

	@Test
	public void testFindById() {
		elapsedTime = new Time("findById");
		Product product;
		int productId = 11000036;
		String json;

		product = productService.findById(productId);
		json = JsonGenerator.generateJson(new String[] {}, product);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);

		product = productService.findById(productId);
		json = JsonGenerator.generateJson(new String[] {}, product);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
	}

	@Test
	public void testFindBySubString() {
		elapsedTime = new Time("findBySubString");
		List<Product> productList;
		String text = "YU";
		String json;

		productList = productService.findBySubString(text);
		json = JsonGenerator.generateJson(new String[] { "branches" }, productList);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);

		productList = productService.findBySubString(text);
		json = JsonGenerator.generateJson(new String[] { "branches" }, productList);
		System.out.println(json);
		elapsedTime.miliseconds(System.out);
	}
}
