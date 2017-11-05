package successgraduate.appointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CheckTime extends AppCompatActivity {
    private boolean saveLoginData;
    String userID;
    String loginId;

    private Button timeFinalbtn;
    private SharedPreferences appData;


    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);

        userID = appData.getString("userID", "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_time);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF0092E0)); //액션바색깔

        Intent intent = getIntent();

        final String sDate = intent.getStringExtra("sDate");
        final String eDate = intent.getStringExtra("eDate");
        final String appName = intent.getStringExtra("appName");

        Button timeFinalbtn = (Button) findViewById(R.id.timeFinalbtn);

        TextView Date = (TextView) findViewById(R.id.startDate);
        TextView PeriodDate = (TextView) findViewById(R.id.dateText);
        String twoDate = sDate + " ~ " + eDate + "\n";

        PeriodDate.setText(twoDate);

        TableLayout tl = (TableLayout) findViewById(R.id.store_table);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            final Date beginDate = formatter.parse(sDate);
            final Date endDate = formatter.parse(eDate);

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            long diff = endDate.getTime() - beginDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            String message = appName;
            Date.setText(message);

            TableRow t = new TableRow(this);
            t.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tl.addView(t, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            /* Create a new row to be added. */
            //현재 버튼의 텍스트를 스트링값으로 버튼클릭시마다 넣고 싶음
            for (int i = 0; i <= diffDays; i++) {
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView guide = new TextView(this);
                guide.setText((beginDate.getDate() + i) + "\n일");
                guide.setLayoutParams(new TableRow.LayoutParams(32,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tr.addView(guide);

                       /* Create a Button to be the row-content. */
                final Button b = new Button(this);
                b.setText(
                        new StringBuilder()
                                .append(beginDate.getYear() + 1900).append("-")
                                .append(beginDate.getMonth() + 1).append("-")
                                .append((beginDate.getDate() + i) + " 10").append(":00 ~ "));
                b.setTextColor(Color.rgb(214, 215, 215));
                b.setLayoutParams(new TableRow.LayoutParams(104,
                        104));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String btime = "" + b.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
                                        b.setTextColor(Color.rgb(255, 182, 193));
                                        b.setBackgroundColor(Color.rgb(255, 182, 193));
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTime.this);
                                        builder.setMessage("등록에 실패했습니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        TimeRequest timeRequest = new TimeRequest(userID, appName, btime, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(CheckTime.this);
                        queue.add(timeRequest);
                    }
                });

                /* Add Button to row. */
                tr.addView(b);

                final Button c = new Button(this);
                c.setTextColor(Color.rgb(214, 215, 215));
                c.setText(new StringBuilder()
                        .append(beginDate.getYear() + 1900).append("-")
                        .append(beginDate.getMonth() + 1).append("-")
                        .append((beginDate.getDate() + i) + " 12").append(":00 ~ "));
                c.setLayoutParams(new TableRow.LayoutParams(104,
                        104));
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String ctime = "" + c.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
                                        c.setTextColor(Color.rgb(249, 249, 176));
                                        c.setBackgroundColor(Color.rgb(249, 249, 176));
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTime.this);
                                        builder.setMessage("등록에 실패했습니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        TimeRequest timeRequest = new TimeRequest(userID, appName, ctime, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(CheckTime.this);
                        queue.add(timeRequest);
                    }
                });
                       /* Add Button to row. */
                tr.addView(c);

                final Button d = new Button(this);
                d.setTextColor(Color.rgb(214, 215, 215));
                d.setText(new StringBuilder()
                        .append(beginDate.getYear() + 1900).append("-")
                        .append(beginDate.getMonth() + 1).append("-")
                        .append((beginDate.getDate() + i) + " 14").append(":00 ~ "));
                d.setLayoutParams(new TableRow.LayoutParams(104,
                        104));
                d.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String dtime = "" + d.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
                                        d.setTextColor(Color.rgb(102, 205, 170));
                                        d.setBackgroundColor(Color.rgb(102, 205, 170));
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTime.this);
                                        builder.setMessage("등록에 실패했습니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        TimeRequest timeRequest = new TimeRequest(userID, appName, dtime, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(CheckTime.this);
                        queue.add(timeRequest);
                    }
                });
                /* Add Button to row. */
                tr.addView(d);

                final Button e = new Button(this);
                e.setTextColor(Color.rgb(214, 215, 215));
                e.setText(new StringBuilder()
                        .append(beginDate.getYear() + 1900).append("-")
                        .append(beginDate.getMonth() + 1).append("-")
                        .append((beginDate.getDate() + i) + " 16").append(":00 ~ "));
                e.setLayoutParams(new TableRow.LayoutParams(104,
                        104));
                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String etime = "" + e.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
                                        e.setTextColor(Color.rgb(135, 206, 250));
                                        e.setBackgroundColor(Color.rgb(135, 206, 250));
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTime.this);
                                        builder.setMessage("등록에 실패했습니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        TimeRequest timeRequest = new TimeRequest(userID, appName, etime, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(CheckTime.this);
                        queue.add(timeRequest);
                    }
                });
                       /* Add Button to row. */
                tr.addView(e);

                final Button f = new Button(this);
                f.setTextColor(Color.rgb(214, 215, 215));
                f.setText(new StringBuilder()
                        .append(beginDate.getYear() + 1900).append("-")
                        .append(beginDate.getMonth() + 1).append("-")
                        .append((beginDate.getDate() + i) + " 18").append(":00 ~ "));
                f.setLayoutParams(new TableRow.LayoutParams(104,
                        104));
                f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String ftime = "" + f.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
                                        f.setTextColor(Color.rgb(170, 162, 219));
                                        f.setBackgroundColor(Color.rgb(170, 162, 219));
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTime.this);
                                        builder.setMessage("등록에 실패했습니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        TimeRequest timeRequest = new TimeRequest(userID, appName, ftime, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(CheckTime.this);
                        queue.add(timeRequest);
                    }
                });
                       /* Add Button to row. */
                tr.addView(f);

                final Button g = new Button(this);
                g.setTextColor(Color.rgb(214, 215, 215));
                g.setText(new StringBuilder()
                        .append(beginDate.getYear() + 1900).append("-")
                        .append(beginDate.getMonth() + 1).append("-")
                        .append((beginDate.getDate() + i) + " 20").append(":00 ~ "));
                g.setLayoutParams(new TableRow.LayoutParams(104,
                        104));
                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String gtime = "" + g.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
                                        g.setTextColor(Color.rgb(221, 160, 221));
                                        g.setBackgroundColor(Color.rgb(221, 160, 211));
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckTime.this);
                                        builder.setMessage("등록에 실패했습니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        TimeRequest timeRequest = new TimeRequest(userID, appName, gtime, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(CheckTime.this);
                        queue.add(timeRequest);
                    }
                });
                       /* Add Button to row. */
                tr.addView(g);

                       /* Add row to TableLayout. */
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeFinalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();

            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://zzcandy.cafe24.com/Final2.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine())!= null)
                {
                    stringBuilder.append(temp + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {
            Intent intent = new Intent(CheckTime.this, FinalActivity.class);
            intent.putExtra("timeList", result);

            CheckTime.this.startActivity(intent);
            finish();
        }
    }
}