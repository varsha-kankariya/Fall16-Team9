package prachihada.eatfoodv2;

/**
 * Created by Azhad on 12/1/2016.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DateUtil {
    //    private static final String[] timeFormats = {"HH:mm:ss","HH:mm"};
    private static final String[] dateSeparators = {"/","-"," ",""};

    private static String finalDateFormat = "";
    private static final String MDY_FORMAT = "MMM{sep}dd{sep}yy";
    private static final String MDYY_FORMAT = "MMM{sep}dd{sep}yyyy";
    private static final String MMDDYY_FORMAT = "MM{sep}dd{sep}yy";
    private static final String DDMMMYY_FORMAT = "dd{sep}MMM{sep}yy";

    private static final String dmy_template = ".*\\w{3}{sep}\\d{2}{sep}\\d{2}.*";
    private static final String ymd_template = ".*\\w{3}{sep}\\d{2}{sep}\\d{4}.*";
    private static final String mmddyy_template = ".*\\d{2}{sep}\\d{2}{sep}\\d{2}.*";
    private static final String ddmmmyy_template = ".*\\d{2}{sep}\\w{3}{sep}\\d{2}.*";

    public static Date stringToDate(String input){
        Date date = null;
        String dateFormat = getDateFormat(input);
        if(dateFormat == null){
            //throw new IllegalArgumentException("Date is not in an accepted format " + input);
            return new Date();
        }

        Pattern pattern = Pattern.compile(finalDateFormat.replace(".*", ""));
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            input = matcher.group();
        }
        else {
            return new Date();
        }

        for(String sep : dateSeparators){
            String actualDateFormat = patternForSeparator(dateFormat, sep);
//            //try first with the time
//            for(String time : timeFormats){
//                date = tryParse(input,actualDateFormat + " " + time);
//                if(date != null){
//                    return date;
//                }
//            }
            //didn't work, try without the time formats
            date = tryParse(input,actualDateFormat);
            if(date != null){
                return date;
            }
        }

        return new Date();
    }

    private static String getDateFormat(String date){
        for(String sep : dateSeparators){
            String ymdPattern = patternForSeparator(ymd_template, sep);
            String dmyPattern = patternForSeparator(dmy_template, sep);
            String mmddyyPattern = patternForSeparator(mmddyy_template, sep);
            String ddmmmyyPattern = patternForSeparator(ddmmmyy_template, sep);
            if(date.matches(ymdPattern)){
                finalDateFormat = ymdPattern;
                return MDYY_FORMAT;
            }
            if(date.matches(dmyPattern)){
                finalDateFormat = dmyPattern;
                return MDY_FORMAT;
            }
            if(date.matches(mmddyyPattern)){
                finalDateFormat = mmddyyPattern;
                return MMDDYY_FORMAT;
            }
            if(date.matches(ddmmmyyPattern)){
                finalDateFormat = ddmmmyyPattern;
                return DDMMMYY_FORMAT;
            }
        }
        return null;
    }

    private static String patternForSeparator(String template, String sep){
        return template.replace("{sep}", sep);
    }

    public static Date tryParse(String input, String pattern){
        try{
            return new SimpleDateFormat(pattern).parse(input);
        }
        catch (ParseException e) {}
        return null;
    }
}
