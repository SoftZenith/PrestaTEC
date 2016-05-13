package com.zenit.bryan.prestatec;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;


public class ListaDeContactos {
    baseDatos bd;
    Activity contexto;
    public ListaDeContactos(Activity a)
    {

        contexto=a;
        bd=new baseDatos(contexto,"proyecto",null,1);
    }
    public ArrayList<ContactoP> buscarContacto() {
        ContactoP c;
        SQLiteDatabase base = bd.getReadableDatabase();
        ArrayList<ContactoP> elementos=new ArrayList<ContactoP>();
        String SQL = "SELECT C.IDCON,C.NOMBRE,R.TIPO,C.RELACION,C.TELEFONO FROM CONTACTOS C, RELACIONES R WHERE C.RELACION=R.IDR";
        try {
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()){
                do {
                    c=new ContactoP(resp.getInt(0),resp.getString(1),resp.getString(2),resp.getInt(3),resp.getString(4));
                    elementos.add(c);
                }while(resp.moveToNext());
            }
            else{
                //mensaje("Atención","NO HAY REGISTROS");
            }
            base.close();
        } catch (SQLiteException ex) {
            mensaje("ERROR","No se puede ejecutar el select");
        }
        return elementos;
    }

    private void mensaje(String t, String s)
    {
        AlertDialog.Builder alerta=new AlertDialog.Builder(contexto);
        alerta.setTitle(t).setMessage(s).show();
    }
}
