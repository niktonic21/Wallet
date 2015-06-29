package sk.upjs.ics.wallet.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sk.upjs.ics.wallet.util.Defaults;

import static android.provider.BaseColumns._ID;
import static sk.upjs.ics.wallet.util.Defaults.DEFAULT_CURSOR_FACTORY;
import static sk.upjs.ics.wallet.provider.Provider.Uzivatel.TABLE_NAME;
import static sk.upjs.ics.wallet.provider.Provider.Uzivatel.MENO;
import static sk.upjs.ics.wallet.provider.Provider.Uzivatel.HESLO;
import static sk.upjs.ics.wallet.provider.Provider.Uzivatel.FINANCIE;



public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "walletike";
    public static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, DEFAULT_CURSOR_FACTORY, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s TEXT,"
                + "%s REAL"
                + ")";
        String sql =  String.format(sqlTemplate, TABLE_NAME, _ID,MENO,HESLO,FINANCIE);
        db.execSQL(sql);

        String sqlTemplate2 = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s REAL,"
                + "%s TEXT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s INTEGER"
                + ")";
        String sql3 =  String.format(sqlTemplate2,Provider.Transakcia.TABLE_NAME,Provider.Transakcia._ID,
                Provider.Transakcia.ID_UZIVATEL,Provider.Transakcia.TYP, Provider.Transakcia.SUMA,
                Provider.Transakcia.POLOZKA,Provider.Transakcia.DAY,Provider.Transakcia.MONTH
                ,Provider.Transakcia.YEAR);
        db.execSQL(sql3);


        String sqlTemplate1 = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s REAL,"
                + "%s REAL,"
                + "%s TEXT"
                + ")";

        String sql2 = String.format(sqlTemplate1,Provider.Bankomat.TABLE_NAME,_ID,
                Provider.Bankomat.LAT,Provider.Bankomat.LNG,Provider.Bankomat.TITLE);
        db.execSQL(sql2);

        String sqlTemplate4 = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s INTEGER,"
                + "%s TEXT"
                + ")";

        String sql4 = String.format(sqlTemplate4,Provider.PhotoUri.TABLE_NAME,
                Provider.PhotoUri._ID, Provider.PhotoUri.URI,
                Provider.PhotoUri.YEAR,Provider.PhotoUri.MONTH,
                Provider.PhotoUri.DAY,Provider.PhotoUri.DESCRIPTION);
        db.execSQL(sql4);


        insertSampleEntry3(db, "mato", "heslo", 1244.5);
        insertSampleEntry3(db, "martin", "he", 824.5);
        insertSampleEntry3(db,"mat","ma",124.5);
        //view-source:http://www.tatrabanka.sk/kontakty/bankomaty
        insertSampleEntry(db,48.72534341720158,21.255332862426737,"hlavna-108");
        insertSampleEntry(db,48.717259219033856,21.260373624328626,"hlavna-1");
        insertSampleEntry(db,48.72230039999999,21.248965399999975,"kuzmanyho-29");
        insertSampleEntry(db,48.73936508339666,21.277908777771017,"lidicke-nam.-13");
        insertSampleEntry(db,48.696085,21.238344,"moldavska-cesta-32");
        insertSampleEntry(db,48.717500911996176,21.262330119079706,"namestie-osloboditelov-1");
        insertSampleEntry(db,48.71720893061631,21.262140798205564,"namestie-osloboditelov-1");
        insertSampleEntry(db,48.757649,21.92248159999997,"namestie-osloboditelov-1");
        insertSampleEntry(db,48.67949392939505,21.281262640197756,"napajadla-16");
        insertSampleEntry(db,48.69693802230653,21.25100621691895,"pri-pracharni-4");
        insertSampleEntry(db,48.71703950265539,21.25945373809816,"sturova-1");
        insertSampleEntry(db,48.716024432082754,21.254486724853532,"sturova-28");
        insertSampleEntry(db,48.71407415252284,21.237812063476582,"toryska-3");
        insertSampleEntry(db,48.71481645461997,21.237720144519017,"toryska-5");
        insertSampleEntry(db,48.71603663251253,21.213894878295907,"trieda-kosickeho-vladneho-programu-1");
        insertSampleEntry(db,48.740646945799234,21.267303185180676,"trolejbusova-1.html");
        insertSampleEntry(db,48.63396140023822,21.171416624328604,"zeleziarenska-11");

        insertSampleEntry2(db,1,1,323.3,"sako",12,2,2015);
        insertSampleEntry2(db,1,0,26.7,"nakup fresh",24,3,2015);
        insertSampleEntry2(db,1,0,53.0,"alkohol",26,4,2015);

        insertSampleEntry2(db,2,1,323.3,"auto oprava",12,2,2015);
        insertSampleEntry2(db,2,0,26.7,"nakup lidl",24,3,2015);
        insertSampleEntry2(db,2,0,53.0,"hokejka",26,4,2015);

    }

    private void insertSampleEntry(SQLiteDatabase db, Double lat,Double lng,String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.Bankomat.LAT, lat);
        contentValues.put(Provider.Bankomat.LNG,lng);
        contentValues.put(Provider.Bankomat.TITLE, title);

        db.insert(Provider.Bankomat.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, contentValues);
    }
    private void insertSampleEntry3(SQLiteDatabase db, String meno,String heslo,Double suma) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.Uzivatel.MENO, meno);
        contentValues.put(Provider.Uzivatel.HESLO,heslo);
        contentValues.put(Provider.Uzivatel.FINANCIE,suma);

        db.insert(Provider.Uzivatel.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, contentValues);
    }

    private void insertSampleEntry2(SQLiteDatabase db, int idecko,int typ,Double suma,String polozka,int den,int mesiac,int rok) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.Transakcia.ID_UZIVATEL,idecko );
        contentValues.put(Provider.Transakcia.TYP,typ );
        contentValues.put(Provider.Transakcia.SUMA,suma );
        contentValues.put(Provider.Transakcia.POLOZKA,polozka );
        contentValues.put(Provider.Transakcia.DAY,den );
        contentValues.put(Provider.Transakcia.MONTH,mesiac );
        contentValues.put(Provider.Transakcia.YEAR, rok);

        db.insert(Provider.Transakcia.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, contentValues);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }
}

