package successgraduate.appointment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OkayRequest extends StringRequest {

    final static private String URL = "http://zzcandy.cafe24.com/Okay.php";
    private Map<String, String> parameters;

    public OkayRequest(String appName, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("appName", appName);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}