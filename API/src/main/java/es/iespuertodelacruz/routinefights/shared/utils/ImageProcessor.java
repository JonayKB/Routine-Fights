package es.iespuertodelacruz.routinefights.shared.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import es.iespuertodelacruz.routinefights.shared.dto.CropRequest;
import es.iespuertodelacruz.routinefights.shared.exceptions.ImageUploadException;

/**
 * Decoding, orientation, cropping, high quality downscaling and encoding of
 * pictures. Everything is plain JDK (java.awt / ImageIO).
 */
public final class ImageProcessor {

    /** Refuse anything bigger than this before decoding (decompression bombs). */
    public static final long MAX_PIXELS = 50_000_000L;
    /** Very large photos are subsampled while decoding to save memory. */
    private static final int DECODE_TARGET_SIDE = 4096;

    private ImageProcessor() {
    }

    /** Result of decoding: the upright picture and whether it uses transparency. */
    public record Decoded(BufferedImage image, boolean alpha) {
    }

    /**
     * Decodes {@code data}, validating that it really is an image and that it is
     * not absurdly large, and applies its EXIF orientation.
     */
    public static Decoded decode(byte[] data) {
        try (ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(data))) {
            Iterator<ImageReader> readers = input == null ? null : ImageIO.getImageReaders(input);
            if (readers == null || !readers.hasNext()) {
                throw new ImageUploadException("File is not a supported image");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(input, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0 || (long) width * height > MAX_PIXELS) {
                    throw new ImageUploadException("Image resolution is too large");
                }
                ImageReadParam param = reader.getDefaultReadParam();
                int step = Math.max(1, Math.max(width, height) / DECODE_TARGET_SIDE);
                if (step > 1) {
                    param.setSourceSubsampling(step, step, 0, 0);
                }
                BufferedImage image = reader.read(0, param);
                boolean alpha = image.getColorModel().hasAlpha() && hasTransparentPixels(image);
                BufferedImage upright = orient(toStandard(image, alpha), ExifOrientation.read(data));
                return new Decoded(upright, alpha);
            } finally {
                reader.dispose();
            }
        } catch (IOException | RuntimeException e) {
            if (e instanceof ImageUploadException ue) {
                throw ue;
            }
            throw new ImageUploadException("File is not a valid image");
        }
    }

    /** Applies the user's rotation and crop. */
    public static BufferedImage crop(BufferedImage image, CropRequest crop) {
        if (crop == null) {
            return image;
        }
        BufferedImage rotated = rotate(image, crop.rotation());
        int w = rotated.getWidth();
        int h = rotated.getHeight();
        int x = clamp((int) Math.round(crop.x() * w), 0, w - 1);
        int y = clamp((int) Math.round(crop.y() * h), 0, h - 1);
        int cw = clamp((int) Math.round(crop.width() * w), 1, w - x);
        int ch = clamp((int) Math.round(crop.height() * h), 1, h - y);
        if (x == 0 && y == 0 && cw == w && ch == h) {
            return rotated;
        }
        BufferedImage out = new BufferedImage(cw, ch, rotated.getType());
        Graphics2D g = out.createGraphics();
        g.drawImage(rotated, -x, -y, null);
        g.dispose();
        return out;
    }

    /**
     * Shrinks {@code image} so its longest side is at most {@code maxSide}, never
     * enlarging it. Halves repeatedly with bilinear filtering and finishes with a
     * bicubic step, which keeps details sharp without the aliasing of a single
     * big jump.
     */
    public static BufferedImage fit(BufferedImage image, int maxSide) {
        int w = image.getWidth();
        int h = image.getHeight();
        int longest = Math.max(w, h);
        if (longest <= maxSide) {
            return image;
        }
        double ratio = (double) maxSide / longest;
        int targetW = Math.max(1, (int) Math.round(w * ratio));
        int targetH = Math.max(1, (int) Math.round(h * ratio));

        BufferedImage current = image;
        while (current.getWidth() / 2 >= targetW && current.getHeight() / 2 >= targetH) {
            current = draw(current, current.getWidth() / 2, current.getHeight() / 2,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        if (current.getWidth() != targetW || current.getHeight() != targetH) {
            current = draw(current, targetW, targetH, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        }
        return current;
    }

    /**
     * Writes {@code image} as PNG when it has transparency, otherwise as a
     * progressive JPEG with the given quality.
     */
    public static void write(BufferedImage image, boolean alpha, float quality, OutputStream out) throws IOException {
        if (alpha) {
            ImageIO.write(image, "png", out);
            return;
        }
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        try (ImageOutputStream output = ImageIO.createImageOutputStream(out)) {
            writer.setOutput(output);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            // Progressive JPEGs are usually smaller and render top-down blurry → sharp.
            if (Math.max(image.getWidth(), image.getHeight()) > 200) {
                param.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
            }
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    public static String extension(boolean alpha) {
        return alpha ? "png" : "jpg";
    }

    // ------------------------------------------------------------------ helpers

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static boolean hasTransparentPixels(BufferedImage image) {
        if (image.getAlphaRaster() == null) {
            return false;
        }
        int w = image.getWidth();
        int h = image.getHeight();
        // Sampling a grid is enough to tell a cut-out from an opaque PNG.
        int stepX = Math.max(1, w / 64);
        int stepY = Math.max(1, h / 64);
        for (int y = 0; y < h; y += stepY) {
            for (int x = 0; x < w; x += stepX) {
                if ((image.getRGB(x, y) >>> 24) != 0xFF) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Converts to INT_RGB / INT_ARGB so drawing and JPEG encoding behave. */
    private static BufferedImage toStandard(BufferedImage image, boolean alpha) {
        int type = alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        if (image.getType() == type) {
            return image;
        }
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), type);
        Graphics2D g = out.createGraphics();
        if (!alpha) {
            // Flatten semi-transparent edges onto white instead of black.
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
        }
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return out;
    }

    private static BufferedImage draw(BufferedImage src, int w, int h, Object interpolation) {
        BufferedImage out = new BufferedImage(w, h, src.getType());
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    /** Applies an EXIF orientation value (1..8). */
    static BufferedImage orient(BufferedImage image, int orientation) {
        return switch (orientation) {
            case 2 -> flipHorizontal(image);
            case 3 -> rotate(image, 180);
            case 4 -> flipHorizontal(rotate(image, 180));
            case 5 -> flipHorizontal(rotate(image, 90));
            case 6 -> rotate(image, 90);
            case 7 -> flipHorizontal(rotate(image, 270));
            case 8 -> rotate(image, 270);
            default -> image;
        };
    }

    /** Rotates clockwise by 0, 90, 180 or 270 degrees. */
    static BufferedImage rotate(BufferedImage image, int degrees) {
        int d = ((degrees % 360) + 360) % 360;
        if (d == 0) {
            return image;
        }
        int w = image.getWidth();
        int h = image.getHeight();
        boolean swap = d == 90 || d == 270;
        BufferedImage out = new BufferedImage(swap ? h : w, swap ? w : h, image.getType());
        AffineTransform t = new AffineTransform();
        switch (d) {
            case 90 -> t.translate(h, 0);
            case 180 -> t.translate(w, h);
            default -> t.translate(0, w);
        }
        t.rotate(Math.toRadians(d));
        Graphics2D g = out.createGraphics();
        g.drawImage(image, t, null);
        g.dispose();
        return out;
    }

    private static BufferedImage flipHorizontal(BufferedImage image) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = out.createGraphics();
        g.drawImage(image, image.getWidth(), 0, -image.getWidth(), image.getHeight(), null);
        g.dispose();
        return out;
    }
}
