package com.example.sgmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] itemsAll;
    private ListView mSongsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSongsList = findViewById(R.id.songsList);


        appExternalStoragePermission();

    }

    public void appExternalStoragePermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        displayAudioSongName();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response)
                    {

                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    public ArrayList<File> readOnlyAudio(File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] allFiles = file.listFiles();

        for (File individualsFiles : allFiles){

            if (individualsFiles.isDirectory() && !individualsFiles.isHidden()){
                arrayList.addAll(readOnlyAudio(individualsFiles));
            }
            else
            {
                if (individualsFiles.getName().endsWith(".mp3") || individualsFiles.getName().endsWith(".aac") || individualsFiles.getName().endsWith(".wav") ||individualsFiles.getName().endsWith(".wma")){
                    arrayList.add(individualsFiles);
                }
            }
        }
        return arrayList;
    }


    public void displayAudioSongName(){
        final ArrayList<File> audioSong = readOnlyAudio(Environment.getExternalStorageDirectory());
        itemsAll = new String[audioSong.size()];

        for (int songCounter = 0; songCounter< audioSong.size(); songCounter++)
        {
            itemsAll[songCounter] = audioSong.get(songCounter).getName();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,itemsAll);
        mSongsList.setAdapter(arrayAdapter);
        mSongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName =  mSongsList.getItemAtPosition(position).toString();

                Intent intent = new Intent(MainActivity.this,SmaertPlayerActivity.class);
                intent.putExtra("songs", audioSong);
                intent.putExtra("name", songName);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });
    }

}
