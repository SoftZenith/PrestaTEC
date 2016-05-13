package com.zenit.bryan.prestatec;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ListaPrestamos {
    baseDatos bd;
    Activity contexto;
    public ListaPrestamos(Activity a)
    {

        contexto=a;
        bd=new baseDatos(contexto,"proyecto",null,1);
    }
    public ArrayList<PrestamosP> buscarPrestamo() {
        PrestamosP p;
        SQLiteDatabase base = bd.getReadableDatabase();
        ArrayList<PrestamosP> elementos=new ArrayList<PrestamosP>();
        String SQL = "SELECT P.IDP,C.NOMBRE,CAT.TIPO,P.OBJETO,P.FDEVUELTO,P.PRESTATARIO,P.TIPO,P.OBSERVACIONES,P.FPRESTAMO FROM PRESTAMOS P, CATALOGO CAT, CONTACTOS C" +
                " WHERE P.PRESTATARIO=C.IDCON AND P.TIPO=CAT.IDCAT";
        try {
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()){
                do {
                    boolean a=convertir(resp.getString(4));
                    p=new PrestamosP(resp.getInt(0),resp.getString(1),resp.getString(2),resp.getString(3),resp.getString(4),a);
                    p.setiContacto(resp.getInt(5));
                    p.setiTipo(resp.getInt(6));
                    p.setObservacion(resp.getString(7));
                    p.setfPrestamo(resp.getString(8));
                    elementos.add(p);
                }while(resp.moveToNext());
            }
            else{
               // mensaje("Atención","NO HAY REGISTROS");
            }
            base.close();
        } catch (SQLiteException ex) {
            mensaje("ERROR","No se puede ejecutar el select");
        }
        return elementos;
    }

    public ArrayList<PrestamosP> buscarPrestamoDevuelto() {
        PrestamosP p;
        SQLiteDatabase base = bd.getReadableDatabase();
        ArrayList<PrestamosP> elementos=new ArrayList<PrestamosP>();
        String SQL = "SELECT IDREG,PRESTATARIO,TIPO,OBJETO,FENTREGADO,FPRESTAMO FROM REGRESADOS";
        try {
            Cursor resp=base.rawQuery(SQL,null);
            if(resp.moveToFirst()){
                do {
                    p=new PrestamosP(resp.getInt(0),resp.getString(1),resp.getString(2),resp.getString(3),resp.getString(4));
                    p.setfPrestamo(resp.getString(5));
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
    private boolean convertir(String f)
    {
        Date n=new Date();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date fe = format.parse(f);
            if(n.compareTo(fe)>0)
                return true;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    private void mensaje(String t, String s)
    {
        AlertDialog.Builder alerta=new AlertDialog.Builder(contexto);
        alerta.setTitle(t).setMessage(s).show();
    }
}

