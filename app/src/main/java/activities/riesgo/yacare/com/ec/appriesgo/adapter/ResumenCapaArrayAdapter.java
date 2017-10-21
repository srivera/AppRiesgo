package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.dto.auxiliar.ResumenCapa;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.MapaPuntosFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.PuntosSegurosFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;


public class ResumenCapaArrayAdapter extends ArrayAdapter<ResumenCapa> {
    private final Context context;
    private final ArrayList<ResumenCapa> values;
    private PuntosSegurosFragment puntosSegurosFragment;
    private DatosAplicacion datosAplicacion;

    public ResumenCapaArrayAdapter(Context context, ArrayList<ResumenCapa> values, PuntosSegurosFragment puntosSegurosFragment) {
        super(context, R.layout.resumen_capa_item, values);
        this.context = context;
        this.values = values;
        this.puntosSegurosFragment = puntosSegurosFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.resumen_capa_item, parent, false);
        final ResumenCapa resumenCapa = (ResumenCapa) values.get(position);

        datosAplicacion = (DatosAplicacion) puntosSegurosFragment.getActivity().getApplicationContext();

        TextView textView3 = (TextView) rowView.findViewById(R.id.nombreTpoCapa);
        textView3.setText(resumenCapa.getNombre());


        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");

        TextView textView1 = (TextView) rowView.findViewById(R.id.nombreSitioCercano);

        TextView textView2 = (TextView) rowView.findViewById(R.id.leyendaCercania);

        ImageView imagenCapa = (ImageView) rowView.findViewById(R.id.imagenCapa);

        resumenCapa.setBtnVerTodos((Button) rowView.findViewById(R.id.btnVerTodaCapa));
        resumenCapa.setBtnVerPunto((Button) rowView.findViewById(R.id.btnVerCapaCercana));


        if (resumenCapa.getCapaCercana() != null) {
            //ZONA DE RIESGO
            if (resumenCapa.getTipocapa().equals(Constantes.CAPA_RUTA_EVACUACION)) {
                textView2.setText("La más cercana está aproximadamente a ");
                textView1.setText(Math.round(Double.valueOf(resumenCapa.getDistancia()) * 1000) + " MTS (la distancia es referencial)");
                imagenCapa.setImageResource(R.drawable.rutaevacuacion);
                resumenCapa.getBtnVerTodos().setText("ver todas");
            } else if (resumenCapa.getTipocapa().equals(Constantes.CAPA_ALBERGUES)) {
                //textView2.setText("El más cercano está en ");
                //textView1.setText(resumenCapa.getCapaCercana().getNombre());
                textView2.setText("Ubique los albergues en el mapa");
                textView1.setVisibility(View.GONE);
                imagenCapa.setImageResource(R.drawable.albergue);
                resumenCapa.getBtnVerPunto().setVisibility(View.GONE);
            } else if (resumenCapa.getTipocapa().equals(Constantes.CAPA_CENTRO_SALUD)) {
                //textView2.setText("El más cercano está en ");
                //textView1.setText(resumenCapa.getCapaCercana().getNombre());
                textView2.setText("Ubique los centros de salud en el mapa");
                textView1.setVisibility(View.GONE);
                imagenCapa.setImageResource(R.drawable.albergue);
                resumenCapa.getBtnVerPunto().setVisibility(View.GONE);
            } else if (resumenCapa.getTipocapa().equals(Constantes.CAPA_PUNTO_SEGURO)) {
                textView2.setText("El más cercano está en ");
                textView1.setText(resumenCapa.getCapaCercana().getNombre());
                imagenCapa.setImageResource(R.drawable.sitiosseguro);
            }
        } else {
            //ZONA SEGURA
            if (resumenCapa.getTipocapa().equals(Constantes.CAPA_RUTA_EVACUACION)) {
                imagenCapa.setImageResource(R.drawable.rutaevacuacion);
                textView1.setText("Ubique las rutas de evacuación en el mapa");
                resumenCapa.getBtnVerTodos().setText("ver todas");
            } else if (resumenCapa.getTipocapa().equals(Constantes.CAPA_ALBERGUES)) {
                imagenCapa.setImageResource(R.drawable.albergue);
                resumenCapa.getBtnVerPunto().setVisibility(View.GONE);
                textView1.setText("Ubique los albergues en el mapa");
            } else if (resumenCapa.getTipocapa().equals(Constantes.CAPA_CENTRO_SALUD)) {
                imagenCapa.setImageResource(R.drawable.salud);
                resumenCapa.getBtnVerPunto().setVisibility(View.GONE);
                textView1.setText("Ubique los centros de salud en el mapa");
            } else if (resumenCapa.getTipocapa().equals(Constantes.CAPA_PUNTO_SEGURO)) {
                imagenCapa.setImageResource(R.drawable.sitiosseguro);
                if(!datosAplicacion.getRiesgoActual().getId().equals("4")) {
                    textView1.setText("Ubique los sitios seguros en el mapa");
                }else{
                    textView1.setText("Ubique los puntos de encuentro en el mapa");
                }
            }

            textView2.setVisibility(View.GONE);
            resumenCapa.getBtnVerPunto().setVisibility(View.GONE);

        }


        textView1.setTypeface(fontRegular);
        textView2.setTypeface(fontRegular);
        textView1.setTypeface(fontRegular);


        resumenCapa.getBtnVerTodos().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapaPuntosFragment fragment = new MapaPuntosFragment();
                if (!resumenCapa.getTipocapa().equals(Constantes.CAPA_RUTA_EVACUACION)) {
                    datosAplicacion.titulo = "Todos los " + resumenCapa.getNombre();
                } else {
                    datosAplicacion.titulo = "Todas los " + resumenCapa.getNombre();
                }
                datosAplicacion.modo = "1";
                datosAplicacion.tipo = resumenCapa.getTipocapa();
                datosAplicacion.capa = resumenCapa.getCapaCercana();

                ((BaseContainerFragment) puntosSegurosFragment.getParentFragment()).replaceFragment(fragment, true);
            }
        });


        if (resumenCapa.getCapaCercana() != null) {
            resumenCapa.getBtnVerPunto().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapaPuntosFragment fragment = new MapaPuntosFragment();
                    if (!resumenCapa.getTipocapa().equals(Constantes.CAPA_RUTA_EVACUACION)) {
                        datosAplicacion.titulo =  resumenCapa.getNombre() + " más cercano ";
                    }else{
                        datosAplicacion.titulo =  resumenCapa.getNombre() + " más cercana ";
                    }

                    datosAplicacion.modo ="0";
                    datosAplicacion.tipo = resumenCapa.getTipocapa();
                    datosAplicacion.capa = resumenCapa.getCapaCercana();
                    fragment.latitudCercana = Double.valueOf(resumenCapa.getPuntoRutaEvacuacion().latitude);
                    fragment.longitudCercana = Double.valueOf(resumenCapa.getPuntoRutaEvacuacion().longitude);

                    ((BaseContainerFragment) puntosSegurosFragment.getParentFragment()).replaceFragment(fragment, true);
                }
            });
        } else {
            resumenCapa.getBtnVerPunto().setEnabled(false);
        }


        return rowView;
    }


}
