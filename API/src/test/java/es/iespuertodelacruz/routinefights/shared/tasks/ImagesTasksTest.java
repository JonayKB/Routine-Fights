package es.iespuertodelacruz.routinefights.shared.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.iespuertodelacruz.routinefights.activity.domain.ports.primary.IActivityService;
import es.iespuertodelacruz.routinefights.communityEvent.domain.ports.primary.ICommunityEventService;
import es.iespuertodelacruz.routinefights.post.domain.ports.primary.IPostService;
import es.iespuertodelacruz.routinefights.shared.services.ImageService;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

class ImagesTasksTest {
    private static final String IMAGE3 = "image3";
    private static final String IMAGE2 = "image2";
    private static final String IMAGE1 = "image1";
    @Mock
    private ImageService imageService;
    @Mock
    private IUserService userService;
    @Mock
    private IPostService postService;
    @Mock
    private ICommunityEventService communityEventService;
    @Mock
    private IActivityService activityService;
    @InjectMocks
    private ImagesTasks imagesTasks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imagesTasks = new ImagesTasks(imageService, userService, postService,communityEventService, activityService);
    }

    @Test
    void deleteNotUsedImagesAllEmptyTest() {
        Set<String> images = new HashSet<String>();
        when(imageService.getAll()).thenReturn(new ArrayList<String>());
        when(userService.findAllImages()).thenReturn(images);
        when(postService.findAllImages()).thenReturn(images);
        List<String> deletedImages = imagesTasks.deleteNotUsedImages();
        assertTrue(deletedImages.isEmpty());
    }

    @Test
    void deleteNotUsedImagesOneTest() {
        List<String> imagesLocal = new ArrayList<String>();
        imagesLocal.add(IMAGE1);
        when(imageService.getAll()).thenReturn(imagesLocal);
        when(userService.findAllImages()).thenReturn(new HashSet<String>());
        when(postService.findAllImages()).thenReturn(new HashSet<String>());
        List<String> deletedImages = imagesTasks.deleteNotUsedImages();
        assertEquals(IMAGE1, deletedImages.get(0));
    }

    @Test
    void deleteNotUsedImagesTwoTest() {
        List<String> imagesLocal = new ArrayList<String>();
        imagesLocal.add(IMAGE1);
        imagesLocal.add(IMAGE2);
        when(imageService.getAll()).thenReturn(imagesLocal);
        when(userService.findAllImages()).thenReturn(new HashSet<String>());
        when(postService.findAllImages()).thenReturn(new HashSet<String>());
        List<String> deletedImages = imagesTasks.deleteNotUsedImages();
        assertEquals(IMAGE1, deletedImages.get(0));
        assertEquals(IMAGE2, deletedImages.get(1));
    }

    @Test
    void deleteNotUsedImagesTwoOneUsedTest() {
        List<String> imagesLocal = new ArrayList<String>();
        imagesLocal.add(IMAGE1);
        imagesLocal.add(IMAGE2);
        imagesLocal.add(IMAGE3);
        Set<String> imagesUsed = new HashSet<String>();
        imagesUsed.add(IMAGE1);
        when(imageService.getAll()).thenReturn(imagesLocal);
        when(userService.findAllImages()).thenReturn(imagesUsed);
        when(postService.findAllImages()).thenReturn(Collections.singleton(IMAGE3));
        List<String> deletedImages = imagesTasks.deleteNotUsedImages();
        assertEquals(IMAGE2, deletedImages.get(0));
        assertEquals(1, deletedImages.size());
    }

}
