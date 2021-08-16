package com.uts.pertemuan9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class InsertView extends AppCompatActivity {
    private EditText editFilename, editCatatan;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_view);
        editFilename = (EditText) findViewById(R.id.editFilename);
        editCatatan = (EditText) findViewById(R.id.editCatatan);

        btnSimpan = (Button) findViewById(R.id.button);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });
    }

    private void create() {
        String state = Environment.getExternalStorageState();
        String path = Environment.getExternalStorageDirectory().toString() + "/files";
        if(! Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File parent = new File(path);
        FileOutputStream outputStream = null;

        if(parent.exists()) {
            File file = new File(path, editFilename.getText().toString());

            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                OutputStreamWriter streamwriter = new OutputStreamWriter(outputStream);
                streamwriter.append(editCatatan.getText().toString());
                streamwriter.flush();
                streamwriter.close();
                outputStream.flush();
                outputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            parent.mkdir();
            File file = new File(path, editFilename.getText().toString());
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);
                outputStream.write(editCatatan.getText().toString().getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}