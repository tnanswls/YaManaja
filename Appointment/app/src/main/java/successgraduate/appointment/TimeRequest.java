package successgraduate.appointment;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by owner on 2017-06-09.
 */

public class TimeRequest extends StringRequest{
    final static private String URL = "http://zzcandy.cafe24.com/Times.php";
    private Map<String, String> parameters;

    public TimeRequest(String userID, String appName, String time,Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("time",time);
        parameters.put("appName",appName);
           }
    public  Map<String,String> getParams(){
        return parameters;
    }

}
