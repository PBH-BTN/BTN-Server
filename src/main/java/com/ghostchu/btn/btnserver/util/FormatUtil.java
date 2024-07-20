package com.ghostchu.btn.btnserver.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
    private static final DecimalFormat df = new DecimalFormat("0.00%");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String fileSizeToText(long size) {
        double unit = 1024D, sizeUnit = unit;
        String[] sizes = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i <= sizes.length; i++) {
            if (size < sizeUnit) {
                sizeUnit = sizeUnit / unit;
                return df.format((double) size / (i == 0 ? 1 : sizeUnit)) + sizes[i];
            }
            sizeUnit = sizeUnit * unit;
        }
        return null;
    }

    public static String formatPercent(double d){
        return df.format(d);
    }

    public static DecimalFormat getPercentageFormatter() {
        return df;
    }

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
    public static String formatDate(long l) {
        return dateFormat.format(l);
    }

}
