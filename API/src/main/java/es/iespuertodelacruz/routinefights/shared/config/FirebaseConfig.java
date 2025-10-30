package es.iespuertodelacruz.routinefights.shared.config;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FirebaseConfig {

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
      FirebaseOptions options = FirebaseOptions.builder()
              .setCredentials(credentials)
              .build();
      List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
      if (firebaseApps != null && !firebaseApps.isEmpty()) {
          return firebaseApps.get(0);
      }

      return FirebaseApp.initializeApp(options);
}

    @Bean
    GoogleCredentials googleCredentials(FirebaseCredentialsConfig firebaseProperties) throws IOException {
        if (firebaseProperties.getServiceAccount() != null) {
            try (InputStream is = firebaseProperties.getServiceAccount().getInputStream()) {
                return GoogleCredentials.fromStream(is);
            }
        } else {
            return GoogleCredentials.getApplicationDefault();
        }
    }
}
