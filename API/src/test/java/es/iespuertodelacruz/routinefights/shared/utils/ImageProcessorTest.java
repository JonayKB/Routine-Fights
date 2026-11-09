package es.iespuertodelacruz.routinefights.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.shared.dto.CropRequest;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageUploadException;

class ImageProcessorTest {

    /** 2x1 picture: red pixel on the left, blue on the right. */
    private static BufferedImage redBlue() {
        BufferedImage img = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, Color.RED.getRGB());
        img.setRGB(1, 0, Color.BLUE.getRGB());
        return img;
    }

    private static boolean isRed(int rgb) {
        Color c = new Color(rgb);
        return c.getRed() > 200 && c.getBlue() < 50;
    }

    private static byte[] png(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }

    @Test
    void appliesEveryExifOrientation() {
        assertTrue(isRed(ImageProcessor.orient(redBlue(), 2).getRGB(1, 0)));
        assertTrue(isRed(ImageProcessor.orient(redBlue(), 3).getRGB(1, 0)));
        assertTrue(isRed(ImageProcessor.orient(redBlue(), 4).getRGB(0, 0)));
        BufferedImage transposed = ImageProcessor.orient(redBlue(), 5);
        assertEquals(1, transposed.getWidth());
        assertTrue(isRed(transposed.getRGB(0, 0)));
        assertTrue(isRed(ImageProcessor.orient(redBlue(), 6).getRGB(0, 0)));
        assertTrue(isRed(ImageProcessor.orient(redBlue(), 7).getRGB(0, 1)));
        assertTrue(isRed(ImageProcessor.orient(redBlue(), 8).getRGB(0, 1)));
        BufferedImage same = redBlue();
        assertSame(same, ImageProcessor.orient(same, 1));
    }

    @Test
    void cropWithoutRequestOrFullFrameKeepsImage() {
        BufferedImage img = redBlue();
        assertSame(img, ImageProcessor.crop(img, null));
        assertSame(img, ImageProcessor.crop(img, new CropRequest(0, 0, 1, 1, 0)));
    }

    @Test
    void cropClampsOutOfRangeValues() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        BufferedImage out = ImageProcessor.crop(img, new CropRequest(0.9, 0.9, 5, 5, 0));
        assertEquals(10, out.getWidth());
        assertEquals(10, out.getHeight());
    }

    @Test
    void rotatesAHundredAndEightyDegrees() {
        BufferedImage out = ImageProcessor.crop(redBlue(), new CropRequest(0, 0, 1, 1, 180));
        assertTrue(isRed(out.getRGB(1, 0)));
    }

    @Test
    void fitNeverUpscales() {
        BufferedImage img = new BufferedImage(10, 20, BufferedImage.TYPE_INT_RGB);
        assertSame(img, ImageProcessor.fit(img, 50));
        BufferedImage small = ImageProcessor.fit(new BufferedImage(1000, 300, BufferedImage.TYPE_INT_RGB), 100);
        assertEquals(100, small.getWidth());
        assertEquals(30, small.getHeight());
    }

    @Test
    void decodeRejectsGarbageAndEmptyInput() {
        byte[] empty = new byte[0];
        byte[] text = "hello".getBytes();
        byte[] truncated = { (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0, 0 };
        assertThrows(ImageUploadException.class, () -> ImageProcessor.decode(empty));
        assertThrows(ImageUploadException.class, () -> ImageProcessor.decode(text));
        assertThrows(ImageUploadException.class, () -> ImageProcessor.decode(truncated));
    }

    @Test
    void decodeSubsamplesHugePictures() throws IOException {
        byte[] data = png(new BufferedImage(9000, 10, BufferedImage.TYPE_INT_RGB));
        ImageProcessor.Decoded decoded = ImageProcessor.decode(data);
        assertEquals(4500, decoded.image().getWidth());
        assertFalse(decoded.alpha());
    }

    @Test
    void decodeConvertsOpaqueArgbAndKeepsRealTransparency() throws IOException {
        BufferedImage opaque = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                opaque.setRGB(x, y, 0xFF00FF00);
            }
        }
        ImageProcessor.Decoded decoded = ImageProcessor.decode(png(opaque));
        assertFalse(decoded.alpha());
        assertEquals(BufferedImage.TYPE_INT_RGB, decoded.image().getType());

        BufferedImage clear = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
        assertTrue(ImageProcessor.decode(png(clear)).alpha());
    }

    @Test
    void writesJpegAndPng() throws IOException {
        ByteArrayOutputStream jpg = new ByteArrayOutputStream();
        ImageProcessor.write(new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB), false, 0.5f, jpg);
        assertTrue(jpg.size() > 0);
        ByteArrayOutputStream png = new ByteArrayOutputStream();
        ImageProcessor.write(new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB), true, 0.5f, png);
        assertTrue(png.size() > 0);
        assertEquals("png", ImageProcessor.extension(true));
        assertEquals("jpg", ImageProcessor.extension(false));
    }
}
