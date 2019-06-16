package com.bridgelabz.fundoo.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.springframework.stereotype.Component;

@Component
public class Utility {
	public static String currentDate() {
		LocalDateTime today = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = today.format(formatter);
		return formatDateTime;
	}
}
