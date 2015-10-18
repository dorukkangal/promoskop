package com.mudo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mudo.promoskop.service.BranchService;
import com.mudo.promoskop.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/applicationContext.xml" })
public class MainTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private BranchService branchService;

	private Time elapsedTime;

	@Test
	public void testFindById() throws Exception {
		elapsedTime = new Time("findById");

//		String barcode = "8696368099002";
//		double latitude = 40.00;
//		double longititude = 30.00;
//		List<ProductBranch> branches;

//		branches = branchService.findCheapestAndClosest(barcode, latitude, longititude);
		elapsedTime.miliseconds(System.out);

		elapsedTime.refreshTime();

//		branches = branchService.findCheapestAndClosest(barcode, latitude, longititude);
		elapsedTime.miliseconds(System.out);
	}

	@Test
	public void testFindBySubString() throws Exception {
		elapsedTime = new Time("findBySubString");

//		String containText = "YU";
//		List<Product> matchingProducts;

//		matchingProducts = productService.findBySubString(containText);
		elapsedTime.miliseconds(System.out);

		elapsedTime.refreshTime();

//		matchingProducts = productService.findBySubString(containText);
		elapsedTime.miliseconds(System.out);
	}
}
