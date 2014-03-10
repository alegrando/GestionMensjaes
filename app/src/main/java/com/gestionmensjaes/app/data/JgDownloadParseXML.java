package com.gestionmensjaes.app.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;


import com.gestionmensjaes.app.ItemListFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//TODO: Instanciar y ejecutar como:
//JgDownloadParseXML jgDownloadParseXML = new JgDownloadParseXML();
//jgdownloadParseXML.execute("http://ruta/documento.xml");

public class JgDownloadParseXML extends AsyncTask<String, Void, Void> {
    private final String NAMESPACE = null;
    private XmlPullParser parser = Xml.newPullParser();
    private Context context;
    private ItemListFragment itemListFragment;

    //TODO: Indicar las etiquetas correspondientes
    private static final String TAG_XML = "MENSAJES";
    private static final String TAG_REGISTRO = "MENSAJE";
    //TODO: Indicar nombre de la clase de los datos en el ArrayList<.....>
    private ArrayList<Mensajes> listaDatos;


    public JgDownloadParseXML(Context context, ItemListFragment mensajeListFragment) {
        this.context = context;
        this.itemListFragment = mensajeListFragment;
    }

    //TODO: Indicar tipo de objeto retornado
    private Mensajes leerRegistro() {
        //TODO: Crear una variable para cada dato del objeto
        String id = null;
        String asunto = null;
        String contenido = null;
        String remitente = null;
        String grupo = null;
        try {
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_REGISTRO);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String tagName = parser.getName();
                //TODO: Repetir para cada dato del objeto
                if (tagName.equals("ID")) {
                    id = readText(tagName);
                } else if (tagName.equals("ASUNTO")) {
                    asunto = readText(tagName);
                } else if (tagName.equals("CONTENIDO")) {
                    contenido = readText(tagName);
                } else if (tagName.equals("REMITENTE")) {
                    remitente = readText(tagName);
                } else if (tagName.equals("GRUPO")) {
                    grupo = readText(tagName);
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: Retornar un objeto usando los datos obtenidos
        return new Mensajes(id,asunto, contenido, remitente, grupo);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //TODO: Tareas a realizar cuando finalice el tratamiento del documento XML
        Log.d(JgDownloadParseXML.class.getName(), "Descarga de datos finalizada. Iniciando procesamiento");
        Log.d(JgDownloadParseXML.class.getName(), "Nº registros descargados: " + listaDatos.size());
        //Conectar con la BD y recorrer los elementos descargados desde el XML
        BDMensajes bdmensajes = new BDMensajes(context);

        for(Mensajes mensajes : listaDatos) {
            //Comprobar si ya existe un elemento con la misma ID
            Log.d(JgDownloadParseXML.class.getName(), "Comprobando si exite el id: " + mensajes.getId());
            if(bdmensajes.findi(mensajes.getId()) == null) {
                Log.d(JgDownloadParseXML.class.getName(), "Id no encontrado. Se insertará el registro");
                //Si no existe, se inserta
                bdmensajes.insertar(mensajes);
            }else{
                bdmensajes.actualizar(mensajes);
            }
        }
        itemListFragment.mostrarListaMensajes();
    }

    @Override
    protected Void doInBackground(String... urls) {

        InputStream stream = null;
        try {
            stream = downloadUrl(urls[0]);
            listaDatos = xmlToList(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }


    private ArrayList xmlToList(InputStream stream) {
        ArrayList list = new ArrayList();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_XML);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals(TAG_REGISTRO)) {
                    list.add(leerRegistro());
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }


}
