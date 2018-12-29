package com.pospi.util;

import android.util.Log;

import com.pospi.pai.yunpos.util.CreateFiles;
import com.pospi.util.constant.Config;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by acer on 2017/8/7.
 * 上传到ftp服务器
 */

public class CommonUtil {
    public String ftpUpload(Config config, ArrayList<String> fileLists) {

        FTPClient ftpClient = new FTPClient();
        FileInputStream fis;
        String returnMessage = "Recived message from ftp";

        try {
            ftpClient.connect(config.getHostname(), config.getPort());
            boolean isLogin = ftpClient.login(config.getUsername(), config.getPasswd());
            int replyCode = ftpClient.getReplyCode();
            if (isLogin && FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.changeWorkingDirectory("/share");
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();

                for (String filepath : fileLists) {
                    File f = new File(filepath);
                    fis = new FileInputStream(f);
                    String name = f.getName();
                    Log.i("ftp", name);
                    boolean isUpload = ftpClient.storeFile(name, fis);
                    if (isUpload) {
                        f.renameTo(new File(CreateFiles.path+"C10122"+ GetData.getDate()+".txt"));
                    }
                }
                returnMessage = "1";
            }
            else {
                returnMessage = "2";
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            returnMessage = "3";
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return returnMessage;
    }
}
