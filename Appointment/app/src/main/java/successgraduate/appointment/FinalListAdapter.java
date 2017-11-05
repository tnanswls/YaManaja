package successgraduate.appointment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Lee jh on 2017-06-16.
 */

public class FinalListAdapter extends BaseAdapter {

    private Context context;
    private List<FinalTime> timeList;

    public FinalListAdapter(Context context, List<FinalTime> timeList) {
        this.context = context;
        this.timeList = timeList;
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public Object getItem(int i) {
        return timeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.finaltime, null);
        TextView appName = (TextView) v.findViewById(R.id.appName);
        TextView time = (TextView) v.findViewById(R.id.time);

        appName.setText(timeList.get(i).getAppName());
        time.setText(timeList.get(i).getTime());

        v.setTag(timeList.get(i).getAppName());


        return v;
    }
}