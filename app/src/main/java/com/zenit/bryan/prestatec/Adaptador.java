package com.zenit.bryan.prestatec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter {

    private Context context;
    private ArrayList<ContactoP> datos;

    public Adaptador(Context context, ArrayList datos)
    {
        super(context,R.layout.elemento_lista2,datos);
        this.context=context;
        this.datos=datos;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.elemento_lista2, null);
        //ContactoP c=datos.get(position);
        TextView relacion = (TextView) item.findViewById(R.id.xlbltituloi2);
        relacion.setText(datos.get(position).getRelacion());
        //relacion.setText(c.getRelacion());

        TextView nombre = (TextView) item.findViewById(R.id.xlblpersonai2);
        nombre.setText(datos.get(position).getNombre());

        //nombre.setText(c.getNombre());
        TextView telefono = (TextView) item.findViewById(R.id.xlblsubtituloi2);
        telefono.setText(datos.get(position).getTelefono());
        return item;
    }
}
