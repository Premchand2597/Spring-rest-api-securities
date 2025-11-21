package com.example.SpringAi_Demo.Config;

import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
//import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
//import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.memory.MessageWindowChatMemory;
//import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

//@Configuration
//public class AiConfig {
	
//	@Bean
//	ChatMemory increaseOrDecreaseMaxMessageStoreInDB(JdbcChatMemoryRepository repo) {
//		return MessageWindowChatMemory.builder()
//				.chatMemoryRepository(repo)
//				.maxMessages(1000000000)
//				.build();
//	}

	//private Logger logger = LoggerFactory.getLogger(AiConfig.class);

   /* @Bean
    ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {

        //this.logger.info("ChatMemoryImplementation class: " + chatMemory.getClass().getName());
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return builder
                .defaultAdvisors(messageChatMemoryAdvisor, new SafeGuardAdvisor(List.of("games")))
                //.defaultSystem("You are a helpful coding assistant. You are an expert in coding.")
                //.defaultOptions(OpenAiChatOptions.builder()
                //        .model("gpt-4o-mini")
               //         .temperature(0.3)
               //         .maxTokens(200)
                //        .build())
                .build();
    }*/
	
//	@Bean
//    ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
//        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
//        return builder
//                .defaultAdvisors(messageChatMemoryAdvisor)
//                .build();
//    }
//}
