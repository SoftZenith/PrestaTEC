package com.zenit.bryan.prestatec;

import java.io.Serializable;

public class PrestamosP implements Serializable {
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String nombre;
    private String objeto;
    private String especificacion;
    private String estado;
    private String observacion;
    private boolean color;
    private int id;
    private int iTipo;
    private int iContacto;
    public int getiTipo() {
        return iTipo;
    }

    public void setiTipo(int iTipo) {
        this.iTipo = iTipo;
    }

    public int getiContacto() {
        return iContacto;
    }

    public void setiContacto(int iContacto) {
        this.iContacto = iContacto;
    }

   public boolean isColor() {
        return color;
    }

    public String getfPrestamo() {
        return fPrestamo;
    }

    public void setfPrestamo(String fPrestamo) {
        this.fPrestamo = fPrestamo;
    }

    public void setfDevuelto(String fDevuelto) {
        this.fDevuelto = fDevuelto;
    }

    private String fPrestamo;

    public String getfDevuelto() {
        return fDevuelto;
    }

    private String fDevuelto;

  PrestamosP(int id, String n, String t, String o, String f, Boolean c)
    {
        this.id=id;
        nombre=n;
        objeto=t;
        especificacion=o;
        fDevuelto=f;
        color=c;
    }
    PrestamosP(int id, String n, String t, String o, String f)
    {
        this.id=id;
        nombre=n;
        objeto=t;
        especificacion=o;
        fDevuelto=f;
    }
    PrestamosP(int id, String n, String o, String e, String s, String ob, String fPrestamo, String fDevuelto) {
        this.id=id;
        nombre=n;
        objeto=o;
        especificacion=e;
        estado=s;
        observacion=ob;
        this.fPrestamo=fPrestamo;
        this.fDevuelto=fDevuelto;
    }


    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getEspecificacion() {
        return especificacion;
    }

    public void setEspecificacion(String especificacion) {
        this.especificacion = especificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
