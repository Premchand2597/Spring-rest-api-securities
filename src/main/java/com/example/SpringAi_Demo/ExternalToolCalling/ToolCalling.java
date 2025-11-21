//package com.example.SpringAi_Demo.ExternalToolCalling;
//
//import java.time.LocalDateTime;
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.context.i18n.LocaleContextHolder;
//
//public class ToolCalling {
//
//	@Tool(description = "Get the current date and time in users zone")
//	public String getCurrentDateAndTime() {
//		
//		String dateTimeValue = LocalDateTime.now()
//				.atZone(LocaleContextHolder.getTimeZone().toZoneId())
//				.toString();
//		
//		System.out.println("dateTimeValue = "+dateTimeValue);
//		
//		return dateTimeValue;
//	}
//}
