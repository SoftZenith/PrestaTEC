package com.zenit.bryan.prestatec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AdaptadorPrestamos extends ArrayAdapter {
    private Context context;
    private ArrayList<PrestamosP> datos;

    public AdaptadorPrestamos(Context context, ArrayList datos)
    {
        super(context,R.layout.elemento_lista,datos);
        this.context=context;
        this.datos=datos;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.elemento_lista_prestamos, null);
        TextView nombre = (TextView) item.findViewById(R.id.xlblPersonaI);
        nombre.setText(datos.get(position).getNombre());

        TextView objeto = (TextView) item.findViewById(R.id.xlblObjetoI);
        objeto.setText(datos.get(position).getObjeto()+":"+datos.get(position).getEspecificacion());

        TextView fecha = (TextView) item.findViewById(R.id.xlblFechaI);
        fecha.setText(datos.get(position).getfPrestamo()+"-->"+datos.get(position).getfDevuelto());

        if(datos.get(position).isColor())
        {
            TextView alerta = (TextView) item.findViewById(R.id.xlblAlerta);
            alerta.setText("ALERTA!... ENTREGA RETRASADA");
        }
        return item;
    }
}
