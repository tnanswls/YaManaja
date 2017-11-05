package successgraduate.appointment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FinalActivity extends AppCompatActivity {

    private ListView finallistview;
    private FinalListAdapter adapter;
    private List<FinalTime> timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent intent = getIntent();

        finallistview = (ListView) findViewById(R.id.finallistview);
        timeList = new ArrayList<FinalTime>();
        adapter = new FinalListAdapter(getApplicationContext(), timeList);
        finallistview.setAdapter(adapter);

        Button btnbtn = (Button) findViewById(R.id.btnbtn);


        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("timeList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            String appName, time;
            while (count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);
                appName = object.getString("appName");
                time = object.getString("time");
                FinalTime finalTime = new FinalTime(appName, time);
                timeList.add(finalTime);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF0092E0)); //액션바색깔



        btnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                final String appName = intent.getStringExtra("appName");
                Intent intent_act = new Intent(getApplicationContext(), Maps.class);
                intent_act.putExtra("appName", appName);
                startActivity(intent_act);
            }
        });
    }
}

