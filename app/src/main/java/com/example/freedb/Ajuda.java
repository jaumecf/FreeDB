package com.example.freedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Ajuda extends AppCompatActivity implements View.OnClickListener{
    EditText comentari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        comentari = findViewById(R.id.comentari);
        Button confirma = findViewById(R.id.confirma);
        Button torna = findViewById(R.id.torna);
        confirma.setOnClickListener(this);
        torna.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirma:
                Log.d("TAG","Hola");
                String comentari_enviar = comentari.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"caf@paucasesnovescifp.cat"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"Ajuda");
                intent.putExtra(Intent.EXTRA_TEXT,comentari_enviar);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Escull la seva app de mail preferida"));

                break;
            case R.id.torna:
                finish();
            default:
                break;
        }
    }
}