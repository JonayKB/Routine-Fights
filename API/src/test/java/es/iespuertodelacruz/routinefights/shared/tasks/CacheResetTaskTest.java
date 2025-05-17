package es.iespuertodelacruz.routinefights.shared.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import java.util.Arrays;
import static org.mockito.Mockito.*;

class CacheResetTaskTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    private CacheResetTask cacheResetTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheResetTask = new CacheResetTask(cacheManager);
    }

    @Test
    void testClearAllCaches() {
        when(cacheManager.getCacheNames()).thenReturn(Arrays.asList("cache1", "cache2"));
        when(cacheManager.getCache("cache1")).thenReturn(cache);
        when(cacheManager.getCache("cache2")).thenReturn(cache);

        cacheResetTask.clearAllCaches();

        verify(cacheManager, times(1)).getCacheNames();
        verify(cache, times(2)).clear();
    }
}
