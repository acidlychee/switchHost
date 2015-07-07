package com.mi.jr.qa;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liming on 2015/6/26.
 */
public class FileOperator {
    private Context context;
    private Process process;

    final String DATA_SYSTEM="/data/system";
    final String STAGING_FILE=DATA_SYSTEM+"/server_staging";
    final String PREVIEW_FILE=DATA_SYSTEM+"/xiaomi_account_preview";


    public FileOperator(Context context) {
        this.context = context;
    }

    public void addHostFile(String fileName, String fileContent) {

        try {
            FileOutputStream os = context.openFileOutput(fileName, context.MODE_PRIVATE);
            os.write(fileContent.getBytes());
            os.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public String getFileContent(String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            return EncodingUtils.getString(buffer, "UTF-8");
        } catch (FileNotFoundException e) {
            new File(fileName);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void switchHost(String fileName) {
        try {
            String hostPath = context.getFilesDir() + "/" + fileName;
            String cmd = "cp -f " + hostPath + " " + "/system/etc/hosts\n";
            String cmd2 = "mount -o remount,rw /system\n";
            process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write(cmd2.getBytes());
            process.getOutputStream().write(cmd.getBytes());
            process.getOutputStream().write("exit\n".getBytes());
            process.waitFor();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public String getCurrentHostContent() {
        try {
            String HostPath = Environment.getRootDirectory() + "/" + "etc/hosts";
            File file = new File(HostPath);
            FileInputStream inStream = new FileInputStream(file);
            byte[] bite = new byte[inStream.available()];
            inStream.read(bite);
            return new String(bite, "UTF-8");
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public boolean isRoot() {
        try {
            process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write("exit\n".getBytes());
            process.getOutputStream().flush();
            int i = process.waitFor();
            if (0 == i) {
                process = Runtime.getRuntime().exec("su");
                return true;
            }

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    public void deleteFile(String filePath){
        String cmd = "rm -f "+filePath+"\n";

        File file = new File(filePath);
        if (!file.exists()){
            return;
        }
        try {
            process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write(cmd.getBytes());
            process.getOutputStream().write("exit\n".getBytes());
            process.waitFor();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void createFile(String filePath){
        String cmd = "touch "+filePath+"\n";
        try {
            process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write(cmd.getBytes());
            process.getOutputStream().write("exit\n".getBytes());
            process.waitFor();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
