package es.iespuertodelacruz.routinefights.shared.tasks;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheResetTask {
    Logger logger = Logger.getLogger(CacheResetTask.class.getName());

    private final CacheManager cacheManager;

    public CacheResetTask(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void clearAllCaches() {
        cacheManager.getCacheNames()
                .forEach(name -> {
                    Cache cache = cacheManager.getCache(name);
                    if (cache != null) {
                        cache.clear();
                    }
                });
        logger.log(Level.INFO, "All caches cleared at {0}", LocalDateTime.now());
    }

}
