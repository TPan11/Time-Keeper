package com.btbsolutions.timekeeper.utility;

import com.btbsolutions.timekeeper.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    private int adImage[] = {R.drawable.btb_logo_ad,
            R.drawable.jmpant_ad,
            R.drawable.btb_logo_ad};

    private String emailRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    private String passwordRegex = "^(?=.*\\w)(?=.*\\d).{6,}$";

    private Pattern emailPattern = Pattern.compile(emailRegex);
    private Pattern passwordPattern = Pattern.compile(passwordRegex);

    private static List<String> allTasks = new ArrayList<>();
    public static List<String> getAllTasks(){
        return allTasks;
    }

    public static void addTask(String task){
        allTasks.add(task);
    }

    private static ToDo copyTodo=null;

    public String stringArrayDateToStringDate(String[] arrayDate){
        String date = arrayDate[0];
        date += arrayDate[1];
        date += arrayDate[2];
        return date;
    }

    public String[] stringDateToStringArrayDate(String date){
        String[] arrayDate;
        arrayDate = date.split("/");
        return arrayDate;
    }

    public String timestampToString(long timestamp, int mode){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        String str, day, month, year;

        if(mode == 0){
            year = Integer.toString(cal.get(Calendar.YEAR));
            month = cal.get(Calendar.MONTH)<9?"0"+(cal.get(Calendar.MONTH)+1):""+cal.get(Calendar.MONTH)+1;
            day = cal.get(Calendar.DAY_OF_MONTH)<10?"0"+cal.get(Calendar.DAY_OF_MONTH):""+cal.get(Calendar.DAY_OF_MONTH);
            str = year+"-"+month+"-"+day;
        }
        else{
            year = Integer.toString(cal.get(Calendar.YEAR));
            month = cal.get(Calendar.MONTH)<9?"0"+(cal.get(Calendar.MONTH)+1):""+cal.get(Calendar.MONTH)+1;
            day = cal.get(Calendar.DAY_OF_MONTH)<10?"0"+cal.get(Calendar.DAY_OF_MONTH):""+cal.get(Calendar.DAY_OF_MONTH);

            String hour = cal.get(Calendar.HOUR)<10?"0"+cal.get(Calendar.HOUR):""+cal.get(Calendar.HOUR);
            String min = cal.get(Calendar.MINUTE)<10?"0"+cal.get(Calendar.MINUTE):""+cal.get(Calendar.MINUTE);
            String sec = cal.get(Calendar.SECOND)<10?"0"+cal.get(Calendar.SECOND):""+cal.get(Calendar.SECOND);
            str = year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;
        }
        return str;
    }

    public long stringtoTimestamp(String date){
        Timestamp timestamp = Timestamp.valueOf(date);
        return timestamp.getTime();
    }

    public boolean validateEmail(String email){
        Matcher matcher = emailPattern.matcher(email);

        return matcher.matches();
    }

    public boolean validatePassword(String email){
        Matcher matcher = passwordPattern.matcher(email);

        return matcher.matches();
    }

    public String addZeroIfLessThan9(int num){
        if(num<10){
            return "0"+num;
        }
        else{
            return String.valueOf(num);
        }
    }

    public int getAdImage(int num){
        return adImage[num];
    }
}
