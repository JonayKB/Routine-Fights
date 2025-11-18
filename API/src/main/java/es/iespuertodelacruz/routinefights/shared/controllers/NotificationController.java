package es.iespuertodelacruz.routinefights.shared.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/notifications")
@RestController()
@Tag(name = "NOTIFICATIONS", description = "Notifications")
// @Secured("ROLE_ADMIN")
public class NotificationController {
    private NotificationsService notificationService;

    public NotificationController(NotificationsService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping("/")
    public String postMethodName(@RequestParam String titleKey, @RequestParam String bodyKey, @RequestBody Map<String, Object> args) {
        return notificationService.sendToAllUsers(titleKey, bodyKey, args);
    }

    @PostMapping("/user")
    public String sendToUser(@RequestParam String titleKey, @RequestParam String bodyKey,@RequestBody DeviceToken userToken, @RequestBody Map<String, Object> args) {
        return notificationService.sendTo(titleKey, bodyKey, userToken,  args);
    }
}
