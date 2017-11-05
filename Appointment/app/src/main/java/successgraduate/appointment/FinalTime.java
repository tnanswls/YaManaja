package successgraduate.appointment;

/**
 * Created by owner on 2017-06-15.
 */

public class FinalTime {
    public FinalTime(String appName, String time) {
    this.appName = appName;
    this.time = time;
}

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    private String appName;
    private String time;

}