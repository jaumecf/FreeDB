package com.example.freedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class InterficieBBDD {

    private final Context context;
    private AjudaPeliBBDD ajuda;
    private SQLiteDatabase bd;

    private String[] allColumnsPelicula = {AjudaPeliBBDD.CLAU_ID_PELICULA, AjudaPeliBBDD.CLAU_NOM, AjudaPeliBBDD.CLAU_COMENTARI, AjudaPeliBBDD.CLAU_DATA, AjudaPeliBBDD.CLAU_VALORACIO, AjudaPeliBBDD.CLAU_FOTO, AjudaPeliBBDD.CLAU_REL_GEN, AjudaPeliBBDD.CLAU_REL_BSO};

    private String[] allColumnsBSO = {AjudaPeliBBDD.CLAU_ID_PELICULA, AjudaPeliBBDD.CLAU_NOM, AjudaPeliBBDD.CLAU_COMENTARI, AjudaPeliBBDD.CLAU_DATA, AjudaPeliBBDD.CLAU_VALORACIO, AjudaPeliBBDD.CLAU_FOTO, AjudaPeliBBDD.CLAU_REL_GEN, AjudaPeliBBDD.CLAU_REL_BSO};

    private String[] allColumnsGenere = {AjudaPeliBBDD.CLAU_ID_GENERE, AjudaPeliBBDD.CLAU_GENERE};


    public InterficieBBDD(Context con) {
        this.context = con;
        ajuda = new AjudaPeliBBDD(context);
    }

    //Obre la Base de dades
    public void obre() throws SQLException {
        bd = ajuda.getWritableDatabase();
    }

    //Tanca la Base de dades
    public void tanca() {
        ajuda.close();
    }

    public Pelicula createPeli(Pelicula peli) {
        // insert d'una nova peli
        ContentValues values = new ContentValues();
        values.put(AjudaPeliBBDD.CLAU_NOM, peli.getNom());
        values.put(AjudaPeliBBDD.CLAU_COMENTARI, peli.getComentari());
        values.put(AjudaPeliBBDD.CLAU_DATA, String.valueOf(peli.getData()));
        values.put(AjudaPeliBBDD.CLAU_VALORACIO, peli.getValoracio());
        values.put(AjudaPeliBBDD.CLAU_FOTO, peli.getFoto());
        values.put(AjudaPeliBBDD.CLAU_REL_GEN, peli.getIdGenere());
        values.put(AjudaPeliBBDD.CLAU_REL_BSO, peli.getIdBSO());
        long insertId = bd.insert(AjudaPeliBBDD.BD_TAULA_PELI, null, values);
        peli.setId(insertId);
        return peli;
    }

    public ArrayList<Pelicula> getAllPelicula() {
        ArrayList<Pelicula> pelicules = new ArrayList<Pelicula>();
        Cursor cursor = bd.query(AjudaPeliBBDD.BD_TAULA_PELI, allColumnsPelicula, null, null, null, null, AjudaPeliBBDD.CLAU_NOM + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Pelicula peli = cursorToPelicula(cursor);
            pelicules.add(peli);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return pelicules;
    }

    public ArrayList<Pelicula> consultaPelis(String regex) {
        ArrayList<Pelicula> pelicules = new ArrayList<Pelicula>();
        Cursor cursor = bd.query(true, AjudaPeliBBDD.BD_TAULA_PELI, allColumnsPelicula,AjudaPeliBBDD.CLAU_NOM + " LIKE ?", new String[] { regex+"%" }, null, null, AjudaPeliBBDD.CLAU_NOM + " ASC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Pelicula peli = cursorToPelicula(cursor);
            pelicules.add(peli);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return pelicules;
    }


    private Pelicula cursorToPelicula(Cursor cursor) {
        Pelicula peli = new Pelicula();
        peli.setId(cursor.getLong(0));
        peli.setNom(cursor.getString(1));
        peli.setComentari(cursor.getString(2));
        peli.setData(cursor.getString(3));
        peli.setValoracio(cursor.getFloat(4));
        peli.setFoto(cursor.getBlob(5));
        peli.setIdGenere(cursor.getLong(6));
        peli.setIdBSO(cursor.getLong(7));
        return peli;
    }

    //Retorna una pelicula
    public Pelicula obtenirPelicula(long IDFila) throws SQLException {
        Cursor mCursor = bd.query(true, AjudaPeliBBDD.BD_TAULA_PELI, allColumnsPelicula,AjudaPeliBBDD.CLAU_ID_PELICULA + " = " + IDFila, null, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return cursorToPelicula(mCursor);
    }

    //Modifica un contacte
    public boolean actualitzaPelicula(long IDFila, Pelicula peli) {
        ContentValues values = new ContentValues();
        values.put(AjudaPeliBBDD.CLAU_NOM, peli.getNom());
        values.put(AjudaPeliBBDD.CLAU_COMENTARI, peli.getComentari());
        values.put(AjudaPeliBBDD.CLAU_DATA, String.valueOf(peli.getData()));
        values.put(AjudaPeliBBDD.CLAU_VALORACIO, peli.getValoracio());
        values.put(AjudaPeliBBDD.CLAU_FOTO, peli.getFoto());
        values.put(AjudaPeliBBDD.CLAU_REL_GEN, peli.getIdGenere());
        values.put(AjudaPeliBBDD.CLAU_REL_BSO, peli.getIdBSO());
        return bd.update(AjudaPeliBBDD.BD_TAULA_PELI, values, AjudaPeliBBDD.CLAU_ID_PELICULA + " = " + IDFila, null) > 0;
    }

    //Esborra un contacte
    public boolean esborraPelicula(long IDFila) {
        return bd.delete(AjudaPeliBBDD.BD_TAULA_PELI, AjudaPeliBBDD.CLAU_ID_PELICULA + " = " + IDFila, null) > 0;
    }


    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public ArrayList<Genere> llistaGeneres() {
        ArrayList<Genere> llista = new ArrayList<Genere>();
        Cursor cursor = bd.query(AjudaPeliBBDD.BD_TAULA_GENERE, allColumnsGenere, null, null, null, null, AjudaPeliBBDD.CLAU_GENERE + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Genere gen = cursorToGenere(cursor);
            llista.add(gen);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return llista;
    }

    private Genere cursorToGenere(Cursor cursor) {
        Genere gen = new Genere();
        gen.setId(cursor.getLong(0));
        gen.setNom(cursor.getString(1));
        return gen;
    }

    //Retorna una pelicula
    public String obtenirGenere(long IDFila) throws SQLException {
        Cursor mCursor = bd.query(true, AjudaPeliBBDD.BD_TAULA_GENERE, allColumnsGenere,AjudaPeliBBDD.CLAU_ID_GENERE + " = " + IDFila, null, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return cursorToGenere(mCursor).getNom();
    }

}
