package com.zenit.bryan.prestatec;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ContactoV extends AppCompatActivity {

    ImageButton btnAgregar;
    ListView listContactos;
    ArrayList<ContactoP> lista;
    baseDatos bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_v);
        setTitle("Contactos");
        btnAgregar=(ImageButton)findViewById(R.id.xbtnIAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarRegistro("insertar");
            }
        });
        bd=new baseDatos(this,"proyecto",null,1);
        listContactos =(ListView)findViewById(R.id.xlstContacto);
        lista=new ListaDeContactos(this).buscarContacto();
        if(lista!=null) {
            Adaptador adapter = new Adaptador(this, lista);
            listContactos.setAdapter(adapter);
        }
        else
        {
            llamarRegistro("insertar");
        }
        registerForContextMenu(listContactos);
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("");
        inflater.inflate(R.menu.menucontextual,menu);
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
            case R.id.mlblItem2:{
                ContactoP c=(ContactoP)(lista.get(info.position));
                llamarRegistro(c,"actualizar");
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void recarga() {
        Intent actividad=new Intent(ContactoV.this,ContactoV.class);
        startActivity(actividad);
        finish();
    }
    private void eliminar(int dni)
    {
        final int id=dni;
        AlertDialog.Builder eliminarContacto=new AlertDialog.Builder(this);
        eliminarContacto.setTitle("").setMessage("¿Seguro que desea eliminar? \n"+"Se perderá todos sus prestamos");
        eliminarContacto.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String SQL="DELETE FROM CONTACTOS WHERE IDCON="+id;
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
    private void mensaje(String t, String m)
    {
            AlertDialog.Builder alerta=new AlertDialog.Builder(this);
            alerta.setMessage(m).setTitle(t).show();
    }
    private void mensajeT(String m)
    {
        Toast.makeText(this,m, Toast.LENGTH_LONG).show();
    }

    private void llamarRegistro(String s)
    {
        Intent actividad=new Intent(this,Contactos.class);
        actividad.putExtra("actividad",s);
        startActivityForResult(actividad,0);
    }

    private void llamarRegistro(ContactoP p,String s)
    {
        Intent actividad=new Intent(this,Contactos.class);
        actividad.putExtra("actividad",s);
        actividad.putExtra("contacto",p);
        startActivityForResult(actividad, 0);
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
        getMenuInflater().inflate(R.menu.menu_contacto_v, menu);
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
