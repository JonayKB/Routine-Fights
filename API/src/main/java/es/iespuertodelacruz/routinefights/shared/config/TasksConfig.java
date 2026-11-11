package es.iespuertodelacruz.routinefights.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Scheduled tasks, and @Async for notifications sent after a request. */
@EnableScheduling
@EnableAsync
@Configuration
public class TasksConfig {

}
