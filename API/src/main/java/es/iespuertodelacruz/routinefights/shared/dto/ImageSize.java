package es.iespuertodelacruz.routinefights.shared.dto;

import java.util.Locale;

/**
 * Sizes every uploaded image is stored in. The number is the longest side in
 * pixels; images are never upscaled.
 */
public enum ImageSize {
    /** Blurred placeholder shown while the real picture loads (~1 KB). */
    TINY(32, 0.5f),
    SMALL(320, 0.78f),
    MEDIUM(720, 0.8f),
    LARGE(1440, 0.82f);

    private final int maxSide;
    private final float quality;

    ImageSize(int maxSide, float quality) {
        this.maxSide = maxSide;
        this.quality = quality;
    }

    public int getMaxSide() {
        return maxSide;
    }

    public float getQuality() {
        return quality;
    }

    public String key() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * @return the size matching {@code value} (case-insensitive) or {@code null}
     *         for "original"/blank, meaning the full stored image.
     */
    public static ImageSize parse(String value) {
        if (value == null || value.isBlank() || value.equalsIgnoreCase("original")) {
            return null;
        }
        for (ImageSize size : values()) {
            if (size.name().equalsIgnoreCase(value)) {
                return size;
            }
        }
        throw new IllegalArgumentException("Unknown image size: " + value);
    }
}
