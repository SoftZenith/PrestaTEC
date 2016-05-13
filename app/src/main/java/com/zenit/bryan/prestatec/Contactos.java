package com.zenit.bryan.prestatec;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class Contactos extends Activity {
    baseDatos bd;
    Spinner combo;
    ImageButton btnAceptar;
    ImageButton btnCancelar;
    EditText txtNombre;
    EditText txtTelefono;
    static String id="0";
    Cursor c;
    ContactoP vContacto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        setTitle("Contactos");
        final String origen=getIntent().getStringExtra("actividad");
        vContacto=(ContactoP)getIntent().getSerializableExtra("contacto");
        bd = new baseDatos(this, "proyecto", null, 1);
        btnAceptar = (ImageButton) findViewById(R.id.xibtnAceptar);
        btnCancelar = (ImageButton) findViewById(R.id.xibtnCancelar);
        txtNombre=(EditText)findViewById(R.id.xtxtNombre);
        txtTelefono=(EditText)findViewById(R.id.xtxtTelefono);
        combo = (Spinner) findViewById(R.id.xSpRelaciones);
         c = llenarCombo();
        if (c != null) {
            SimpleCursorAdapter ad = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, new String[]{"TIPO"}, new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            combo.setAdapter(ad);
        } else {
            /*Intent actividad = new Intent(this, Tipos.class);
            actividad.putExtra("ventana", "Relaciones");
            startActivity(actividad);*/
            mensajeT("Inserte una relación\n"+"Menú-Configuración-Relaciones");
            finish();
        }

        combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor c1 = (Cursor) parent.getItemAtPosition(position);
                Contactos.id= c1.getString(c1.getColumnIndex("_id"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

               btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(origen.equals("insertar")) {
                        if(validar()) {
                            insertar();
                            setResult(RESULT_OK);
                            finish();
                        }
                        else
                            mensajeT("ERROR \n Campos vacíos");
                    }
                    else
                    {

                            actualizar();
                            setResult(RESULT_OK);
                            finish();

                    }
                }
                catch(Exception e)
                {
                    mensaje("ERROR",e.getMessage());
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
                finish();
            }
        });

        if(origen.equals("actualizar")) {
            txtNombre.setText(vContacto.getNombre());
            combo.setSelection(indice(vContacto.getIrelacion()));
            txtTelefono.setText(vContacto.getTelefono());
        }

    }

    private int indice(int identificador){
        int i=0;
        if(c.moveToFirst())
        {
            do
            {
                if(c.getInt(0)==identificador)
                    return i;
                i++;
            }while(c.moveToNext());
        }
        return -1;
    }
    private void actualizar() {
        SQLiteDatabase tablaAlumno=bd.getWritableDatabase();
        if(id.equals(String.valueOf(vContacto.getIrelacion())))
            id= String.valueOf(vContacto.getIrelacion());
        String SQL="UPDATE CONTACTOS SET NOMBRE='"+txtNombre.getText().toString()+"', RELACION="+id+"," +
                "TELEFONO='"+txtTelefono.getText().toString()+"'"+
                "WHERE IDCON="+vContacto.getId();
        try{
            tablaAlumno.execSQL(SQL);
            tablaAlumno.close();
            mensajeT("Datos Actualizados");
        }catch(SQLiteException ex)
        {
            mensaje("ERROR",ex.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contactos, menu);
        return true;
    }

    private Cursor llenarCombo()
    {
        SQLiteDatabase base = bd.getReadableDatabase();
        String SQL = "SELECT IDR AS _id, TIPO FROM RELACIONES";
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()) {
                bd.close();
                return resp;
            }
            else {
                bd.close();//Agregue un close
                return null;
            }
    }
    private void mensajeT(String m)
    {
        Toast.makeText(this,m, Toast.LENGTH_LONG).show();
    }
    private void mensaje(String t, String s)
    {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setTitle(t).setMessage(s).show();
    }


    private boolean validar()
    {
        if(txtTelefono.getText().toString().trim().isEmpty())
            return false;
        if(txtNombre.getText().toString().trim().isEmpty())
            return false;
        return true;
    }
   private void insertar()
    {
        String SQL="INSERT INTO CONTACTOS(NOMBRE, RELACION,TELEFONO) VALUES('"+txtNombre.getText().toString()+"',"+
                id+",'"+txtTelefono.getText().toString()+"')";
        try{
            SQLiteDatabase baseDat=bd.getWritableDatabase();
            baseDat.execSQL(SQL);
            baseDat.close();
            limpiar();
            //mensajeT("Se inserto correctamente");
        }catch(SQLiteException ex)
        {
            mensaje("ERROR",ex.getMessage());
        }
    }

    private void limpiar()
    {
        txtNombre.setText("");
        txtTelefono.setText("");
        combo.setSelection(0);
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
