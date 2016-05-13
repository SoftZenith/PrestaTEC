package com.zenit.bryan.prestatec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Tipos extends Activity {
    String titulo="";
    Button btnAgregar;
    baseDatos bd;
    ListView tipos;
    ArrayList<CatalogoP> lista;
    static boolean v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos);
        Bundle valores=getIntent().getExtras();
        bd=new baseDatos(this,"proyecto",null,1);
        titulo=valores.getString("ventana");
        v=titulo.equals("Catalogo");
        tipos=(ListView)findViewById(R.id.xlstCatalogo);
        setTitle(titulo);
        if(v)
            lista=new ListaCatalogos(this).buscarTipos();
        else
            lista=new ListaCatalogos(this).buscarRelaciones();
        if(lista!=null) {
            AdaptadorCatalogo adapter = new AdaptadorCatalogo(this, lista,v);
            tipos.setAdapter(adapter);
        }
        registerForContextMenu(tipos);
        btnAgregar=(Button)findViewById(R.id.xbtnAgregarT);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregar();
            }
        });
        registerForContextMenu(tipos);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("");
        inflater.inflate(R.menu.menucontextualcatalogo,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.mlblItem1:{
                int id=lista.get(info.position).getId();
                eliminar(id);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void eliminar(int ide) {
        final int id=ide;
        AlertDialog.Builder eliminarContacto=new AlertDialog.Builder(this);
        if (Tipos.v)
            eliminarContacto.setTitle("").setMessage("¿Seguro que desea eliminar? \n"+"Se perderá todos sus prestamos");
        else
            eliminarContacto.setTitle("").setMessage("¿Seguro que desea eliminar?");
        eliminarContacto.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String SQL;
                if (Tipos.v)
                    SQL = "DELETE FROM CATALOGO WHERE IDCAT=" + id;
                else
                    SQL = "DELETE FROM RELACIONES WHERE IDR=" + id;
                try {
                    SQLiteDatabase baseDato = bd.getWritableDatabase();
                    baseDato.execSQL(SQL);
                    baseDato.close();
                    //mensajeT("Registro Eliminado");
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
    private void recarga() {
        Intent actividad=new Intent(Tipos.this,Tipos.class);
        if(v)
            actividad.putExtra("ventana","Catalogo");
        else
            actividad.putExtra("ventana","Relaciones");
        startActivity(actividad);
        finish();
    }
    private void mensajeT(String m)
    {
        Toast.makeText(this,m, Toast.LENGTH_LONG).show();
    }
    private void agregar() {
        final EditText txtValor=new EditText(this);
        AlertDialog.Builder agregarTipo = new AlertDialog.Builder(this);
        agregarTipo.setTitle("Nuevo Registro").setMessage("Escriba el nuevo registro");
        agregarTipo.setView(txtValor);
        agregarTipo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (txtValor.getText().toString().trim().isEmpty()) {
                    mensajeT("Error \nCampo vacío");
                } else {
                    String SQL = "INSERT INTO " + titulo.toUpperCase() + "(TIPO) VALUES('" + txtValor.getText().toString() + "')";
                    try {
                        SQLiteDatabase tabla = bd.getWritableDatabase();
                        tabla.execSQL(SQL);
                        tabla.close();
                        recarga();
                        //mensajeT("REGISTRO GUARDADO");
                    } catch (SQLiteException ex) {
                        mensaje("ERROR", ex.getMessage());
                    }
                    dialog.dismiss();
                }
            }
        });

        agregarTipo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        agregarTipo.show();
    }


    private void mensaje(String t, String m)
    {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage(m).setTitle(t).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tipos, menu);
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
