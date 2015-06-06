package com.amine.horaires.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class FavorisBdd extends SQLiteOpenHelper {

    private static final String TABLE_FAVORIS = "shop_favoris";
    private static final String COL_ID = "ID";
    private static final String COL_NOM = "Nom";
    private static final String COL_ADR = "Adresse";
    private static final String COL_ZIP = "Zip";
    private static final String COL_LAT = "Lat";
    private static final String COL_LNG = "Lng";
    private static final String COL_TEL = "Tel";
    private static final String COL_CITY = "City";
    private static final String COL_OUVERT = "Ouvert";
    private static final String COL_HORAIRES = "Horaires";
    private static final String COL_URL = "Url";
    private static final String COL_OPENSTATUS = "OpenStatus";
    private static final String COL_PARKING = "Parking";
    private static final String COL_WIFI = "Wifi";
    private static final String COL_HANDI = "Handi";
    private static final String COL_IDPOI = "IDPoi";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_FAVORIS + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOM + " TEXT, " +
            COL_ADR + " TEXT, " +
            COL_ZIP + " INTEGER, " +
            COL_LAT + " TEXT, " +
            COL_LNG + " TEXT, " +
            COL_TEL + " VARCHAR(40), " +
            COL_CITY + " TEXT, " +
            COL_OUVERT + " SHORT, " +
            COL_HORAIRES + " TEXT, " +
            COL_URL + " TEXT, " +
            COL_OPENSTATUS + " TEXT, " +
            COL_PARKING + " SHORT, " +
            COL_WIFI + " SHORT, " +
            COL_HANDI + " SHORT, " +
            COL_IDPOI + " VARCHAR(40) NOT NULL);";

    public FavorisBdd(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
