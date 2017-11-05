package successgraduate.appointment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Lee jh on 2017-05-14.
 */

public class UserListAdapter extends BaseAdapter {


    private String[] userID;
    boolean[] itemSelected;
    private Context context;
    private List<User> userList;
    private Activity parentActivity;
    private List<User> saveList;
    private CheckBox selectcheckbox;

    public UserListAdapter(Context context, List<User> userList, Activity parentActivity, List<User> saveList) {
        this.context = context;
        this.userList = userList;
        this.parentActivity = parentActivity;
        this.saveList = saveList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup viewGroup) {

        final View v = View.inflate(context, R.layout.user, null);
        final TextView userID = (TextView) v.findViewById(R.id.userID);
        final CheckBox selectcheckbox = (CheckBox) v.findViewById(R.id.selectcheckbox);

        userID.setText(userList.get(position).getUserID());


        userID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (selectcheckbox.isChecked()) {
                    selectcheckbox.setChecked(false);
                    itemSelected[position] = false;
                } else {
                    selectcheckbox.setChecked(true);
                    itemSelected[position] = true;
                }
            }
        });

        v.setTag(userList.get(position).getUserID());

        selectcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean isChecked = jsonResponse.getBoolean("isChecked");
                            if (isChecked) {
                                itemSelected[position] = true;
                            } else {
                                itemSelected[position] = false;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                SelectRequest selectRequest = new SelectRequest(userID.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                queue.add(selectRequest);

                userList.get(position).setSelected(buttonView.isChecked());
                saveList.get(position).setSelected(buttonView.isChecked());
            }

        });

        selectcheckbox.setChecked(userList.get(position).isSelected());
        selectcheckbox.setChecked(saveList.get(position).isSelected());

        return v;
    }
}