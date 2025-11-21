package com.example.SpringAi_Demo.controller;

import java.io.IOException;

//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.image.ImageResponse;
////import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.SpringAi_Demo.Service.ChatImpl;
//
//import jakarta.servlet.http.HttpServletResponse;
//import reactor.core.publisher.Flux;

/*@RestController
@CrossOrigin(origins = "http://192.168.1.66:3000")
public class chatController { */

	/*private ChatClient chatClient;

	public chatController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	@GetMapping("/chat")
	public ResponseEntity<String> getChatResponse(@RequestParam String message){
		String content = chatClient.prompt(message).call().content();
		return ResponseEntity.ok(content);
	}*/
	
	/*private ChatClient ollamaChatClient;

	public chatController(OllamaChatModel ollamaChatModel) {
		this.ollamaChatClient = ChatClient.builder(ollamaChatModel).build();
	}
	
	@GetMapping("/chat")
	public ResponseEntity<String> getChatResponse(@RequestParam String message){
		String content = ollamaChatClient.prompt(message).call().content();
		return ResponseEntity.ok(content);
	}*/
	
	/*@Autowired
	private ChatImpl chatImpl;

	@GetMapping("/chat")
	public ResponseEntity<String> getChatResponse(@RequestParam String message){
		String content = chatImpl.chat(message);
		return ResponseEntity.ok(content);
	}*/
	
/*	@Autowired
	private ChatImpl chatImpl;

	@GetMapping("/stream-chat/{uid}")
	public ResponseEntity<Flux<String>> getChatResponseAsSmallChunks(@RequestParam String message, @PathVariable String uid){
		Flux<String> content = chatImpl.streamChat(message, uid);
		return ResponseEntity.ok(content);
	}
	
	@GetMapping("/generate-image")
	public void genaerateImage(HttpServletResponse res, @RequestParam String message) throws IOException{
		ImageResponse image = chatImpl.generateImage(message);
		String imageURL = image.getResult().getOutput().getUrl();
		res.sendRedirect(imageURL);
	}
	
}*/
