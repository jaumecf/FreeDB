package com.example.freedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.freedb.ui.dialog.DatePickerFragment;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AfegeixPelicula extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private int GALLERY_REQUEST_CODE = 1;
    private int APP_PERMISSION_READ_STORAGE = 1;
    private int RESULT_OK = 1;

    private ArrayList<Genere> llista_generes;
    private InterficieBBDD bd;
    private EditText editNom, editComentari, editData;
    private Button btnAfegir, btnTorna;
    private ImageView mImatge;
    private Spinner spinner;
    private Bitmap imatge_bitmap;
    private byte[] bitmapmap;
    private float valoracio_rating = 0;
    private Genere genere = null;
    private Pelicula peli = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir_pelicula);

        editNom = findViewById(R.id.nom);
        editComentari = findViewById(R.id.comentari);
        editData = findViewById(R.id.data);
        btnAfegir = (Button) findViewById(R.id.confirma);
        btnTorna = (Button) findViewById(R.id.torna);
        mImatge = (ImageView) findViewById(R.id.imatge);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);
        editData.setOnClickListener(this);
        mImatge.setOnClickListener(this);
        btnAfegir.setOnClickListener(this);
        btnTorna.setOnClickListener(this);

        // Quan seleccionem una puntuació
        final RatingBar rating = findViewById(R.id.valoracio);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(),  " La puntuació seleccionada és: " + rating, Toast.LENGTH_SHORT).show();
                valoracio_rating = rating;
            }
        });

        bd = new InterficieBBDD(this);
        bd.obre();
        llista_generes = bd.llistaGeneres();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,ferSpinner(llista_generes));

        // Drop down layout style - list view with radio button
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
        bd.tanca();

    }

    @Override
    public void onClick(View v) {
        if (v == btnAfegir) {
            bd = new InterficieBBDD(this);
            bd.obre();
            Pelicula peli = generaObjectePelicula();

            if (peli == null) {
                Toast.makeText(this, "Posi un nom i un genere correcte", Toast.LENGTH_SHORT).show();
            } else if (bd.createPeli(peli).getId() != -1) {
                Toast.makeText(this, "Afegit correctament", Toast.LENGTH_SHORT).show();
                bd.tanca();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Error a l’afegir BBDD", Toast.LENGTH_SHORT).show();
            }
        }else if(v == mImatge){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, APP_PERMISSION_READ_STORAGE);
            }
            recullDeGaleria();
        }else if(v == editData){
            mostraData();
        }else if (v == btnTorna) {
            finish();
        }

    }

    public Pelicula generaObjectePelicula(){
        if(genere != null && !editNom.getText().toString().isEmpty()){
            peli = new Pelicula();
            peli.setNom(editNom.getText().toString());
            peli.setComentari((editComentari.getText().toString()));
            peli.setData(editData.getText().toString());
            peli.setValoracio(valoracio_rating);
            peli.setFoto(bitmapmap);
            peli.setIdGenere(genere.getId());
        }
        return peli;
    }

    private void recullDeGaleria(){
        //Cream l'Intent amb l'acció ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Establim tipus d'imatges, per tant només s'acceptaran els tipus imagtge
        intent.setType("image/*");
        //Establim uns tipus de format de fotografia per assegurar-nos d'acceptar només aquest tipus de format jpg i png
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Llançam l'Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {//data.getData return the content URI for the selected Image
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                //Get the column index of MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //Gets the String value in the column
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                imatge_bitmap = BitmapFactory.decodeFile(imgDecodableString);

                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                imatge_bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /* Ignored for PNGs */, blob);
                bitmapmap = blob.toByteArray();
                mImatge.setImageBitmap(imatge_bitmap);

            }
        }
    }

    private void mostraData(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                editData.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private List<String> ferSpinner(ArrayList<Genere> generes){
        // Spinner Drop down elements
        List<String> string_generes = new ArrayList<String>();
        string_generes.add("Selecciona genere..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (int i = 0; i<generes.size();i++){
                string_generes.add(generes.get(i).getNom());
            }
        }
        return string_generes;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            genere = llista_generes.get(position-1);


        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
