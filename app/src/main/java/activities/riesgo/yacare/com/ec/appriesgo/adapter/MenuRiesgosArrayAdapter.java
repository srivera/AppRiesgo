package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;


public class MenuRiesgosArrayAdapter extends ArrayAdapter<Riesgo> {
    private final Context context;
    private final ArrayList<Riesgo> values;
    private ImageView imagen;
    private Boolean actualizado;


    public  MenuRiesgosArrayAdapter(Context context, ArrayList<Riesgo> values, Boolean actualizado) {
        super(context, R.layout.elemento_emergencia_item, values);
        this.context = context;
        this.values = values;
        this.actualizado = actualizado;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.menu_riesgo_item, parent, false);
        final Riesgo riesgo = (Riesgo) values.get(position);
        TextView textView = (TextView) rowView.findViewById(R.id.nombreRiesgo);
        textView.setText(riesgo.getNombreRiesgo());
        textView.setTag(riesgo);
        Typeface fontBlack = Typeface.createFromAsset(context.getAssets(), "Lato-Black.ttf");
        textView.setTypeface(fontBlack);



        imagen = (ImageView) rowView.findViewById(R.id.imagenRiesgo);
        if(actualizado) {
            Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
            TextView textView1 = (TextView) rowView.findViewById(R.id.alertaRiesgo);
            textView1.setText(riesgo.getTipoAlerta().getNombre());
            textView1.setTypeface(fontRegular);
            if (riesgo.getEstadoRiesgo().equals("V")) {
                if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_AMARILLA)) {
                    imagen.setImageResource(R.drawable.amarilla);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_NARANJA)) {
                    imagen.setImageResource(R.drawable.naranja);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_ROJA)) {
                    imagen.setImageResource(R.drawable.roja);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_BLANCA)) {
                    imagen.setImageResource(R.drawable.gris);
                }
            }else  if (riesgo.getEstadoRiesgo().equals("F")) {
                if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_AMARILLA)) {
                    imagen.setImageResource(R.drawable.amarillafn);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_NARANJA)) {
                    imagen.setImageResource(R.drawable.naranjafn);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_ROJA)) {
                    imagen.setImageResource(R.drawable.rojafn);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_BLANCA)) {
                    imagen.setImageResource(R.drawable.fenomenonino);
                }

            }else {
                if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_AMARILLA)) {
                    imagen.setImageResource(R.drawable.tsuamarilla);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_NARANJA)) {
                    imagen.setImageResource(R.drawable.tsunaranja);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_ROJA)) {
                    imagen.setImageResource(R.drawable.tsuroja);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_BLANCA)) {
                    imagen.setImageResource(R.drawable.tsugris);
                } else if (riesgo.getAlertaActual().equals(Constantes.CODIGO_MENSAJE_VERDE)) {
                    imagen.setImageResource(R.drawable.tsugris);
                }
            }
        }else{
            if (riesgo.getEstadoRiesgo().equals("V")) {
                imagen.setImageResource(R.drawable.volcan);
            }else if (riesgo.getEstadoRiesgo().equals("F")) {
                imagen.setImageResource(R.drawable.fenomenonino);
            }else if (riesgo.getEstadoRiesgo().equals("T"))  {
                imagen.setImageResource(R.drawable.fenomenonino);
            }else if (riesgo.getEstadoRiesgo().equals("I"))  {
                imagen.setImageResource(R.drawable.maleta);
            }
        }
          return rowView;
    }


}
