package de.h2cl.kiezbrueller.utils;

public final class Utils {

    public static int coordinatesToPos(Integer x, Integer y) {
        return y * 2 + x + 1;
    }

    public static String calcSongName(int position) {
        if (position > 9) {
            return "010song.mp3";
        } else {
            return "00" + position + "song.mp3";
        }
    }

    public static String humanReadableByteCount(long bytes) {
        int unit = 1000;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "kMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
