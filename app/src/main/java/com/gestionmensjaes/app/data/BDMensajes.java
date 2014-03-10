package com.gestionmensjaes.app.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by alejandro on 10/02/14.
 */
public class BDMensajes {
    protected SQLiteDatabase bdMensajes;
    private static final int VERSION_BD=1;
    private Context context;

    public BDMensajes(Context context){
        this.context = context;
        BDMensajesHelper bdMensajesHelper = new BDMensajesHelper(context, "mensajes_bd", null, VERSION_BD);
        bdMensajes = bdMensajesHelper.getWritableDatabase();
    }



    public void insertar(Mensajes mensajes){
        if(bdMensajes != null) {
            //Insertar datos en la base de datos
            String cadena="";
            bdMensajes.execSQL("INSERT INTO mensajes (asunto, contenido, remitente, grupo) " +
                    "VALUES ('"+mensajes.getAsunto()+"', '"+mensajes.getContenido()+"', '"+mensajes.getRemitente()+"', '"+mensajes.getGrupo()+"')");
        }
    }

    public void actualizar(Mensajes mensajes){
        String id = mensajes.getId();
        String asunto = mensajes.getAsunto();
        String contenido = mensajes.getContenido();
        String remitente = mensajes.getRemitente();
        String grupo = mensajes.getGrupo();
        bdMensajes.execSQL("UPDATE mensajes SET id ='"+id+"',"+
                                                "asunto='"+ asunto+"',"+
                                                "contenido='"+contenido+"',"+
                                                "remitente='"+remitente+"',"+
                                                "grupo='"+grupo+"' WHERE id ='"+id+"'");
    }

    public void borrar(Mensajes mensajes){
        String id = mensajes.getId();
        String asunto = mensajes.getAsunto();
        String contenido = mensajes.getContenido();
        String remitente = mensajes.getRemitente();
        String grupo = mensajes.getGrupo();
        bdMensajes.execSQL("DELETE FROM mensajes WHERE id='"+id+"'");
    }

    public Mensajes findi(String id){
        Cursor cursor = bdMensajes.rawQuery("SELECT * FROM mensajes WHERE id='"+id+"'",null);
        if(cursor.moveToNext()){
            String asunto = cursor.getString(1);
            String contenido = cursor.getString(2);
            String remitente = cursor.getString(3);
            String grupo = cursor.getString(4);
            return new Mensajes(id,asunto,contenido,remitente,grupo);
        }else{
            return null;
        }
    }

    public ArrayList<Mensajes> list(){
        ArrayList<Mensajes> lista = new ArrayList();
        String sql = "SELECT * FROM mensajes";
        Log.d(BDMensajes.class.getName(), "Executing SQL statement: " + sql);
        Cursor cursor = bdMensajes.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String asunto = cursor.getString(1);
            String contenido = cursor.getString(2);
            String remitente = cursor.getString(3);
            String grupo = cursor.getString(4);
            Mensajes mensajes = new Mensajes(id,asunto,contenido,remitente,grupo);
            lista.add(mensajes);
        }
        return lista;
    }

    public void cerrar(){
        bdMensajes.close();
    }

    public String[] getListaMensajes(){
        ArrayList<String> listaMensajes = new ArrayList();
        Cursor cursor = bdMensajes.rawQuery("SELECT * from cursos", null);
        while(cursor.moveToNext()){
            String nombreMensajes = cursor.getString(0);
            listaMensajes.add(nombreMensajes);
        }
        return (String[])listaMensajes.toArray();
    }
}
