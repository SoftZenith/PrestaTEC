package com.zenit.bryan.prestatec;


public class CatalogoP {
    private int id;
    CatalogoP(int i, String n)
    {
        id=i;
        descripcion=n;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String descripcion;
}
