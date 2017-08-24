package com.example.dptoredes.leccion;

import android.app.Notification;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ListView listadoUsuarios;
    private long last_update = 0, last_movement = 0;
    private float prevX = 0, prevY = 0, prevZ = 0;
    private float curX = 0, curY = 0, curZ = 0;
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            long current_time = event.timestamp;

            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];

            if (prevX == 0 && prevY == 0 && prevZ == 0) {
                last_update = current_time;
                last_movement = current_time;
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
            }

            long time_difference = current_time - last_update;
            if (time_difference > 0) {
                float movement = Math.abs((curX + curY + curZ) - (prevX - prevY - prevZ)) / time_difference;
                int limit = 1500;
                float min_movement = 1E-6f;
                if (movement > min_movement) {
                    if (current_time - last_movement >= limit) {
                        Toast.makeText(getApplicationContext(), "Hay movimiento de " + movement, Toast.LENGTH_SHORT).show();
                    }
                    last_movement = current_time;
                }
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
                last_update = current_time;
            }


            ((TextView) findViewById(R.id.txtAccX)).setText("Aceler—metro X: " + curX);
            ((TextView) findViewById(R.id.txtAccY)).setText("Aceler—metro Y: " + curY);
            ((TextView) findViewById(R.id.txtAccZ)).setText("Aceler—metro Z: " + curZ);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}