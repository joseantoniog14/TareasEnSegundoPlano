package com.example.tareasensegundoplano;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    Button buttonStart,buttonStop,buttonStart2,buttonStop2,buttonCuentaAtras;
    TextView textViewCrono,textViewCrono2;
    int contador;
    Thread hilo=null;
    boolean hiloActivo;
    boolean crono2ON;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart=findViewById(R.id.buttonStart);
        buttonStop=findViewById(R.id.buttonStop);
        textViewCrono=findViewById(R.id.textViewCrono);
        buttonStart2=findViewById(R.id.buttonStart2);
        buttonStop2=findViewById(R.id.buttonStop2);
        textViewCrono2=findViewById(R.id.textViewCrono2);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hilo==null){
                    hiloActivo=true;
                hilo = new Thread() {
                    @Override
                    public void run() {
                        contador = 0;
                        while (hiloActivo) {
                            int minutos = contador / 60;
                            int segundos = contador % 60;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //textViewCrono.setText(minutos + ":" + segundos);
                                    textViewCrono.setText(String.format("%02d:%02d",minutos,segundos));
                                }
                            });
                            Log.d("CRONO", minutos + ":" + segundos);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            contador++;
                        }
                    }
                };
                hilo.start();
            }
            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiloActivo=false;
                hilo=null;
                Log.d("CRONO","Crono parado");
            }
        });
        buttonStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MiCronometro mc=new MiCronometro(0,textViewCrono2);
                crono2ON=true;
                mc.execute();
            }
        });
        buttonStop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crono2ON=false;
            }
        });
        buttonCuentaAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuentaatras miCronometro=new cuentaatras(100,progressBar);
                miCronometro.execute();
            }
        });
    }
    private class cuentaatras extends AsyncTask<String ,String, String>{
        int micontador;
        ProgressBar progressBar;
        cuentaatras(int cont,ProgressBar sd){
            micontador=cont;
            progressBar=sd;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            while (micontador!=0){
                publishProgress(String.valueOf(micontador));
                micontador--;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressBar.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            buttonStart.callOnClick();
            buttonStart2.callOnClick();
        }
    }
    private  class MiCronometro extends AsyncTask<String ,String,String>{
        int miContador;
        TextView miIextView;
        MiCronometro(int inicio,TextView tv){
            miContador=inicio;
            miIextView=tv;

        }
        @Override
        protected String doInBackground(String... strings) {
            while(true){
                int minutos=contador/60;
                int segundos=contador%60;
                String salida=String.format("%02d:%02d",minutos,segundos);
                publishProgress(salida);
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                miContador++;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            miIextView.setText(values[0]);
        }

    }
}