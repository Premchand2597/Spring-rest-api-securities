package com.example.SpringAi_Demo.Service;

//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.image.ImagePrompt;
//import org.springframework.ai.image.ImageResponse;
//import org.springframework.ai.openai.OpenAiImageModel;
//import org.springframework.stereotype.Service;
//
//import com.example.SpringAi_Demo.ExternalToolCalling.ToolCalling;
//import com.example.SpringAi_Demo.Repo.ChatRepo;
//
//import reactor.core.publisher.Flux;
//
//@Service
//public class ChatImpl implements ChatRepo{
//	
//	private ChatClient chatClient;
//	
//	private OpenAiImageModel openAiImageModel;
//
//	public ChatImpl(ChatClient chatClient, OpenAiImageModel openAiImageModel) {
//		this.chatClient = chatClient;
//		this.openAiImageModel = openAiImageModel;
//	}
//
//	@Override
//	public String chat(String query) {
//		Prompt newPrompt = new Prompt(query);
//		var content = chatClient.prompt(newPrompt).call().content();
//		return content;
//	}

	/*@Override
	public Flux<String> streamChat(String query, String uid) {
		//Prompt newPrompt = new Prompt(query);
		var content = chatClient.prompt(query)
								.advisors(AdvisorSpec->AdvisorSpec.param(ChatMemory.CONVERSATION_ID, uid))
								.stream()
								.content();
		return content;
	}*/
	
//	@Override
//	public Flux<String> streamChat(String query, String uid) {
//		var content = chatClient.prompt()
//								//.tools(new ToolCalling())
//								.user(query)
//								.advisors(AdvisorSpec->AdvisorSpec.param(ChatMemory.CONVERSATION_ID, uid))
//								.stream()
//								.content();
//		return content;
//	}
//
//	@Override
//	public ImageResponse generateImage(String prompt) {
//		ImageResponse imageResponse = openAiImageModel.call(
//				new ImagePrompt(prompt)
//				);
//		return imageResponse;
//	}
//
//}
