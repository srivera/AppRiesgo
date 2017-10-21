package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.ElementoEmergenciaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.ConsejoRiesgo;
import activities.riesgo.yacare.com.ec.appriesgo.dto.ElementoEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InformacionUtilFragment;
import activities.riesgo.yacare.com.ec.appriesgo.recomendaciones.RecomendacionFragment;


public class ElementoArrayAdapter extends ArrayAdapter<ElementoEmergencia> {
    private final Context context;
    private final ArrayList<ElementoEmergencia> values;
    private ImageView imagen;
    private RecomendacionFragment recomendacionFragment;

    public ElementoArrayAdapter(Context context, ArrayList<ElementoEmergencia> values, RecomendacionFragment recomendacionFragment) {
        super(context, R.layout.elemento_emergencia_item, values);
        this.context = context;
        this.values = values;
        this.recomendacionFragment = recomendacionFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.elemento_emergencia_item, parent, false);
        final ElementoEmergencia elementoEmergencia = (ElementoEmergencia) values.get(position);
        TextView textView = (TextView) rowView.findViewById(R.id.nombreMochila);
        textView.setText(elementoEmergencia.getNombre());
        textView.setTag(elementoEmergencia);
        Typeface fontBlack = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
        textView.setTypeface(fontBlack);
        imagen = (ImageView) rowView.findViewById(R.id.imagenMochila);
        Resources res = context.getResources();
        int resID = res.getIdentifier(elementoEmergencia.getIcono().replace(".png", ""), "drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resID);
        imagen.setImageDrawable(drawable);

         CheckBox checkMochila = (CheckBox) rowView.findViewById(R.id.checkMochila);
        elementoEmergencia.setCheckBox(checkMochila);
        elementoEmergencia.setImageView(imagen);

        if(elementoEmergencia.getCheck().equals("1")){
            checkMochila.setChecked(true);
            imagen.setColorFilter(Color.parseColor("#36A0CE"));
        }else{
            imagen.setColorFilter(Color.parseColor("#acacac"));
        }


        checkMochila.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ){
                    elementoEmergencia.setCheck("1");
                    elementoEmergencia.getImageView().setColorFilter(Color.parseColor("#36A0CE"));
                    ElementoEmergenciaDataSource elementoEmergenciaDataSource = new ElementoEmergenciaDataSource(context);
                    elementoEmergenciaDataSource.open();
                    elementoEmergenciaDataSource.updateElementoEmergencia(elementoEmergencia);
                    elementoEmergenciaDataSource.close();
                    recomendacionFragment.verificarMochila();
                 }else{
                    elementoEmergencia.setCheck("0");
                    elementoEmergencia.getImageView().setColorFilter(Color.parseColor("#acacac"));
                    ElementoEmergenciaDataSource elementoEmergenciaDataSource = new ElementoEmergenciaDataSource(context);
                    elementoEmergenciaDataSource.open();
                    elementoEmergenciaDataSource.updateElementoEmergencia(elementoEmergencia);
                    elementoEmergenciaDataSource.close();
                }

            }
        });
        return rowView;
    }


}
