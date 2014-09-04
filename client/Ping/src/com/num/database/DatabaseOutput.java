package com.num.database;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseOutput {
	
	HashMap<String, String> data;
	
	public DatabaseOutput() {
		data = new HashMap<String, String>();
	}
	
	public String getString(String key) {
		return data.get(key);
	}
	
	public Long getLong(String key) {
		return Long.parseLong(data.get(key));
	}
	
	/**
	 * getDouble method gets the value and returns double 
	 * @param key a database key to get the value of
	 * @return value in a form of double
	 */
	public Double getDouble(String key) {
		
		double d=0.0;
		//need to check locale since some countries use decimal comma instead of decimal point
		NumberFormat nf = NumberFormat.getNumberInstance();
		try {
			Number number = nf.parse(data.get(key));
			d = number.doubleValue();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return d;
	}
	
	public void add(String key, String value) {
		data.put(key, value);
	}

}
