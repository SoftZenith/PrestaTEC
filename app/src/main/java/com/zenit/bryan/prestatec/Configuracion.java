package com.zenit.bryan.prestatec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Configuracion extends Activity {
    Button btnCatalogo;
    Button btnRelaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Configuración");
        setContentView(R.layout.activity_configuracion);
        btnCatalogo=(Button)findViewById(R.id.xbtnCatalogo);
        btnRelaciones=(Button)findViewById(R.id.xbtnRelaciones);
        btnCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarConfiguracion("Catalogo");
            }
        });

        btnRelaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarConfiguracion("Relaciones");
            }
        });
    }

    private void llamarConfiguracion(String t)
    {
        Intent actividad=new Intent(this,Tipos.class);
        actividad.putExtra("ventana",t);
        startActivity(actividad);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
