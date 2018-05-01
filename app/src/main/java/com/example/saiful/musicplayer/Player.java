package com.example.saiful.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    Uri uri;

    SeekBar seekBar;
    Thread updateSeekBar;
    Button btPlay, btFF, btNxt, btFV, btPv;
    TextView show;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (Button) findViewById(R.id.btPlay);
        btFF = (Button) findViewById(R.id.btFF);
        btNxt = (Button) findViewById(R.id.btNxt);
        btFV = (Button) findViewById(R.id.btFV);
        btPv = (Button) findViewById(R.id.btPv);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btFV.setOnClickListener(this);
        btPv.setOnClickListener(this);

        show = (TextView) findViewById(R.id.show);
        editText = (EditText) findViewById(R.id.editText);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread() {
            @Override
            public void run() {
                super.run();
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                //seekBar.setMax(totalDuration);
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if (mp != null) {
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);
        uri = Uri.parse(mySongs.get(position).toString());
        show.setText(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.start();
        seekBar.setMax(mp.getDuration());
        updateSeekBar.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btPlay:
                if (mp.isPlaying()) {
                    btPlay.setText(">");
                    mp.pause();
                } else {
                    btPlay.setText("||");
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            case R.id.btFV:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position + 1) % mySongs.size();
                uri = Uri.parse(mySongs.get(position).toString());
                show.setText(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                uri = Uri.parse(mySongs.get(position).toString());
                show.setText(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;
        }
    }

    public void search(View view) {
        String search_song = editText.getText().toString();
        while (mySongs.equals(search_song)) {
            uri = Uri.parse(mySongs.get(position).toString());
            show.setText(mySongs.get(position).toString());
            mp = MediaPlayer.create(getApplicationContext(), uri);
            mp.start();

        }
    }
}
