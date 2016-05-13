package com.zenit.bryan.prestatec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kervin on 29/05/2015.
 */
public class AdaptadorCatalogo extends ArrayAdapter {
    private Context context;
    private ArrayList<CatalogoP> datos;
    private boolean respuesta;
    public AdaptadorCatalogo(Context context, ArrayList datos, boolean a)
    {
        super(context,R.layout.elemento_lista,datos);
        this.context=context;
        this.datos=datos;
        respuesta=a;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.elemento_catalogo_item, null);
        ImageView img=(ImageView)item.findViewById(R.id.ximgItem);
        if(respuesta)
            img.setImageDrawable(context.getResources().getDrawable(R.drawable.catalog));
        else
            img.setImageDrawable(context.getResources().getDrawable(R.drawable.contactos));
        TextView descripcion = (TextView) item.findViewById(R.id.xtxtDescripcion);
        descripcion.setText(datos.get(position).getDescripcion());
        return item;
    }
}

