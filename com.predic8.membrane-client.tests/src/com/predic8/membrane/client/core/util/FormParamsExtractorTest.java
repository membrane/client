package com.predic8.membrane.client.core.util;

import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.predic8.membrane.core.util.ByteUtil;


public class FormParamsExtractorTest extends TestCase {

	private FormParamsExtractor extractor;
	
	public FormParamsExtractorTest() {
		extractor = new FormParamsExtractor();
	}
	
	public void testExtractGetBank() throws Exception {
		String text = new String(ByteUtil.getByteArrayData(this.getClass().getResourceAsStream("/getBank.xml")));
		Map<String, String> res = extractor.extract(text);
		dumpFormParams(res);
		assertTrue(res.size() == 1);
		assertEquals("66762332", res.get("xpath:/getBank/blz"));
	} 
	
	public void testExtractArticle() throws Exception {
		String text = new String(ByteUtil.getByteArrayData(this.getClass().getResourceAsStream("/article.xml")));
		Map<String, String> res = extractor.extract(text);
		dumpFormParams(res);
		
		assertTrue(res.size() == 6);
		
		assertEquals("23", res.get("xpath:/create/article/price/amount"));
		assertEquals("Fine Tea", res.get("xpath:/create/article/description"));		
		assertEquals("Early Gray", res.get("xpath:/create/article/name"));		
		assertEquals("EUR", res.get("xpath:/create/article/price/currency"));
		assertEquals("2010-11-01", res.get("xpath:/create/article/@date"));
		assertEquals("1", res.get("xpath:/create/article/id"));
	} 
	
	private void dumpFormParams(Map<String, String> formParams) {
		System.out.println("Dumping of generated form parameters.");
		System.out.println("====================================");
		Set<String> keys = formParams.keySet();
		for (String key : keys) {
			System.out.println(key + ": " + formParams.get(key));
		}
		System.out.println();
	}
}
