package com.zenit.bryan.prestatec;

import java.io.Serializable;


public class ContactoP implements Serializable {
    private String nombre;
    private String relacion;
    private int irelacion;
    private String telefono;
    private int id;

    ContactoP(String n, String r, String t) {
        nombre = n;
        relacion = r;
        telefono = t;
    }
    ContactoP(String n, String r, int re, String t) {
        nombre = n;
        relacion = r;
        telefono = t;
        irelacion=re;
    }
    ContactoP(int id, String n, String r, String t) {
        this.id=id;
        nombre = n;
        relacion = r;
        telefono = t;
    }
    ContactoP(int id, String n, String r, int re, String t) {
        this.id=id;
        nombre = n;
        irelacion=re;
        relacion = r;
        telefono = t;
    }

    public int getIrelacion() {
        return irelacion;
    }
    public void setIrelacion(int irelacion) {
        this.irelacion = irelacion;
    }
    public void setNombre(String n) {
        nombre=n;
    }
    public void setTelefono(String t){
        telefono=t;
    }
    public void setRelacion(String r)
    {
        relacion=r;
    }
    public int getId()
    {
        return id;
    }
    public String getRelacion(){
        return relacion;
    }
    public String getNombre()
    {
        return nombre;
    }
    public String getTelefono()
    {
        return telefono;
    }

}
