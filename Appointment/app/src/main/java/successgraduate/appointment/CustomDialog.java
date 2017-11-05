package successgraduate.appointment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


/**
 * Created by Lee jh on 2017-06-14.
 */

public class CustomDialog extends Dialog {
    /*****************************************************************************
     * Interfaces
     *****************************************************************************/
    public interface CustomDialogListener {
        public void onClick(View view);

        public void onDismiss();
    }

    public interface CustomDateDialogListener {
        public void onClick(CustomDialog dialog, DatePicker picker);

        public void onDismiss();
    }

    /*****************************************************************************
     * Constructor
     *****************************************************************************/
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    /*****************************************************************************
     * Inner Functions & Classes
     *****************************************************************************/
    private static CustomDialog setDefault(
            final Activity activity,
            int layoutId) {
        CustomDialog dialog = new CustomDialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutId);
        dialog.setCanceledOnTouchOutside(false);     // it dismiss the dialog when click outside the dialog frame
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private static void setButtonClickListener(
            final Dialog dialog,
            int id,
            final CustomDialogListener listener,
            final String tag) {
        ((Button) dialog.findViewById(id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    v.setTag(tag);
                    listener.onClick(v);
                }

                dialog.dismiss();
            }
        });
    }

    /*****************************************************************************
     * Methods
     *****************************************************************************/


    public static CustomDialog groupName(
            final Activity activity,
            final CustomDialogListener listener) {
        if (activity.isFinishing())
            return null;

        final CustomDialog dialog = setDefault(activity, R.layout.part_pop_group_name);

        setButtonClickListener(dialog, R.id.button_yes, listener, null);
        setButtonClickListener(dialog, R.id.button_no, listener, null);

        ((View)dialog.findViewById(R.id.button_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ((EditText) dialog.findViewById(R.id.edittext)).getText().toString();

                if (listener != null) {
                    v.setTag(content);
                    listener.onClick(v);
                }

                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        return dialog;
    }

    public static CustomDialog showYesNo(
            final Activity activity,
            final CustomDialogListener listener) {
        if (activity.isFinishing())
            return null;

        final CustomDialog dialog = setDefault(activity, R.layout.part_pop_yesno);

        dialog.setCancelable(false);

        setButtonClickListener(dialog, R.id.button_yes, listener, "Y");
        setButtonClickListener(dialog, R.id.button_no, listener, "N");

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        return dialog;
    }
}