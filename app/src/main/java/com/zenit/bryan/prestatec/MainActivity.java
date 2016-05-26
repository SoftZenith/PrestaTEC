package com.zenit.bryan.prestatec;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPrestamos = (Button) findViewById(R.id.btnPrestamos);
        Button btnContactos = (Button) findViewById(R.id.btnContactos);
        Button btnConfiguracion = (Button) findViewById(R.id.btnConfiguracion);
        Button btnAcerca = (Button) findViewById(R.id.btnAcerca);
        Button btnSalir = (Button) findViewById(R.id.btnSalir);

        btnPrestamos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPrestamo();
            }
        });

        btnContactos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mostrarContactos();
            }
        });

        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarConfiguracion();
            }
        });

        btnAcerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta=new AlertDialog.Builder(MainActivity.this);
                alerta.setMessage("Desarrollado por ZenitSoft\n" +
                        "Brayan Jesus Gutierrez Esparza" +
                        "Julio Cesar Bautista Monsalvo").setTitle("PrestaTEC").show();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void mostrarConfiguracion() {
        Intent ventana = new Intent(this, Configuracion.class);
        startActivity(ventana);
    }

    private void mostrarContactos() {
        Intent ventana = new Intent(this,ContactoV.class);
        startActivity(ventana);
    }

    private void mostrarPrestamo() {
        Intent ventana = new Intent(this, vistaPrestamos.class);
        startActivity(ventana);
    }
}
