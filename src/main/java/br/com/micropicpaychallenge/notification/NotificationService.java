package br.com.micropicpaychallenge.notification;

import org.springframework.stereotype.Service;

import br.com.micropicpaychallenge.transaction.Transaction;

@Service
public class NotificationService {

    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer){
        this.notificationProducer = notificationProducer;
    }

    public void notify(Transaction transaction){
        notificationProducer.sendNotification(transaction);
    }
}
