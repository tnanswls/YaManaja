package successgraduate.appointment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFriend extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private UserListAdapter adapter;
    private List<User> userList;
    private List<User> saveList;
    private String userID;
    private String appName;
    private AlertDialog dialog;

    private EditText search;
    private InputMethodManager imm;

    private CheckBox selectcheckbox;
    private Button btnok;
    private FloatingActionButton fab;

    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF0092E0)); //액션바색깔

        Intent intent = getIntent();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        search = (EditText)findViewById(R.id.search);


        final ListView listView = (ListView) findViewById(R.id.listview);
        userList = new ArrayList<User>();
        saveList = new ArrayList<User>();
        adapter = new UserListAdapter(getApplicationContext(), userList, this, saveList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            String userID;
            while (count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);
                userID = object.getString("userID");
                User user = new User(userID);
                userList.add(user);
                saveList.add(user);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        EditText search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
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


    public void searchUser(String search) {
        userList.clear();
        for (int i = 0; i < saveList.size(); i++) {
            if (saveList.get(i).getUserID().contains(search)) {
                userList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                CustomDialog.groupName(this,
                        new CustomDialog.CustomDialogListener() {
                            @Override
                            public void onClick(View view) {
                                switch (view.getId()) {
                                    case R.id.button_yes: {
                                        final String appName = (String) view.getTag();

                                        if (appName.equals("")) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "그룹명을 입력해주세요", Toast.LENGTH_SHORT); toast.show();

                                            return;
                                        }

                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {

                                                        Toast.makeText(SearchFriend.this, "그룹 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(getApplicationContext(), Main.class);
                                                        intent.putExtra("appName", appName);
                                                        intent.putExtra("time", "timeset");
                                                        SearchFriend.this.startActivity(intent);

                                                        finish();
                                                    } else {
                                                        Toast.makeText(SearchFriend.this, "그룹 등록에 실패했습니다. ", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        OkayRequest okayRequest = new OkayRequest(appName, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(SearchFriend.this);
                                        queue.add(okayRequest);
                                    }


                                }
                            }

                            @Override
                            public void onDismiss() {
                            }
                        });

            }
        }
    }
}