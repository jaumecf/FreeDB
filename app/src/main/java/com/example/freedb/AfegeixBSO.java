package com.example.freedb;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;


public class AfegeixBSO extends AppCompatActivity{

    private InterficieBBDD bd;
    private EditText editNom, editComentari, editData,editLink;
    private Button btnAfegir, btnTorna;
    private BSO peli = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegeix_b_s_o);

        editNom = findViewById(R.id.nom);
        editComentari = findViewById(R.id.comentari);
        editData = findViewById(R.id.data);
        editLink = findViewById(R.id.link);
        btnAfegir = (Button) findViewById(R.id.confirma);
        btnTorna = (Button) findViewById(R.id.torna);

    }


}