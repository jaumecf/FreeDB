package com.example.freedb.BSO;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.freedb.BSO;
import com.example.freedb.BBDD.InterficieBBDD;
import com.example.freedb.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AfegeixBSO extends YouTubeBaseActivity  implements View.OnClickListener {

    private InterficieBBDD bd;
    private EditText editTitol, editAutor, editDuracio, editData,editLink;
    private Button btnAfegir, btnTorna;
    private YouTubePlayerView youtubeView;
    private BSO bso = null;
    private String GOOGLE_API_KEY = "AIzaSyDZ7MLFQ2R7Ru6dBceOcxOIc5M-VhhIW_k";
    private String YOUTUBE_VIDEO_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegeix_b_s_o);

        editTitol = findViewById(R.id.titol);
        editAutor = findViewById(R.id.autor);
        editDuracio = findViewById(R.id.duracio);
        editData = findViewById(R.id.data);
        editLink = findViewById(R.id.link);
        btnAfegir = (Button) findViewById(R.id.confirma);
        btnTorna = (Button) findViewById(R.id.torna);
        youtubeView = findViewById(R.id.youtube_player);

        btnAfegir.setOnClickListener(this);
        btnTorna.setOnClickListener(this);
        editData.setOnClickListener(this);
        editLink.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                YOUTUBE_VIDEO_ID = expandUrl(editLink.getText().toString());
                Log.d("ID","ID: "+YOUTUBE_VIDEO_ID);
                publicaVideo(YOUTUBE_VIDEO_ID, youtubeView);
            }

        });


    }


    @Override
    public void onClick(View v) {
        if (v == btnAfegir){
            bd = new InterficieBBDD(this);
            bd.obre();
            bso = generaBSO();

            if (bso == null) {
                Toast.makeText(this, "Posi un nom i un enllaç", Toast.LENGTH_SHORT).show();
            } else if (bd.createBSO(bso).getId() != -1) {
                Toast.makeText(this, "Afegit correctament", Toast.LENGTH_SHORT).show();
                bd.tanca();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Error a l’afegir BBDD", Toast.LENGTH_SHORT).show();
            }
        }else if(v == btnTorna){
            finish();
        }
    }

    public BSO generaBSO(){
        if(!editTitol.getText().toString().isEmpty()){
            bso = new BSO();
            bso.setTitol(editTitol.getText().toString());
            bso.setAutor(editAutor.getText().toString());
            bso.setDuracio(editDuracio.getText().toString());
            bso.setData(editData.getText().toString());
            bso.setLink(editLink.getText().toString());
        }
        return bso;
    }

    public void publicaVideo(final String videoId, YouTubePlayerView youTubePlayerView) {
        //initialize youtube player view
        youTubePlayerView.initialize(GOOGLE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(videoId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    private String expandUrl(String youTubeUrl) {
        String pattern = "https?://(?:[0-9A-Z-]+\\.)?(?:youtu\\.be/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|</a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


}