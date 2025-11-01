package es.iespuertodelacruz.routinefights.shared.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import lombok.extern.java.Log;

@Service
@Log
public class NotificationsService {
    private FirebaseMessaging fcm;

    public NotificationsService(FirebaseMessaging fcm) {
        this.fcm = fcm;
    }

    public String sendToAllUsers(String title, String body) {
        Message message = Message.builder()
                .setTopic("general")
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .build();

        try {
            String sent = fcm.send(message);
            log.info("Sent message to all users: " + sent);
            return sent;
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendTo(String title, String body, String userToken) {
        Message message = Message.builder()
                .setToken(userToken)
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .build();

        try {
            String sent = fcm.send(message);
            log.info("Sent message to user: " + sent);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendTo(String title, String body, List<String> usersTokens) {
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .addAllTokens(usersTokens)
                .build();
        try {
            BatchResponse sent = fcm.sendMulticast(message);
            log.info("Sent message to some users: Success:" + sent.getSuccessCount() + " Failures: "
                    + sent.getFailureCount());
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
