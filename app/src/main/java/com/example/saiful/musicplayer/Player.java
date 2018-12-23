package com.example.saiful.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    int pos = 0;
//    It's searchView Work
    SearchView searchView;
    ListView listView2;
    ArrayAdapter<String> adapter;
    String[] itm;
    Toolbar toolbar;
    ImageButton pButton;

//    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        toolbar= (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
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

        /*autoCompleteTextView= (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,mySongs);
        autoCompleteTextView.setAdapter(adapter);*/
        /*editText = (EditText) findViewById(R.id.editText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == event.ACTION_DOWN) && (keyCode == event.KEYCODE_ENTER)) {
                    String songName = editText.getText().toString().toLowerCase();
//                    Toast.makeText(getApplicationContext(),"Under Construction",Toast.LENGTH_SHORT).show();
                    for (File f : mySongs) {
                        String fName = f.getName().toLowerCase().replace(".mp3", "");
                        pos += 1;
                        if (fName.equals(songName)) {
                            if (mp != null) {
                                mp.stop();
                                mp.release();
                            }
                            position = 0;
                            uri = Uri.parse(mySongs.get(position = pos - 1).toString());
                            show.setText(mySongs.get(position = pos - 1).toString());
                            mp = MediaPlayer.create(getApplicationContext(), uri);
                            mp.start();

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Player.this);
                            alertDialogBuilder.setMessage(fName);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
//                            Toast.makeText(getApplicationContext(),"it's true",Toast.LENGTH_SHORT).show();

                            break;
                        }
//                        Toast.makeText(getApplicationContext(), " " + pos, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),f.getName(),Toast.LENGTH_SHORT).show();
//                        pos+=1;
                    }
                    ////
                    return true;
                }
                return false;
            }
        });*/

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
                        try {
                            currentPosition = mp.getCurrentPosition();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
//        It's searchView work
        itm=new String[mySongs.size()];
        for(int j=0;j<mySongs.size();j++){
            itm[j]=mySongs.get(j).getName().toString();
        }
        searchView= (SearchView) findViewById(R.id.searchView);
        listView2= (ListView) findViewById(R.id.listView2);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itm);
        listView2.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionn, long id) {
                Toast.makeText(Player.this, " "+parent.getItemAtPosition(positionn)+" "+positionn, Toast.LENGTH_SHORT).show();
                String uName= (String) parent.getItemAtPosition(positionn);
                for(File f:mySongs){
                    String sName=f.getName();
                    pos += 1;
                    if(sName.equals(uName)){
                        if (mp != null) {
                            mp.stop();
                            mp.release();
                        }
                        try{
                            position = 0;
                            uri = Uri.parse(mySongs.get(position = pos - 1).toString());
                            show.setText(mySongs.get(position = pos - 1).toString());
                            mp = MediaPlayer.create(getApplicationContext(), uri);
                            mp.start();
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }

                    }
                }
            }

        });
        pButton= (ImageButton) findViewById(R.id.pButton);
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Player.this,MainActivity.class));
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

    /*public void search(View view) {
        *//*Toast.makeText(Player.this, "it's search option", Toast.LENGTH_SHORT).show();
        String search_song = editText.getText().toString();
        while (mySongs.equals(search_song)) {
            uri = Uri.parse(mySongs.get(position).toString());
            show.setText(mySongs.get(position).toString());
            mp = MediaPlayer.create(getApplicationContext(), uri);
            mp.start();

        }*//*
        String songName = editText.getText().toString().toLowerCase();
//                    Toast.makeText(getApplicationContext(),"Under Construction",Toast.LENGTH_SHORT).show();
        for (File f : mySongs) {
            String fName = f.getName().toLowerCase().replace(".mp3", "");
            pos += 1;
            if (fName.equals(songName)) {
                if (mp != null) {
                    mp.stop();
                    mp.release();
                }
                position = 0;

                uri = Uri.parse(mySongs.get(position = pos - 1).toString());
                show.setText(mySongs.get(position = pos - 1).toString());
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.start();

                            *//*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Player.this);
                            alertDialogBuilder.setMessage(fName);
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();*//*
//                            Toast.makeText(getApplicationContext(),"it's true",Toast.LENGTH_SHORT).show();

                break;
            }
//            Toast.makeText(getApplicationContext(), " " + pos, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),f.getName(),Toast.LENGTH_SHORT).show();
//                        pos+=1;
        }
    }*/
}
