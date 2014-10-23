package com.mudo.promoskop.parser.launch;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.json.JSONObject;

import com.mudo.promoskop.web.model.Product;

public class LogoFinder {

	private static final String GOOGLE_API = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	private static final String CHARSET = "UTF-8";

	public static void saveProductImage(Product product) {
		try {
			BufferedImage productImage = findProductImageFromGoogle(product.getName());
			productImage = Scalr.resize(productImage, 100, 100);
			saveProductImage(productImage, product.getName());
		} catch (Exception e) {
			System.out.println(product.getName() + " : " + e.getMessage());
		}
	}

	public static void saveProductImage(BufferedImage uniImage, String uniName) throws Exception {
		File outputfile = new File("logo/" + uniName + ".png");
		ImageIO.write(uniImage, "png", outputfile);
	}

	public static BufferedImage findProductImageFromGoogle(String productName) throws Exception {
		URL url = new URL(GOOGLE_API + URLEncoder.encode(getSearchKey(productName), CHARSET));
		URLConnection connection = url.openConnection();
		JSONObject json = getJsonObject(connection);
		BufferedImage image = getResultImage(json);
		return image;
	}

	private static String getSearchKey(String uniName) {
		return (uniName.contains("(") ? uniName.substring(0, (uniName.indexOf("(") - 1)) : uniName) + " logo";
	}

	private static JSONObject getJsonObject(URLConnection connection) throws Exception {
		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return new JSONObject(builder.toString());
	}

	private static BufferedImage getResultImage(JSONObject json) throws Exception {
		String imageUrl = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("url");
		return ImageIO.read(new URL(imageUrl));
	}
}
