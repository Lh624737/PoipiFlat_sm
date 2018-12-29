package com.pospi.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pospi.pai.yunpos.R;

/**
 * Created by Qiyan on 2016/5/20.
 */
public class CustomDialog extends Dialog {


    public CustomDialog(Context context) {
        super(context);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private View contentView;

        private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }



        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        /**
         * Create the custom dialog
         */
        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context);

            dialog.setCanceledOnTouchOutside(false);
            View layout = inflater.inflate(R.layout.scaner_code, null);
            dialog.addContentView(layout, new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
            // set the confirm button

            final EditText et=(EditText)layout.findViewById(R.id.message);
//            Button sure=(Button)layout.findViewById(R.id.positiveButton);
//
//            sure.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!(et.getText().toString().trim()==null)){
//
//                    }
//                }
//            });




            dialog.setContentView(layout);
            return dialog;
        }

    }

}