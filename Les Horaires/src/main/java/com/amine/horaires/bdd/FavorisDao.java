package com.amine.horaires.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.amine.horaires.models.Shop;

import java.util.ArrayList;

public class FavorisDao {
    private static final int VERSION_BDD = 1;// pour le onupgrade de BaseSQL
    private static final String NOM_BDD = "shops.db";

    private static final String TABLE_FAVORIS = "shop_favoris";
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

    private static final int NUM_COL_NOM = 1;
    private static final int NUM_COL_ADR = 2;
    private static final int NUM_COL_ZIP = 3;
    private static final int NUM_COL_LAT = 4;
    private static final int NUM_COL_LNG = 5;
    private static final int NUM_COL_TEL = 6;
    private static final int NUM_COL_CITY = 7;
    private static final int NUM_COL_OUVERT = 8;
    private static final int NUM_COL_HORAIRES = 9;
    private static final int NUM_COL_URL = 10;
    private static final int NUM_COL_OPENSTATUS = 11;
    private static final int NUM_COL_PARKING = 12;
    private static final int NUM_COL_WIFI = 13;
    private static final int NUM_COL_HANDI = 14;
    private static final int NUM_COL_IDPOI = 15;
    private static FavorisDao instance = null;
    private final FavorisBdd maBaseSQLite;
    private SQLiteDatabase bdd;

    private FavorisDao(Context context) {
        maBaseSQLite = new FavorisBdd(context, NOM_BDD, null, VERSION_BDD);
    }

    public static FavorisDao getInstance(Context c) {
        if (instance == null) {
            instance = new FavorisDao(c);
            return instance;
        } else {
            return instance;
        }
    }

    public void open() {
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        bdd.close();
    }

    public void insertFavori(Shop favori) {
        ContentValues values = getContentValues(favori);
        bdd.insert(TABLE_FAVORIS, null, values);
    }

    private ContentValues getContentValues(Shop favori) {
        ContentValues values = new ContentValues();
        values.put(COL_IDPOI, favori.getId());
        values.put(COL_NOM, favori.getName());
        values.put(COL_ADR, favori.getAdresse());
        values.put(COL_ZIP, favori.getZip());
        values.put(COL_LAT, favori.getLat());
        values.put(COL_LNG, favori.getLng());
        values.put(COL_TEL, favori.getTel());
        values.put(COL_CITY, favori.getCity());
        values.put(COL_OUVERT, favori.isOuvert());
        values.put(COL_HORAIRES, favori.getHoraires());
        values.put(COL_URL, favori.getUrl());
        values.put(COL_OPENSTATUS, favori.getOpenStatus());
        values.put(COL_PARKING, favori.isParking());
        values.put(COL_WIFI, favori.isWifi());
        values.put(COL_HANDI, favori.isAccesHandicape());
        return values;
    }

    public void updateFavori(Shop favori) {
        ContentValues values = getContentValues(favori);
        bdd.update(TABLE_FAVORIS, values, COL_IDPOI + " LIKE " + "\"" + favori.getId() + "\"", null);
    }

    public void deleteFavori(int id) {
        bdd.delete(TABLE_FAVORIS, COL_IDPOI + " LIKE \"" + id + "\"", null);
    }

    public Shop getFavori(int id) {
        Cursor c = bdd.query(TABLE_FAVORIS, null, COL_IDPOI + " LIKE \"" + id
                + "\"", null, null, null, null);
        c.moveToFirst();
        Shop s = cursorToFavori(c);
        c.close();
        return s;
    }

    private Shop cursorToFavori(Cursor c) {
        if (c.getCount() == 0) {
            return null;
        } else {
            Shop f = new Shop();
            f.setId(c.getInt(NUM_COL_IDPOI));
            f.setName(c.getString(NUM_COL_NOM));
            f.setAdresse(c.getString(NUM_COL_ADR));
            f.setZip(c.getString(NUM_COL_ZIP));
            f.setLat(c.getString(NUM_COL_LAT));
            f.setLng(c.getString(NUM_COL_LNG));
            f.setTel(c.getString(NUM_COL_TEL));
            f.setCity(c.getString(NUM_COL_CITY));
            f.setOuvert(c.getShort(NUM_COL_OUVERT) != 0);
            f.setHoraires(c.getString(NUM_COL_HORAIRES));
            f.setUrl(c.getString(NUM_COL_URL));
            f.setOpenStatus(c.getString(NUM_COL_OPENSTATUS));
            f.setParking(c.getShort(NUM_COL_PARKING) != 0);
            f.setWifi(c.getShort(NUM_COL_WIFI) != 0);
            f.setAccesHandicape(c.getShort(NUM_COL_HANDI) != 0);
            f.setId(c.getInt(NUM_COL_IDPOI));
            return f;
        }
    }

    public ArrayList<Shop> getAllFavoris() {
        Cursor c = bdd.rawQuery("SELECT * FROM " + TABLE_FAVORIS, null);
        return cursorToListeFavoris(c);
    }

    private ArrayList<Shop> cursorToListeFavoris(Cursor cursor) {
        ArrayList<Shop> list = new ArrayList<Shop>();
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    list.add(cursorToFavori(cursor));
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        return list;
    }
}
