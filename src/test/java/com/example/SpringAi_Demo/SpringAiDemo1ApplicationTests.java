//package com.example.SpringAi_Demo;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class SpringAiDemo1ApplicationTests {
//
//	@Autowired
//	private ChatClient chatClient;
//	
//	@Test
//	public void testChatClient() {
//		System.out.println("Started Testing...");
//		
//		var query = "who is sharukhan";
//		var response = chatClient.prompt(query).call().content();
//		
//		System.out.println("Response = "+response);
//		System.out.println("Testing completed!");
//	}
//
//}
