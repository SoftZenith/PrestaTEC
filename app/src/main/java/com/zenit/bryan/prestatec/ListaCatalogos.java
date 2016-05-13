package com.zenit.bryan.prestatec;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;


public class ListaCatalogos {
    baseDatos bd;
    Activity contexto;
    public ListaCatalogos(Activity a)
    {
        contexto=a;
        bd=new baseDatos(contexto,"proyecto",null,1);
    }
    public ArrayList<CatalogoP> buscarTipos() {
        CatalogoP p;
        SQLiteDatabase base = bd.getReadableDatabase();
        ArrayList<CatalogoP> elementos=new ArrayList<CatalogoP>();
        String SQL = "SELECT IDCAT, TIPO FROM CATALOGO";
        try {
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()){
                do {
                    p=new CatalogoP(resp.getInt(0),resp.getString(1));
                    elementos.add(p);
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

    public ArrayList<CatalogoP> buscarRelaciones() {
        CatalogoP p;
        SQLiteDatabase base = bd.getReadableDatabase();
        ArrayList<CatalogoP> elementos=new ArrayList<CatalogoP>();
        String SQL = "SELECT IDR, TIPO FROM RELACIONES";
        try {
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()){
                do {
                    p=new CatalogoP(resp.getInt(0),resp.getString(1));
                    elementos.add(p);
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
