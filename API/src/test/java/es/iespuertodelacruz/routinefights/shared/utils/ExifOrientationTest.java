package es.iespuertodelacruz.routinefights.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ExifOrientationTest {

    /** SOI + APP1(Exif, big endian TIFF, one IFD entry: Orientation = value) + EOI. */
    private static byte[] jpegWithOrientation(int value, boolean little) {
        byte[] tiff = little
                ? new byte[] { 'I', 'I', 42, 0, 8, 0, 0, 0, 1, 0, 0x12, 0x01, 3, 0, 1, 0, 0, 0, (byte) value, 0, 0, 0,
                        0, 0, 0, 0, 0 }
                : new byte[] { 'M', 'M', 0, 42, 0, 0, 0, 8, 0, 1, 0x01, 0x12, 0, 3, 0, 0, 0, 1, 0, (byte) value, 0, 0,
                        0, 0, 0, 0 };
        int length = 2 + 6 + tiff.length;
        byte[] out = new byte[2 + 2 + length + 2];
        int p = 0;
        out[p++] = (byte) 0xFF;
        out[p++] = (byte) 0xD8;
        out[p++] = (byte) 0xFF;
        out[p++] = (byte) 0xE1;
        out[p++] = (byte) (length >> 8);
        out[p++] = (byte) length;
        for (byte b : new byte[] { 'E', 'x', 'i', 'f', 0, 0 }) {
            out[p++] = b;
        }
        System.arraycopy(tiff, 0, out, p, tiff.length);
        p += tiff.length;
        out[p++] = (byte) 0xFF;
        out[p] = (byte) 0xD9;
        return out;
    }

    @Test
    void readsBothByteOrders() {
        assertEquals(6, ExifOrientation.read(jpegWithOrientation(6, false)));
        assertEquals(8, ExifOrientation.read(jpegWithOrientation(8, true)));
    }

    @Test
    void defaultsToNormal() {
        assertEquals(1, ExifOrientation.read(new byte[] { 1, 2, 3 }));
        assertEquals(1, ExifOrientation.read(new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xD9 }));
        assertEquals(1, ExifOrientation.read(jpegWithOrientation(42, false)));
    }

    @Test
    void skipsOtherSegmentsAndStopsAtScan() {
        // SOI, APP0 (4 bytes long), then start of scan.
        byte[] jfif = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 4, 0, 0, (byte) 0xFF, (byte) 0xDA, 0,
                2 };
        assertEquals(1, ExifOrientation.read(jfif));
        // Garbage where a marker is expected.
        assertEquals(1, ExifOrientation.read(new byte[] { (byte) 0xFF, (byte) 0xD8, 0x12, 0x34, 0, 0 }));
        // A segment longer than the data.
        assertEquals(1, ExifOrientation.read(new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x7F,
                0x7F }));
    }

    @Test
    void ignoresExifWithoutOrientationOrTruncated() {
        byte[] full = jpegWithOrientation(6, false);
        // Bytes 22-23 hold the tag id of the only IFD entry.
        byte[] other = full.clone();
        other[23] = 0x10;
        assertEquals(1, ExifOrientation.read(other));
        // More entries than the segment holds, none of them Orientation.
        byte[] lying = other.clone();
        lying[21] = 9;
        assertEquals(1, ExifOrientation.read(lying));
        // IFD offset pointing past the segment.
        byte[] farIfd = full.clone();
        farIfd[19] = 0x7F;
        assertEquals(1, ExifOrientation.read(farIfd));
        // An APP1 segment with another signature, such as XMP, is skipped.
        byte[] xmp = full.clone();
        xmp[6] = 'X';
        assertEquals(1, ExifOrientation.read(xmp));
    }
}
