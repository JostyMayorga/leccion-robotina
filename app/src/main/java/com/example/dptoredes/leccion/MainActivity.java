package com.example.dptoredes.leccion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    ListView listadoUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listadoUsuarios = (ListView) findViewById(R.id.listadoUsuarios);
        obtenerDatos();
    }
    public void obtenerDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://jsonplaceholder.typicode.com/posts";



        client.post(url, null, new AsyncHttpResponseHandler() {

        //client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200){//OK
                    cargarUsuarios(obtenerDatosJson(new String(responseBody)));
                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Carga Fallida", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void cargarUsuarios(ArrayList<String> usuarios){
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1, usuarios);
        listadoUsuarios.setAdapter(adaptador);
    }

    public ArrayList<String> obtenerDatosJson(String respuesta){

        ArrayList<String> usuarios = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(respuesta);
            String texto = "";
            for (int i=0; i < jsonArray.length(); i++){
                texto =  jsonArray.getJSONObject(i).getString("title");
                usuarios.add(texto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return usuarios;

    }
}