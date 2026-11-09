package es.iespuertodelacruz.routinefights.shared.dto;

/**
 * Crop chosen in the app's editor. The rectangle is expressed as fractions
 * (0..1) of the picture <em>after</em> applying its EXIF orientation and the
 * extra clockwise {@code rotation}, so it does not depend on the resolution the
 * client saw.
 *
 * @param x        left edge, 0..1
 * @param y        top edge, 0..1
 * @param width    width, 0..1
 * @param height   height, 0..1
 * @param rotation clockwise rotation in degrees: 0, 90, 180 or 270
 */
public record CropRequest(double x, double y, double width, double height, int rotation) {

    public CropRequest {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(width) || !Double.isFinite(height)) {
            throw new IllegalArgumentException("Invalid crop");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid crop size");
        }
        int normalized = ((rotation % 360) + 360) % 360;
        if (normalized % 90 != 0) {
            throw new IllegalArgumentException("Rotation must be a multiple of 90");
        }
        rotation = normalized;
    }

    /** Builds a request from optional form fields; {@code null} when nothing was sent. */
    public static CropRequest of(Double x, Double y, Double width, Double height, Integer rotation) {
        boolean hasRect = x != null && y != null && width != null && height != null;
        int rot = rotation == null ? 0 : rotation;
        if (!hasRect && rot == 0) {
            return null;
        }
        return hasRect ? new CropRequest(x, y, width, height, rot) : new CropRequest(0, 0, 1, 1, rot);
    }
}
