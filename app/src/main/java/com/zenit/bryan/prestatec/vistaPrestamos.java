package com.zenit.bryan.prestatec;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class vistaPrestamos extends AppCompatActivity {

    ListView listaP;
    ArrayList<PrestamosP> lista;
    baseDatos bd;
    Button btnAgregar;
    CheckBox chActivos;
    ImageButton imgPapelera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_prestamos);

        setTitle("Préstamos");
        listaP=(ListView)findViewById(R.id.lstPrestamos);
        btnAgregar=(Button)findViewById(R.id.btnAgregar);
        imgPapelera=(ImageButton)findViewById(R.id.btnBorrar);
        imgPapelera.setVisibility(View.INVISIBLE);
        bd=new baseDatos(this,"proyecto",null,1);
        listaLlenado(this);
        chActivos=(CheckBox)findViewById(R.id.ckbPrestados);
        registerForContextMenu(listaP);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarRegistro("insertar");
            }
        });

        chActivos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    lista=new ListaPrestamos(vistaPrestamos.this).buscarPrestamoDevuelto();
                    AdaptadorPrestamos adapter = new AdaptadorPrestamos(vistaPrestamos.this, lista);
                    listaP.setAdapter(adapter);
                    registerForContextMenu(listaP);
                    imgPapelera.setVisibility(View.VISIBLE);
                }
                else
                {
                    listaLlenado(vistaPrestamos.this);
                    registerForContextMenu(listaP);
                    imgPapelera.setVisibility(View.INVISIBLE);
                }
            }
        });

        imgPapelera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaciar();
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("");
        if(chActivos.isChecked())
            inflater.inflate(R.menu.menu_vacio,menu);
        else
        {
            inflater.inflate(R.menu.menucontextualprestamo,menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.mlblItem0:{
                PrestamosP c=(PrestamosP)(lista.get(info.position));
                eliminarEntregado(c);
                return true;
            }
            case R.id.mlblItem1:{
                eliminar(lista.get(info.position).getId());
                return true;
            }
            case R.id.mlblItem2:{
                PrestamosP c=(PrestamosP)(lista.get(info.position));
                llamarRegistro(c,"actualizar");
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }

    }




    private void listaLlenado(Context a)
    {
        lista=new ListaPrestamos(this).buscarPrestamo();
        if(lista!=null) {
            AdaptadorPrestamos adapter = new AdaptadorPrestamos(a, lista);
            listaP.setAdapter(adapter);
        }
        else
        {
            llamarRegistro("insertar");
        }
    }

    private void llamarRegistro(String s)
    {
        Intent actividad = new Intent(this,Prestamos.class);
        actividad.putExtra("actividad",s);
        startActivityForResult(actividad,0);
    }

    private void llamarRegistro(PrestamosP p,String s)
    {
        Intent actividad=new Intent(this,Prestamos.class);
        actividad.putExtra("actividad",s);
        actividad.putExtra("prestamo",p);
        startActivityForResult(actividad, 0);
    }

    private void vaciar()
    {
        AlertDialog.Builder eliminarContacto=new AlertDialog.Builder(this);
        eliminarContacto.setTitle("").setMessage("¿Seguro que desea vaciar la papelera?");
        eliminarContacto.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String SQL="DELETE FROM REGRESADOS";
                try {
                    SQLiteDatabase baseDato=bd.getWritableDatabase();
                    baseDato.execSQL(SQL);
                    baseDato.close();
                    mensajeT("Papelera Vacía");
                    recarga();
                } catch (SQLiteException ex) {
                    mensaje("ERROR", ex.getMessage());
                }
                dialog.dismiss();
            }
        });

        eliminarContacto.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        eliminarContacto.show();
    }

    private void mensaje(String t,String m)
    {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage(m).setTitle(t).show();
    }
    private void mensajeT(String m)
    {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    private void recarga() {
        Intent actividad=new Intent(vistaPrestamos.this,vistaPrestamos.class);
        startActivity(actividad);
        finish();
    }

    private void eliminarEntregado(PrestamosP pe)
    {
        final int id=pe.getId();
        final PrestamosP p=pe;
        AlertDialog.Builder eliminarContacto=new AlertDialog.Builder(this);
        eliminarContacto.setTitle("").setMessage("¿Seguro que desea entregarlo?");
        eliminarContacto.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String SQL1="INSERT INTO REGRESADOS(PRESTATARIO,TIPO,OBJETO,OBSERVACIONES,FPRESTAMO,FENTREGADO) " +
                        "VALUES('"+p.getNombre()+"','"+p.getObjeto()+"','"+p.getEspecificacion()+"','ENTREGADO','"+p.getfPrestamo()+"','"+fechaActual()+"')";
                String SQL="DELETE FROM PRESTAMOS WHERE IDP="+id;
                try {
                    SQLiteDatabase baseDato=bd.getWritableDatabase();
                    baseDato.execSQL(SQL1);
                    baseDato.execSQL(SQL);
                    baseDato.close();
                    recarga();
                } catch (SQLiteException ex) {
                    mensaje("ERROR", ex.getMessage());
                }
                dialog.dismiss();
            }
        });

        eliminarContacto.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        eliminarContacto.show();
    }

    private String fechaActual()
    {
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(fecha);
    }

    private void eliminar(int dni)
    {
        final int id=dni;
        AlertDialog.Builder eliminarContacto=new AlertDialog.Builder(this);
        eliminarContacto.setTitle("").setMessage("¿Seguro que desea eliminar?");
        eliminarContacto.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String SQL="DELETE FROM PRESTAMOS WHERE IDP="+id;
                try {
                    SQLiteDatabase baseDato=bd.getWritableDatabase();
                    baseDato.execSQL(SQL);
                    baseDato.close();
                    mensajeT("Registro Eliminado");
                    recarga();
                } catch (SQLiteException ex) {
                    mensaje("ERROR", ex.getMessage());
                }
                dialog.dismiss();
            }
        });

        eliminarContacto.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        eliminarContacto.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (RESULT_OK == resultCode) {
            recarga();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vista_prestamos, menu);
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
