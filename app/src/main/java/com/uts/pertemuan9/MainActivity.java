package com.uts.pertemuan9;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_STORAGE = 100;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= 23) {
            if(IzinPenyimpanan()) {
                takeList();
            }
        } else {

        }
    }

    public boolean IzinPenyimpanan() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeList();
                }
            break;
        }
    }

    private void takeList() {
        String path = Environment.getExternalStorageDirectory().toString() + "/files";
        File dir = new File(path);
        if(dir.exists()) {
            File[] files = dir.listFiles();
            String[] filename = new String[files.length];
            String[] filedate = new String[files.length];
            SimpleDateFormat sdate = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
            ArrayList<Map<String,String>> itemdataList = new ArrayList<>();
            for (int i=0; i<files.length; i++) {
                filename[i] = files[i].getName();
                Date lastMod = new Date(files[i].lastModified());
                filedate[i] = sdate.format(lastMod);
                Map<String,String> itemMap = new HashMap<>();
                itemMap.put("Name", filename[i]);
                itemMap.put("Last Modified", filedate[i]);
            }

            SimpleAdapter simpleadapter = new SimpleAdapter(this, itemdataList, android.R.layout.simple_list_item_2, new String[]{"Name", "Last Modified"}, new int[]{android.R.id.text1, android.R.id.text1});
            listview.setAdapter(simpleadapter);
            simpleadapter.notifyDataSetChanged();
        }
    }
}