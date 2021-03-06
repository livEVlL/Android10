package com.org.iii.android10;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView tv;
    private File sdroot,approot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.textTv);
        sp = getSharedPreferences("gamedata",MODE_PRIVATE);
        editor = sp.edit();

        sdroot = Environment.getExternalStorageDirectory();
        Log.v("AB", sdroot.getAbsolutePath());

        // 以下是檢查危險權限
        if ( ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
        }else {
            init();
        }
    }

    private void init(){
        approot = new File(sdroot, "Android/data/" + getPackageName());
        if (!approot.exists()) approot.mkdirs();
    }

    // call back
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int grantResult : grantResults){
            if (grantResult == PackageManager.PERMISSION_GRANTED){
                Log.v("AB","OK");
            }
        }
    }

    // 偏好設定 => save.
    public void test1(View v){
        editor.putInt("stage", 3);
        editor.putString("user", "Brad");
        editor.commit();
        Toast.makeText(this, "Save OK", Toast.LENGTH_SHORT).show();
    }
    // 偏好設定 => read.
    public void test2(View v){
        int stage = sp.getInt("stage",0);
        String name = sp.getString("user","nobody");
        tv.setText("Stage: " + stage + "\n" +
                "User: " + name);
    }
    //內存write
    public void test3(View v){
        try {
            FileOutputStream fout = openFileOutput("Data",MODE_APPEND);
            fout.write("Hi,there\nLOL\nDota2\nPPPP123\n123456\n".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"Save OK",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.v("AB","test3() :"+ e.toString());
        }
    }
    //內存read
    public void test4(View v){
        tv.setText("");
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput("Data")));
            String line;
            while ((line = reader.readLine()) != null){
                tv.append(line + "\n");
            }
            reader.close();
        } catch (IOException e) {
            Log.v("AB","test4() :"+ e.toString());
        }
    }
    //
    public void test5(){
        try {
            FileOutputStream fout = new FileOutputStream(
                    new File(approot,"test1.txt"));
            fout.write("Hi,there".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"save1 OK",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.v("AB","test5():" + e.toString());
        }
    }
    public void test6(){
        try {
            FileOutputStream fout = new FileOutputStream(
                    new File(sdroot,"test1.txt"));
            fout.write("Hi,there".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"save1 OK",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.v("AB","test6():" + e.toString());
        }
    }
    public void test7(){
        tv.setText("");
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(new File(approot,"fail1.txt"))
            );
            String line;
            while ( (line = reader.readLine()) != null){
                tv.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {
            Log.v("AB","test7():" + e.toString());
        }
    }
}