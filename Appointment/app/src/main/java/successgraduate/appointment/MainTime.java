package successgraduate.appointment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainTime extends AppCompatActivity implements View.OnClickListener {

    private InputMethodManager imm;
    private LinearLayout ll;
    private TextView mpickStartDate;
    private TextView mpickEndDate;
    private View tempv1;
    private View tempv2;

    private Button okay;
    private int sYear, sMonth, sDay;
    private int eYear, eMonth, eDay;

    static final int DATE1_DIALOG_ID = 0;
    static final int DATE2_DIALOG_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_time);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF0092E0)); //액션바색깔


        okay = (Button) findViewById(R.id.okay);
        mpickStartDate = (TextView) findViewById(R.id.pickStartDate);
        mpickEndDate = (TextView) findViewById(R.id.pickEndDate);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Init();
    }

    public void Init() {
        final Calendar c1 = Calendar.getInstance();
        sYear = c1.get(c1.YEAR);
        sMonth = c1.get(c1.MONTH);
        sDay = c1.get(c1.DAY_OF_MONTH);

        final Calendar c2 = Calendar.getInstance();
        eYear = c2.get(c2.YEAR);
        eMonth = c2.get(c2.MONTH);
        eDay = c2.get(c2.DAY_OF_MONTH);
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE1_DIALOG_ID:
                return new DatePickerDialog(this, sDateSetListener, sYear, sMonth, sDay);

            case DATE2_DIALOG_ID:
                return new DatePickerDialog(this, eDateSetListener, eYear, eMonth, eDay);
        }
        return null;
    }

    private void updateDisplay(TextView s, TextView e) {

        if (s != null) {
            s.setText(
                    new StringBuilder()
                            .append(sYear).append("-")
                            .append(sMonth + 1).append("-")
                            .append(sDay).append("")
            );
        } else {
            if (sYear < eYear) {
                e.setText(
                        new StringBuilder()
                                .append(eYear).append("-")
                                .append(eMonth + 1).append("-")
                                .append(eDay).append("")
                );
            } else if (sMonth < eMonth && sYear == eYear) {
                e.setText(
                        new StringBuilder()
                                .append(eYear).append("-")
                                .append(eMonth + 1).append("-")
                                .append(eDay).append("")
                );
            } else if (sDay <= eDay && sYear == eYear && sMonth == eMonth) {
                e.setText(
                        new StringBuilder()
                                .append(eYear).append("-")
                                .append(eMonth + 1).append("-")
                                .append(eDay).append("")
                );
            } else
                Toast.makeText(getApplicationContext(), "시작일보다 이후날짜를 선택해주세요!", Toast.LENGTH_SHORT).show();
        }

    }

    private DatePickerDialog.OnDateSetListener sDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            sYear = year;
            sMonth = month;
            sDay = dayOfMonth;
            if (tempv1.getId() == R.id.pickStartDate)
                updateDisplay(mpickStartDate, null);
        }
    };

    private DatePickerDialog.OnDateSetListener eDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            eYear = year;
            eMonth = month;
            eDay = dayOfMonth;
            if (tempv2.getId() == R.id.pickEndDate)
                updateDisplay(null, mpickEndDate);
        }
    };

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(mpickStartDate.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mpickEndDate.getWindowToken(), 0);
    }


    // 날짜가 yyyymmdd 형식으로 입력되었을 경우 Date로 변경하는 메서드
    public Date transformDate(String date) {
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");

        // Date로 변경하기 위해서는 날짜 형식을 yyyy-mm-dd로 변경해야 한다.
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");

        java.util.Date tempDate = null;

        try {
            // 현재 yyyymmdd로된 날짜 형식으로 java.util.Date객체를 만든다.
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // java.util.Date를 yyyy-mm-dd 형식으로 변경하여 String로 반환한다.
        String transDate = afterFormat.format(tempDate);

        // 반환된 String 값을 Date로 변경한다.
        Date d = Date.valueOf(transDate);

        return d;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okay: {
                String sDate = mpickStartDate.getText().toString();
                String eDate = mpickEndDate.getText().toString();

                Intent intent_act = getIntent();
                String appName = intent_act.getStringExtra("appName");

                if (sDate.equals("") && eDate.equals("")) {
                    Toast.makeText(getApplicationContext(), "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainTime.this, CheckTime.class);
                intent.putExtra("sDate", sDate);
                intent.putExtra("eDate", eDate);
                intent.putExtra("appName", appName);
                MainTime.this.startActivity(intent);
                finish();

            }
            break;

            case R.id.pickStartDate: {
                tempv1 = v;
                showDialog(DATE1_DIALOG_ID);
            }
            break;

            case R.id.pickEndDate: {
                tempv2 = v;
                showDialog(DATE2_DIALOG_ID);
            }
            break;
        }


    }
}

