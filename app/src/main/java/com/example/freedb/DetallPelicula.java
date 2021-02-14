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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.example.freedb.BBDD.InterficieBBDD;
import com.example.freedb.ui.dialog.DatePickerFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DetallPelicula extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private InterficieBBDD bd;
    private ArrayList<Genere> llista_generes;
    private ArrayList<BSO> llista_bso;

    private int GALLERY_REQUEST_CODE = 1;
    private int APP_PERMISSION_READ_STORAGE = 1;
    private int RESULT_OK = 1;

    private Pelicula peli;
    private long idPelicula;
    private Button btnTorna, btnActualitza, btnEsborra;

    private EditText editNom, editComentari, editData, editGenere;
    private RatingBar ratingBar;
    private ImageView mImatge;
    private Bitmap imatge_bitmap;
    private byte[] bitmapmap;
    private float valoracio_rating = 0;
    private Spinner spinnerGenere, spinnerBSO;
    private Genere genere = null;
    private BSO bso = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall_pelicula);
        Bundle extras = getIntent().getExtras();
        idPelicula = extras.getLong("idPelicula");
        bd = new InterficieBBDD(this.getApplicationContext());
        bd.obre();
        peli = bd.obtenirPelicula(idPelicula);



        editNom = findViewById(R.id.nom);
        editComentari = findViewById(R.id.comentari);
        editData = findViewById(R.id.data);
        ratingBar = findViewById(R.id.valoracio);
        mImatge = (ImageView) findViewById(R.id.imatge);

        //Botons i spinner
        btnTorna = (Button) findViewById(R.id.torna);
        btnActualitza = (Button) findViewById(R.id.actualitza);
        btnEsborra = (Button) findViewById(R.id.esborra);
        spinnerGenere = (Spinner) findViewById(R.id.spinnerGenere);
        spinnerBSO = (Spinner) findViewById(R.id.spinnerBSO);

        spinnerGenere.setOnItemSelectedListener(this);
        spinnerBSO.setOnItemSelectedListener(this);
        btnTorna.setOnClickListener(this);
        btnActualitza.setOnClickListener(this);
        btnEsborra.setOnClickListener(this);
        editData.setOnClickListener(this);
        mImatge.setOnClickListener(this);


        showInfo(peli);
    }


    @Override
    public void onClick(View v) {
        if (v == btnTorna) {
            bd.tanca();
            finish();
        }else if(v == mImatge){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        APP_PERMISSION_READ_STORAGE);
            }
            recullDeGaleria();
        }else if(v == editData){
            mostraData();
        }else if(v == btnActualitza){
            bd.actualitzaPelicula(idPelicula,getInfo());
            bd.tanca();
            finish();
        }else if(v == btnEsborra){
            bd.esborraPelicula(idPelicula);
            bd.tanca();
            finish();
        }

    }

    public void showInfo(Pelicula peli){
        editNom.setText(peli.getNom());
        editComentari.setText(peli.getComentari());
        editData.setText(peli.getData());
        ratingBar.setRating(peli.getValoracio());
        ferSpinnerGeneres();
        ferSpinnerBSO();
        pinta_imatge(peli);
    }

    public void pinta_imatge(Pelicula pelicula){
        if(pelicula.getFoto() == null){
            mImatge.setImageResource(R.drawable.no_image);
        }else{
            bitmapmap = pelicula.getFoto();
            Bitmap bmp = BitmapFactory.decodeByteArray(bitmapmap,0,bitmapmap.length);
            mImatge.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));
        }
    }

    public Pelicula getInfo(){
        peli.setNom(editNom.getText().toString());
        peli.setComentari((editComentari.getText().toString()));
        peli.setData(editData.getText().toString());
        peli.setValoracio(ratingBar.getRating());
        peli.setFoto(bitmapmap);
        peli.setIdGenere(genere.getId());

        return peli;
    }

    private void recullDeGaleria(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
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

    private void ferSpinnerGeneres() {

        llista_generes = bd.llistaGeneres();

        // Spinner Drop down elements
        List<String> string_generes = new ArrayList<String>();
        string_generes.add("Selecciona genere..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (int i = 0; i < llista_generes.size(); i++) {
                string_generes.add(llista_generes.get(i).getNom());
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.text_spinner, string_generes);

        // attaching data adapter to spinner
        spinnerGenere.setAdapter(dataAdapter);
        spinnerGenere.setSelection(Math.round(peli.getIdGenere()));

    }

    private void ferSpinnerBSO(){

        llista_bso = bd.llistaBSO();

        // Spinner Drop down elements
        List<String> string_bso = new ArrayList<String>();
        string_bso.add("Selecciona bso..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (int i = 0; i<llista_bso.size();i++){
                string_bso.add(llista_bso.get(i).getTitol());
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterBSO = new ArrayAdapter<>(this, R.layout.text_spinner, string_bso);

        // attaching data adapter to spinner
        spinnerBSO.setAdapter(dataAdapterBSO);
        spinnerBSO.setSelection(Math.round(peli.getIdBSO()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            if(parent.getId() == R.id.spinnerGenere) {
                genere = llista_generes.get(position-1);
            } else if(parent.getId() == R.id.spinnerBSO) {
                bso = llista_bso.get(position-1);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
