/*
 * Copyright (c) 2013 Joseph Su
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


public class BarcodeReaderDemo {

	public static void main(String[] args) throws Throwable {  
		
		String url = null;// args[0];	
		
		String code = readBarcode(url);
		String price = getJsonPrice(code);
		
		System.out.println();
		System.out.println("LowestPrice: " + price);
	}  
	
	public static String getJsonPrice(String barcode) throws Throwable {
		
		String price = "0.99";
		String json = "";
		
		String urlString = null;
		String uri = "https://www.googleapis.com/shopping/search/v1/public/products?key=AIzaSyBZPWOr79_uGj7yqCbMakrKElaoL5ZFcfc&country=US&restrictBy=gtin:";		
		
		urlString = uri.concat(barcode);
		URL url = new URL(urlString);
		
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));
        
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            json += inputLine;
        }
        in.close();
        
		System.out.println("CHECKING GOOGLE SHOPPING ...");
        
		System.out.println();
	
        LOG.fine("JSON OUTPUT = " + json);

        AppData data = new Gson().fromJson(json, AppData.class);
        //System.out.println(data.getItems());
        
		return price;
	}
	
	
	public static String readBarcode(String urlString) throws IOException, ReaderException, FormatException {
		
		//InputStream barCodeInputStream = new FileInputStream("sprite.jpg");  
		URL url = new URL(urlString);
		InputStream barCodeInputStream = url.openStream();
		BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);  
		  
		LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);  
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
		Reader reader = new MultiFormatReader();  
		Result result = reader.decode(bitmap);  
		  
		String barcode = result.getText();
		int barLength = barcode.length();
		String leadingZero = "0";
		if (barLength < 14) {
			int diff = 14 - barLength;			
			for (int i=0; i<diff; i++) {
				barcode = leadingZero.concat(barcode); 
			}
		}
		
		System.out.println("CONVERTING BARCODE IMAGE ... ");

		System.out.println();

		System.out.println("DONE CONVERTING ...");

		System.out.println();

		System.out.println("BARCOE READ: " + barcode);
		
		System.out.println();	
		
		return barcode;
		
	}
	
	public class AppDataResults {
		
		public AppDataResults() {			
		}
		
		private String id = null;
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		
	}

	public class AppData {
		
		private AppDataResults[] items;
		
		public AppDataResults[] getItems() {
			return items;
		}			
	}
	
}
