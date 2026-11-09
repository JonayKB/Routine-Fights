package es.iespuertodelacruz.routinefights.shared.utils;

/**
 * Minimal reader for the EXIF "Orientation" tag (0x0112) of a JPEG.
 * <p>
 * Java's ImageIO ignores EXIF orientation, so photos taken in portrait on many
 * phones would come out sideways once re-encoded. Only the first IFD is
 * inspected, which is where cameras write the tag.
 */
public final class ExifOrientation {

    public static final int NORMAL = 1;

    private ExifOrientation() {
    }

    /**
     * @return the orientation (1..8) or {@link #NORMAL} when absent or unreadable.
     */
    public static int read(byte[] data) {
        try {
            return parse(data);
        } catch (RuntimeException e) {
            return NORMAL;
        }
    }

    private static int parse(byte[] d) {
        if (d.length < 4 || (d[0] & 0xFF) != 0xFF || (d[1] & 0xFF) != 0xD8) {
            return NORMAL;
        }
        int pos = 2;
        while (pos + 4 <= d.length) {
            if ((d[pos] & 0xFF) != 0xFF) {
                return NORMAL;
            }
            int marker = d[pos + 1] & 0xFF;
            // Start of scan / end of image: no more metadata segments.
            if (marker == 0xDA || marker == 0xD9) {
                return NORMAL;
            }
            int length = ((d[pos + 2] & 0xFF) << 8) | (d[pos + 3] & 0xFF);
            int segment = pos + 4;
            if (marker == 0xE1 && length >= 8 && segment + 6 <= d.length
                    && d[segment] == 'E' && d[segment + 1] == 'x' && d[segment + 2] == 'i'
                    && d[segment + 3] == 'f' && d[segment + 4] == 0 && d[segment + 5] == 0) {
                return readTiff(d, segment + 6, Math.min(d.length, pos + 2 + length));
            }
            pos += 2 + length;
        }
        return NORMAL;
    }

    private static int readTiff(byte[] d, int tiff, int end) {
        boolean little = d[tiff] == 'I' && d[tiff + 1] == 'I';
        int ifd = tiff + readInt(d, tiff + 4, little);
        if (ifd + 2 > end) {
            return NORMAL;
        }
        int entries = readShort(d, ifd, little);
        for (int i = 0; i < entries; i++) {
            int entry = ifd + 2 + i * 12;
            if (entry + 12 > end) {
                return NORMAL;
            }
            if (readShort(d, entry, little) == 0x0112) {
                int value = readShort(d, entry + 8, little);
                return value >= 1 && value <= 8 ? value : NORMAL;
            }
        }
        return NORMAL;
    }

    private static int readShort(byte[] d, int at, boolean little) {
        return little
                ? (d[at] & 0xFF) | ((d[at + 1] & 0xFF) << 8)
                : ((d[at] & 0xFF) << 8) | (d[at + 1] & 0xFF);
    }

    private static int readInt(byte[] d, int at, boolean little) {
        return little
                ? (d[at] & 0xFF) | ((d[at + 1] & 0xFF) << 8) | ((d[at + 2] & 0xFF) << 16) | ((d[at + 3] & 0xFF) << 24)
                : ((d[at] & 0xFF) << 24) | ((d[at + 1] & 0xFF) << 16) | ((d[at + 2] & 0xFF) << 8) | (d[at + 3] & 0xFF);
    }
}
