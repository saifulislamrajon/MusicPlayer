package com.example.saiful.musicplayer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView ivPlaylist;
    String[] item;
    final int REQUEST_IMAGE_CALL = 1;
    ArrayList<File> mySongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivPlaylist = (ListView) findViewById(R.id.lvPlaylist);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_CALL);
            } else {
//                list = imageReader(Environment.getExternalStorageDirectory());
//                list = imageReader(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
                mySongs = findsong(Environment.getExternalStorageDirectory());
            }
        } else {
//            list = imageReader(Environment.getExternalStorageDirectory());
//            list = imageReader(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
            mySongs = findsong(Environment.getExternalStorageDirectory());

        }

        item = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            //Toast.makeText(getApplicationContext(),mySongs.get(i).getName().toString(),Toast.LENGTH_SHORT).show();
            item[i] = mySongs.get(i).getName().toString().replace(",mp3", "");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, item);
        ivPlaylist.setAdapter(adp);

        ivPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", position).putExtra("songlist", mySongs));
            }
        });
    }

    public ArrayList<File> findsong(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findsong(singleFile));

            } else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".3gp")) {
                    al.add(singleFile);
                }
            }
        }
        return al;
    }
}
