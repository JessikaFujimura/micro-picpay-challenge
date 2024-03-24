package br.com.micropicpaychallenge.notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.micropicpaychallenge.transaction.Transaction;

@Service
public class NotificationProducer {
    
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    private NotificationProducer( KafkaTemplate<String, Transaction> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(Transaction transaction){
        kafkaTemplate.send("transaction-notification", transaction);
    }
}
