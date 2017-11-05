package successgraduate.appointment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends AppCompatActivity implements View.OnClickListener {

    private TextView welcomeMessage;
    private String mTime;
    private ImageButton mTimeSet;

    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF0092E0)); //액션바색깔

        welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        mTimeSet = (ImageButton)findViewById(R.id.timeset);

        Intent intent = getIntent();
        final String appName = intent.getStringExtra("appName");

        mTime = intent.getStringExtra("time");

        if(mTime.equals("timeset")){
            scaleView(welcomeMessage, 10f, 1f);
        }


        String message = appName;

        welcomeMessage.setText(message);

    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                0f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "'뒤로'버튼을한번더누르시면종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:{
                CustomDialog.showYesNo(Main.this,
                        new CustomDialog.CustomDialogListener() {
                            @Override
                            public void onClick(View view) {
                                if (((String) view.getTag()).equals("Y")) {
                                    Intent intent = new Intent(Main.this, LoginActivity.class);
                                    startActivity(intent);
                                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = auto.edit();

                                    editor.clear();
                                    editor.commit();

                                    Toast.makeText(Main.this, "로그아웃되었습니다." , Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            }

                            @Override
                            public void onDismiss() {
                            }
                        });

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeset: {
                Intent intent = getIntent();
                final String appName = intent.getStringExtra("appName");

                Intent intent_act = new Intent(getApplicationContext(), MainTime.class);
                intent_act.putExtra("appName", appName);
                startActivity(intent_act);
            }
            break;

            case R.id.placeset: {
                Intent intent_act = new Intent(getApplicationContext(), Maps.class);
                startActivity(intent_act);
            }
            break;
        }
    }
}

