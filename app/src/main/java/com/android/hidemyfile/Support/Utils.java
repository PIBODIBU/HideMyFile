package com.android.hidemyfile.Support;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
    public static String bytesToHumanReadable(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String convertMsToDate(String milliSeconds) {
        String dateFormat = "dd.MM.yyyy hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String convertMsToDate(String milliSeconds, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return simpleDateFormat.format(calendar.getTime());
    }
}
