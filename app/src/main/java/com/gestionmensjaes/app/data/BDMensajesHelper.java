package com.gestionmensjaes.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alejandro on 10/02/14.
 */
public class BDMensajesHelper extends SQLiteOpenHelper {


    public BDMensajesHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE mensajes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", asunto VARCHAR(50)" +
                ", contenido TEXT" +
                ", remitente VARCHAR(50)" +
                ", grupo VARCHAR(50)" +
                ")";
        db.execSQL(sqlCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if(oldVersion==1 && newVersion==2) ... ALTER TABLE CURSO....
    }
}
