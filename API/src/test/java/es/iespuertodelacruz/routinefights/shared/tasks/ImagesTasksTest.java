package es.iespuertodelacruz.routinefights.shared.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @InjectMocks
    private ImagesTasks imagesTasks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imagesTasks = new ImagesTasks(imageService, userService, postService);
    }

    @Test
    void deleteNotUsedImagesAllEmptyTest() {
        List<String> images = new ArrayList<String>();
        when(imageService.getAll()).thenReturn(images);
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
        when(userService.findAllImages()).thenReturn(new ArrayList<String>());
        when(postService.findAllImages()).thenReturn(new ArrayList<String>());
        List<String> deletedImages = imagesTasks.deleteNotUsedImages();
        assertEquals(IMAGE1, deletedImages.get(0));
    }

    @Test
    void deleteNotUsedImagesTwoTest() {
        List<String> imagesLocal = new ArrayList<String>();
        imagesLocal.add(IMAGE1);
        imagesLocal.add(IMAGE2);
        when(imageService.getAll()).thenReturn(imagesLocal);
        when(userService.findAllImages()).thenReturn(new ArrayList<String>());
        when(postService.findAllImages()).thenReturn(new ArrayList<String>());
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
        List<String> imagesUsed = new ArrayList<String>();
        imagesUsed.add(IMAGE1);
        when(imageService.getAll()).thenReturn(imagesLocal);
        when(userService.findAllImages()).thenReturn(imagesUsed);
        when(postService.findAllImages()).thenReturn(Collections.singletonList(IMAGE3));
        List<String> deletedImages = imagesTasks.deleteNotUsedImages();
        assertEquals(IMAGE2, deletedImages.get(0));
        assertEquals(1, deletedImages.size());
    }

}
