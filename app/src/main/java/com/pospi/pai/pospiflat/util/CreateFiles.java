package com.pospi.pai.pospiflat.util;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by acer on 2017/8/3.
 */

public class CreateFiles {
            public static String path = android.os.Environment.getExternalStorageDirectory().getPath() +"/storecode/";
              
            //创建文件夹及文件  
            public void CreateText(String name) throws IOException {
                    File file = new File(path);
                    if (!file.exists()) {  
                            try {  
                                    //按照指定的路径创建文件夹  
                                    file.mkdirs();  
                                } catch (Exception e) {  
                                    // TODO: handle exception  
                                }  
                        }  
                    File dir = new File(path+name);
                    if (!dir.exists()) {  
                              try {  
                                      //在指定的文件夹中创建文件  
                                      dir.createNewFile();  
                                } catch (Exception e) {  
                                }  
                        }  
              
                }  
              
            //向已指定的文件中写入数据
            public void write(String name ,String data) {
                    FileWriter fw = null;
                    BufferedWriter bw = null;
                    String datetime = "";  
                    try {  
                            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "
                                    + "hh:mm:ss");  
                            datetime = tempDate.format(new java.util.Date()).toString();  
                            fw = new FileWriter(path + name, true);//
                            // 创建FileWriter对象，用来写入字符流  
                            bw = new BufferedWriter(fw); // 将缓冲对文件的输出  
                            String myreadline = datetime ;
                              
                            bw.append(data+"\n"); // 写入文件
//                            bw.newLine();
                            bw.flush(); // 刷新该流的缓冲  
                            bw.close();  
                            fw.close();  
                        } catch (IOException e) {  
                            // TODO Auto-generated catch block  
                            e.printStackTrace();  
                            try {  
                                    bw.close();  
                                    fw.close();  
                                } catch (IOException e1) {  
                                    // TODO Auto-generated catch block  
                            }
                    }
            }
    public int filesNum() {
        File file = new File(path);
        Log.i("file", file.list().length + "");
        return file.list().length;
    }
}
