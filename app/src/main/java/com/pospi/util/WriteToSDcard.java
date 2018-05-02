package com.pospi.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by acer on 2017/4/14.
 */

public class WriteToSDcard {
    private Context context;


    public WriteToSDcard(Context context) {
        this.context = context;
    }

    public void write(String folder , String files) {
        String filePath = android.os.Environment.getExternalStorageDirectory().getPath() + "/miyajpos/miyaapay/"+folder;
        File f = new File(filePath + "/"+files);
        if (f.exists()) {
            Log.i("miya", "成功");
            return;
        }
        InputStream inputStream;
        try {
            inputStream = context.getResources().getAssets().open("miyaapay/"+folder+"/"+files);
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/"+files);
            byte[] buffer = new byte[512];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            System.out.println("success");
            Log.i("miya", "成功");
            // Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
