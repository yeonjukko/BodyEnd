package net.yeonjukko.bodyend.libs;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yeonjukko on 16. 3. 15..
 */
public class DayCounter {

    public int dayCounter(int d_day) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Log.d("mox4","="+d_day);

            long dbDate = format.parse(d_day+"").getTime();


            Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            //Calendar dday = Calendar.getInstance();
            //dday.setTimeInMillis(d_day);            // D-day의 날짜를 입력합니다.

            long day = dbDate/86400000;
            // 각각 날의 시간 값을 얻어온 다음
            //( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )

            long tday = today.getTimeInMillis()/86400000;
            long count = day - tday; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            return (int) count+1; // 날짜는 하루 + 시켜줘야합니다.
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
