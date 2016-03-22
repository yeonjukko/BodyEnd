package net.yeonjukko.bodyend.model;

/**
 * Created by yeonjukko on 16. 3. 20..
 */
public class WaterAlarmInfoModel {

    private int waterAlarmStatus;
    private int waterAlarmPeriod;
    private int alarmTimezoneStart;
    private int alarmTimezoneStop;


    public int getWaterAlarmStatus() {
        return waterAlarmStatus;
    }

    public void setWaterAlarmStatus(int waterAlarmStatus) {
        this.waterAlarmStatus = waterAlarmStatus;
    }

    public int getWaterAlarmPeriod() {
        return waterAlarmPeriod;
    }

    public void setWaterAlarmPeriod(int waterAlarmPeriod) {
        this.waterAlarmPeriod = waterAlarmPeriod;
    }

    public int getAlarmTimezoneStart() {
        return alarmTimezoneStart;
    }

    public void setAlarmTimezoneStart(int alarmTimezoneStart) {
        this.alarmTimezoneStart = alarmTimezoneStart;
    }

    public int getAlarmTimezoneStop() {
        return alarmTimezoneStop;
    }

    public void setAlarmTimezoneStop(int alarmTimezoneStop) {
        this.alarmTimezoneStop = alarmTimezoneStop;
    }
}
