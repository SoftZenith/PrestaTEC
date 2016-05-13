package com.zenit.bryan.prestatec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Prestamos extends Activity {
    TextView lblFecha,lblEstado;
    baseDatos bd;
    PrestamosP vPrestamo;
    Spinner sPersona, sTipo;
    EditText txtEspecificacion, txtObservacion;
    ImageButton btnAceptar,btnCancelar;
    static String idP, idT;
    Cursor c,c2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamos);
        setTitle("Préstamos");
        final String origen=getIntent().getStringExtra("actividad");
        vPrestamo=(PrestamosP)getIntent().getSerializableExtra("prestamo");
        btnAceptar = (ImageButton) findViewById(R.id.xbtnAceptarP);
        btnCancelar = (ImageButton) findViewById(R.id.xbtnCancelarP);
        lblFecha=(TextView)findViewById(R.id.xlblFecha);
        lblEstado=(TextView)findViewById(R.id.xlblEstado);
        txtEspecificacion=(EditText)findViewById(R.id.xtxtEspecificacion);
        txtObservacion=(EditText)findViewById(R.id.xtxtObservacion);
        lblFecha.setText(fechaActual());
        bd = new baseDatos(this, "proyecto", null, 1);
        lblFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
        sPersona = (Spinner) findViewById(R.id.xSpPersona);
        sTipo=(Spinner)findViewById(R.id.xSpTipo);
        c = llenarCombo();
        if (c != null) {
            SimpleCursorAdapter ad = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, new String[]{"NOMBRE"}, new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sPersona.setAdapter(ad);
        } else {
            mensajeT("Inserte contactos");
            finish();
        }
        sPersona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor c1 = (Cursor) parent.getItemAtPosition(position);
                Prestamos.idP = c1.getString(c1.getColumnIndex("_id"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        c2 = llenarCombo2();
        if (c2 != null) {
            SimpleCursorAdapter ad = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c2, new String[]{"TIPO"}, new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sTipo.setAdapter(ad);
        } else {
            mensajeT("Inserte catalogo");
            finish();
        }

        sTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor c1 = (Cursor) parent.getItemAtPosition(position);
                Prestamos.idT = c1.getString(c1.getColumnIndex("_id"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(origen.equals("actualizar")) {
            sPersona.setSelection(indice(vPrestamo.getiContacto()));
            sTipo.setSelection(indiceT(vPrestamo.getiTipo()));
            txtObservacion.setText(vPrestamo.getObservacion());
            txtEspecificacion.setText(vPrestamo.getEspecificacion());
            lblFecha.setText(vPrestamo.getfDevuelto());
        }
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(origen.equals("insertar")) {
                        if(txtEspecificacion.getText().toString().trim().isEmpty()) {
                            mensajeT("El campo especificación esta vacío");
                            return;
                        }
                        insertar();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                    {
                        if(txtEspecificacion.getText().toString().trim().isEmpty())
                        {
                            mensajeT("El campo especificación esta vacío");
                            return;
                        }
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

    private int indiceT(int identificador){
        int i=0;
        if(c2.moveToFirst())
        {
            do
            {
                if(c2.getInt(0)==identificador)
                    return i;
                i++;
            }while(c.moveToNext());
        }
        return -1;
    }

    private void insertar()
    {
        String SQL="INSERT INTO PRESTAMOS(PRESTATARIO,TIPO,OBJETO,ESTADO,OBSERVACIONES,FPRESTAMO,FDEVUELTO)" +
                "VALUES("+idP+","+idT+",'"+txtEspecificacion.getText().toString()+"'," +"'PENDIENTE',"+
                "'"+txtObservacion.getText().toString()+"','"+fechaActual()+"','"+lblFecha.getText().toString()+"')";
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
    private void actualizar() {
        SQLiteDatabase tablaPrestamo=bd.getWritableDatabase();
        if(idP.equals(String.valueOf(vPrestamo.getiContacto())))
            idP= String.valueOf(vPrestamo.getiContacto());
        if(idT.equals(String.valueOf(vPrestamo.getiTipo())))
            idT= String.valueOf(vPrestamo.getiTipo());
        String SQL="UPDATE PRESTAMOS SET PRESTATARIO="+idP+",TIPO="+idT+"," +
                "OBJETO='"+txtEspecificacion.getText().toString()+"'," +
                "OBSERVACIONES='"+txtObservacion.getText().toString()+"'," +
                "FDEVUELTO='"+lblFecha.getText().toString()+"'" +
                " WHERE IDP="+vPrestamo.getId();
        try{
            tablaPrestamo.execSQL(SQL);
            tablaPrestamo.close();
            mensajeT("Datos Actualizados");
        }catch(SQLiteException ex)
        {
            mensaje("ERROR",ex.getMessage());
        }
    }

    private void mensajeT(String m)
    {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }
    private void mensaje(String t, String s)
    {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setTitle(t).setMessage(s).show();
    }
    private void limpiar()
    {
        txtEspecificacion.setText("");
        txtObservacion.setText("");
        sPersona.setSelection(0);
        sTipo.setSelection(0);
    }
    private Cursor llenarCombo2()
    {
        SQLiteDatabase base = bd.getReadableDatabase();
        String SQL = "SELECT IDCAT AS _id, TIPO FROM CATALOGO";
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

    private Cursor llenarCombo()
    {
        SQLiteDatabase base = bd.getReadableDatabase();
        String SQL = "SELECT IDCON AS _id, NOMBRE FROM CONTACTOS";
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prestamos, menu);
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

    private String fechaActual()
    {
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(fecha);
    }
    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String mes=(monthOfYear+1)<10?("0"+ String.valueOf(monthOfYear+1)):(monthOfYear+1)+"";
                        String dia=(dayOfMonth)<10?("0"+ String.valueOf(dayOfMonth)):(dayOfMonth)+"";
                        lblFecha.setText(year+"-"+mes+"-"+dia);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }
}
