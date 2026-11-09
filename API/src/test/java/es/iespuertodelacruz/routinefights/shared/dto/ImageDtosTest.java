package es.iespuertodelacruz.routinefights.shared.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ImageDtosTest {

    @Test
    void cropOfNothingIsNull() {
        assertNull(CropRequest.of(null, null, null, null, null));
        assertNull(CropRequest.of(null, null, null, null, 0));
        assertNull(CropRequest.of(0.1, null, 0.5, 0.5, null));
    }

    @Test
    void rotationOnlyKeepsWholePicture() {
        assertEquals(new CropRequest(0, 0, 1, 1, 90), CropRequest.of(null, null, null, null, 90));
    }

    @Test
    void fullCropAndRotationNormalized() {
        CropRequest crop = CropRequest.of(0.1, 0.2, 0.3, 0.4, -90);
        assertEquals(0.1, crop.x());
        assertEquals(0.4, crop.height());
        assertEquals(270, crop.rotation());
        assertEquals(0, CropRequest.of(0.0, 0.0, 1.0, 1.0, 720).rotation());
    }

    @Test
    void rejectsInvalidCrops() {
        assertThrows(IllegalArgumentException.class, () -> new CropRequest(Double.NaN, 0, 1, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> new CropRequest(0, Double.POSITIVE_INFINITY, 1, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> new CropRequest(0, 0, Double.NaN, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> new CropRequest(0, 0, 1, Double.NaN, 0));
        assertThrows(IllegalArgumentException.class, () -> new CropRequest(0, 0, 0, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> new CropRequest(0, 0, 1, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new CropRequest(0, 0, 1, 1, 45));
    }

    @Test
    void parsesSizes() {
        assertNull(ImageSize.parse(null));
        assertNull(ImageSize.parse(" "));
        assertNull(ImageSize.parse("original"));
        assertEquals(ImageSize.MEDIUM, ImageSize.parse("Medium"));
        assertEquals("tiny", ImageSize.TINY.key());
        assertThrows(IllegalArgumentException.class, () -> ImageSize.parse("huge"));
    }
}
