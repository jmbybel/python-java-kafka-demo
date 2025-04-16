package com.example.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import org.springframework.web.client.RestClientException;
import org.springframework.kafka.core.KafkaTemplate;

@RestController
public class HelloKafka {
    
    private static final String KAFKA_TOPIC = "test-topic";

    @Autowired
    private KafkaTemplate<String, Map<String, String>> kafkaTemplate;


    @GetMapping("/hello")
    public String helloWorldApiCall() {
        return "Hello World";
    
        
    }
    @GetMapping("/worldhello")
    public String worldHelloCrossApi() {
        // fetch HOST_MACHINE environment variable
        String hostMachine = System.getenv("HOST_MACHINE");
        
        // create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            // build the URL using the host machine environment variable
            String url = "http://" + hostMachine + ":5000/worldhello";
            
            // make the GET request and return the response
            String response = restTemplate.getForObject(url, String.class);
            return response != null ? response : "No response received";
            
        } catch (RestClientException e) {
            // handle any connection errors
            return "Error calling Python service: " + e.getMessage();
        }
    }
    

    
    @GetMapping("/push-message")
    public String pushMessage() {
        try {
            // Create message with timestamp
            Map<String, String> message = new HashMap<>();
            message.put("timestamp", LocalDateTime.now().toString());
            message.put("service", "java-service");
            message.put("message", "Test message from Java");

            // Send message to Kafka
 //           CompletableFuture<SendResult<String, Map<String, String>>> future = 
                kafkaTemplate.send(KAFKA_TOPIC, message);
/* 
            // Wait for the result
            SendResult<String, Map<String, String>> result = future.get();

            // Create success response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("topic", result.getRecordMetadata().topic());
            response.put("partition", result.getRecordMetadata().partition());
            response.put("offset", result.getRecordMetadata().offset());
*/
            return "Kafka message sent";

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return errorResponse.toString();
        }
    }

    @GetMapping("/receive-message")
    public String receiveMessage() {
        //receive message from the topic and displays it to the user as json.

        ConsumerRecord<String, Map<String, String>>  r = kafkaTemplate.receive("test-topic", 1, 0);

        return r.toString();




        
    }
/*
    // Kafka Consumer using @KafkaListener
    @KafkaListener(topics = KAFKA_TOPIC, groupId = "java-service-group")
    public void listen(Map<String, String> message) {
        try {
            // Process the message
            System.out.println("Received message: " + message);
            // Add your message processing logic here
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
*/
}