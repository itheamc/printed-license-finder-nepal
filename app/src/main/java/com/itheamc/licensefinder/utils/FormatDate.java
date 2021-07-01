package com.itheamc.licensefinder.utils;

public class FormatDate {

    /**
     * Formating date
     * @param date --- It will take the date in this format --- 15-05-1996
     * @return it will return the date in this format --- 1996-01-15T00:00:00+05:45
     */
    public static String format(String date) {
        String[] splitDate = date.split("-");
        if (splitDate[0].length() == 1) {
            splitDate[0] = "0" + splitDate[0];
        }

        if (splitDate[1].length() == 1) {
            splitDate[1] = "0" + splitDate[1];
        }
        return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0] + "T00:00:00+05:45";
    }


    /**
     * Formating date
     * @param date --- It will take the date in this format --- 15-Jan-1996
     * @return it will return the date in this format --- 1996-01-15T00:00:00+05:45
     */
    public static String format0(String date) {
        String[] splitDate = date.split("-");
        return splitDate[2] + "-" + getMonthInNumber(splitDate[1].toLowerCase()) + "-" + splitDate[0] + "T00:00:00+05:45";
    }


    /**
     * Formating date
     * @param date --- It will take the date in this format --- 15-Jan-1996
     * @return it will return the date in this format --- 1/15/19
     */
    public static String format1(String date) {
        String[] splitDate = date.split("-");
        String m = getMonthInNumber(splitDate[1].toLowerCase());
        String d = splitDate[0];

        if (d.startsWith("0")) {
            d = d.replace("0", "");
        }

        if (m.startsWith("0")) {
            m = m.replace("0", "");
        }

        return m + "/" + d + "/" + splitDate[2].substring(2);
    }



    /**
     * Formating date
     * @param date --- It will take the date in this format --- 15-Jan-1996
     * @return it will return the date in this format --- 15-01-1996
     */
    public static String format2(String date) {
        String [] splits = date.split("-");
        String month = getMonthInNumber(splits[1].toLowerCase());
        return splits[0] + "-" + month + "-" + splits[2];
    }

    // Function to identify month
    private static String getMonthInNumber(String month) {
        switch (month) {
            case "jan":
                return "01";
            case "feb":
                return "02";
            case "mar":
                return "03";
            case "apr":
                return "04";
            case "may":
                return "05";
            case "jun":
                return "06";
            case "jul":
                return "07";
            case "aug":
                return "08";
            case "sep":
                return "09";
            case "oct":
                return "10";
            case "nov":
                return "11";
            case "dec":
                return "12";
        }
        return "00";
    }
}
