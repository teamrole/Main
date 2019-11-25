package br.com.irole.api.service.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConversorData {
	
	public static Date stringToDate(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return  dateFormat.parse(date);
		} catch (ParseException e) {
			//TODO log
			return null;
		}
	}
	
}
