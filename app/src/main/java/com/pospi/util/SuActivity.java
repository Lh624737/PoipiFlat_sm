package com.pospi.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.pospi.pai.pospiflat.R;

import java.util.ArrayList;

public class SuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
            Log.d("ROOT", "result:" + new ExecuteAsRoot().execute());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ExecuteAsRoot extends AExecuteAsRoot {

        @Override
        protected ArrayList<String> getCommandsToExecute() {
            ArrayList<String> list = new ArrayList<String>();
            list.add("add kill-server");
            list.add("adb devices");
            return list;
        }
    }
}
