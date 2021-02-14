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

public class DetallBSO extends YouTubeBaseActivity implements View.OnClickListener {

    private InterficieBBDD bd;
    private EditText editTitol, editAutor, editDuracio, editData,editLink;
    private Button btnEsborra, btnActualitza, btnTorna;
    private YouTubePlayerView youtubeView;
    private BSO bso = null;
    private String GOOGLE_API_KEY = "AIzaSyDZ7MLFQ2R7Ru6dBceOcxOIc5M-VhhIW_k";
    private String YOUTUBE_VIDEO_ID = "";
    private long idBSO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall_b_s_o);

        Bundle extras = getIntent().getExtras();
        idBSO = extras.getLong("idBSO");
        bd = new InterficieBBDD(this.getApplicationContext());
        bd.obre();
        bso = bd.obtenirBSO(idBSO);

        editTitol = findViewById(R.id.titol);
        editAutor = findViewById(R.id.autor);
        editDuracio = findViewById(R.id.duracio);
        editData = findViewById(R.id.data);
        editLink = findViewById(R.id.link);
        btnTorna = (Button) findViewById(R.id.torna);
        youtubeView = findViewById(R.id.youtube_player);

        //Botons i spinner
        btnTorna = (Button) findViewById(R.id.torna);
        btnActualitza = (Button) findViewById(R.id.actualitza);
        btnEsborra = (Button) findViewById(R.id.esborra);

        btnTorna.setOnClickListener(this);
        btnActualitza.setOnClickListener(this);
        btnEsborra.setOnClickListener(this);
        editLink.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                YOUTUBE_VIDEO_ID = expandUrl(editLink.getText().toString());
                Log.d("ID","ID: "+YOUTUBE_VIDEO_ID);
                publicaVideo(YOUTUBE_VIDEO_ID, youtubeView);
            }

        });

        showInfo(bso);

    }

    @Override
    public void onClick(View v) {
        if (v == btnTorna) {
            bd.tanca();
            finish();
        }else if(v == btnActualitza){
            if (bd.actualitzaBSO(idBSO,getInfo())) {
                Toast.makeText(this, "Afegit correctament", Toast.LENGTH_SHORT).show();
                bd.tanca();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Error al modificar a la BBDD", Toast.LENGTH_SHORT).show();
            }
            bd.tanca();
            finish();
        }else if(v == btnEsborra){
            bd.esborraPelicula(idBSO);
            bd.tanca();
            finish();
        }
    }

    public void showInfo(BSO bso){
        editTitol.setText(bso.getTitol());
        editAutor.setText(bso.getAutor());
        editDuracio.setText(bso.getDuracio());
        editData.setText(bso.getData());
        editLink.setText(bso.getLink());
        YOUTUBE_VIDEO_ID = expandUrl(editLink.getText().toString());
        Log.d("ID","ID: "+YOUTUBE_VIDEO_ID);
        publicaVideo(YOUTUBE_VIDEO_ID, youtubeView);
    }

    public BSO getInfo(){
        bso.setTitol(editTitol.getText().toString());
        bso.setAutor(editAutor.getText().toString());
        bso.setDuracio(editDuracio.getText().toString());
        bso.setData(editData.getText().toString());
        bso.setLink(editLink.getText().toString());

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