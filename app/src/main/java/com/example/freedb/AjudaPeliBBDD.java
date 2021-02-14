package com.example.freedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class AjudaPeliBBDD extends SQLiteOpenHelper {

    //Declaració general BBDD
    public static final String TAG = "InterficieBBDD";
    public static final String BD_NOM = "FreeDB";
    public static final String BD_TAULA_PELI = "pelicules";
    public static final String BD_TAULA_BSO = "bso";
    public static final String BD_TAULA_GENERE = "genere";
    public static final int VERSIO = 1;

    //Declaració de constants classe Pelicula
    public static final String CLAU_ID_PELICULA = "_id";
    public static final String CLAU_NOM = "nom";
    public static final String CLAU_COMENTARI = "comentari";
    public static final String CLAU_DATA = "data";
    public static final String CLAU_VALORACIO = "val";
    public static final String CLAU_FOTO = "foto";
    public static final String CLAU_REL_GEN  = "idGen";
    public static final String CLAU_REL_BSO = "idBso";



    public static final String BD_CREATE_PELI = "create table " + BD_TAULA_PELI + "( " + CLAU_ID_PELICULA + " integer primary key autoincrement, " +
            CLAU_NOM + " TEXT NOT NULL, " + CLAU_COMENTARI + " TEXT, " + CLAU_DATA + " TEXT, " +
            CLAU_VALORACIO + " REAL,  " + CLAU_FOTO + " BLOB, " +
            CLAU_REL_GEN + " INTEGER, " + CLAU_REL_BSO + " INTEGER);";


    //Declaracio taula bso
    public static final String CLAU_ID_BSO = "_id";
    public static final String CLAU_TITOL = "titol";
    public static final String CLAU_AUTOR = "autor";
    public static final String CLAU_DURACIO = "duracio";
    public static final String CLAU_DATA_BSO = "data";
    public static final String CLAU_LINK = "link";

    public static final String BD_CREATE_BSO = "create table " + BD_TAULA_BSO + "( " + CLAU_ID_BSO + " integer primary key autoincrement, " + CLAU_TITOL + " TEXT NOT NULL, " +
            CLAU_AUTOR + " TEXT, " + CLAU_DURACIO + " TEXT, " + CLAU_DATA_BSO + " TEXT, " + CLAU_LINK + " text);";


    //Declaracio taula genere
    public static final String CLAU_ID_GENERE = "_id";
    public static final String CLAU_GENERE = "genere";

    public static final String BD_CREATE_GENERE = "create table " + BD_TAULA_GENERE + "(" + CLAU_ID_GENERE + " integer primary key autoincrement, " + CLAU_GENERE + " text not null);";


    public AjudaPeliBBDD(@Nullable Context context) {
        super(context, BD_NOM, null, VERSIO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(BD_CREATE_PELI);
        db.execSQL(BD_CREATE_BSO);
        db.execSQL(BD_CREATE_GENERE);
        db.execSQL(" INSERT INTO "+BD_TAULA_GENERE+" (genere) values (('Accio'))");
        db.execSQL(" INSERT INTO "+BD_TAULA_GENERE+" (genere) values (('Drama'))");
        db.execSQL(" INSERT INTO "+BD_TAULA_GENERE+" (genere) values (('Romantica'))");
        db.execSQL(" INSERT INTO "+BD_TAULA_GENERE+" (genere) values (('Suspens'))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Detecta si hi ha una canvi a DATABASE_VERSION i recrea la base de dades
        Log.w(AjudaPeliBBDD.class.getName(), "Modificant desde la versió " + oldVersion + " a "+ newVersion );
        db.execSQL("DROP TABLE IF EXISTS " + BD_TAULA_PELI);
        db.execSQL("DROP TABLE IF EXISTS " + BD_TAULA_BSO);
        db.execSQL("DROP TABLE IF EXISTS " + BD_TAULA_GENERE);
        onCreate(db);
    }


}
