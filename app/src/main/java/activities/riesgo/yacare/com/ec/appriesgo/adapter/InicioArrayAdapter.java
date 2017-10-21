package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
import activities.riesgo.yacare.com.ec.appriesgo.boletin.BoletinFragment;
import activities.riesgo.yacare.com.ec.appriesgo.dto.auxiliar.Opciones;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.AcercaDeFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.GuiaVisitaFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InformacionUtilFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.PoliticaPrivacidadFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.PuntosSegurosFragment;
import activities.riesgo.yacare.com.ec.appriesgo.oficial.InformacionOficialFragment;
import activities.riesgo.yacare.com.ec.appriesgo.recomendaciones.RecomendacionFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class InicioArrayAdapter extends ArrayAdapter<Opciones> {
    private final Context context;
    private final ArrayList<Opciones> values;
    private InicioFragment inicioFragment;

    public InicioArrayAdapter(Context context, ArrayList<Opciones> values, InicioFragment inicioFragment) {
        super(context, R.layout.inicio_item, values);
        this.context = context;
        this.values = values;
        this.inicioFragment = inicioFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.inicio_item, parent, false);
        final Opciones opcion = (Opciones)values.get(position);

        Typeface fontBold = Typeface.createFromAsset(context.getAssets(), "Lato-Bold.ttf");
        Typeface fontLight = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");


        TextView textView = (TextView) rowView.findViewById(R.id.nombreOpcionInicio);
        textView.setText(opcion.getNombre());
        textView.setTypeface(fontBold);

        TextView textView1 = (TextView) rowView.findViewById(R.id.leyendaOpcionInicio);
        textView1.setText(opcion.getLeyenda());
        textView1.setTypeface(fontLight);

        opcion.setBtnOpcion((Button) rowView.findViewById(R.id.btnItemInicio));
        opcion.getBtnOpcion().setBackgroundColor(Color.parseColor(((DatosAplicacion) context).getRiesgoActual().getTipoAlerta().getCodigoColorOscuro()));

        opcion.getBtnOpcion().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opcion.getId().equals("1")) {
                    InformacionUtilFragment fragment = new InformacionUtilFragment();
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("2")) {
                    RecomendacionFragment fragment = new RecomendacionFragment();
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("3")) {
                    PuntosSegurosFragment fragment = new PuntosSegurosFragment();
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("4")) {
                    BoletinFragment fragment = new BoletinFragment();
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("5")) {
                    InformacionOficialFragment fragment = new InformacionOficialFragment();
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("6")) {
                    PoliticaPrivacidadFragment fragment = new PoliticaPrivacidadFragment();
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("7")) {
                    AcercaDeFragment fragment = new AcercaDeFragment();
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("8")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "GUÍA DE VISITA");
                    args.putInt("codigo", 8);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("9")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "ECU 911");
                    args.putInt("codigo", 9);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("10")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "UPC");
                    args.putInt("codigo", 10);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("11")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "Botón Turismo Seguro");
                    args.putInt("codigo", 11);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("12")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "Sitios Públicos Seguros");
                    args.putInt("codigo", 12);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("13")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "Transporte Seguro");
                    args.putInt("codigo", 13);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("14")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "Salud");
                    args.putInt("codigo", 14);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("15")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "Información y Facilitación");
                    args.putInt("codigo", 15);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("16")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "Consulado Virtual");
                    args.putInt("codigo", 16);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("17")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "Fiscalía General");
                    args.putInt("codigo", 17);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("18")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "Cajeros Automáticos");
                    args.putInt("codigo", 18);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                } else if (opcion.getId().equals("19")) {
                    GuiaVisitaFragment fragment = new GuiaVisitaFragment();
                    Bundle args = new Bundle();
                    args.putString("titulo", "¡Descargue las aplicaciones!");
                    args.putInt("codigo", 19);
                    fragment.setArguments(args);
                    ((BaseContainerFragment) inicioFragment.getParentFragment()).replaceFragment(fragment, true);
                }
            }
        });



        ImageView imagenMenuInicio = (ImageView) rowView.findViewById(R.id.imagenOpcionInicio);

        if (opcion.getId().equals("1")) {
            imagenMenuInicio.setImageResource(R.drawable.info_util);
        } else if (opcion.getId().equals("2")) {
            imagenMenuInicio.setImageResource(R.drawable.recomendacion);
        } else if (opcion.getId().equals("3")) {
            imagenMenuInicio.setImageResource(R.drawable.punto_seguro);
        } else if (opcion.getId().equals("4")) {
            imagenMenuInicio.setImageResource(R.drawable.boletin);
        } else if (opcion.getId().equals("5")) {
            imagenMenuInicio.setImageResource(R.drawable.oficial);
        } else if (opcion.getId().equals("6")) {
            imagenMenuInicio.setImageResource(R.drawable.config);
        } else if (opcion.getId().equals("7")) {
            imagenMenuInicio.setImageResource(R.drawable.acerca);
        } else if (opcion.getId().equals("8")) {
            imagenMenuInicio.setImageResource(R.drawable.volcan);
        } else if (opcion.getId().equals("9")) {
            imagenMenuInicio.setImageResource(R.drawable.turecu911);
        } else if (opcion.getId().equals("10")) {
            imagenMenuInicio.setImageResource(R.drawable.turupc);
        } else if (opcion.getId().equals("11")) {
            imagenMenuInicio.setImageResource(R.drawable.turboton);
        } else if (opcion.getId().equals("12")) {
            imagenMenuInicio.setImageResource(R.drawable.tursitiopublico);
        } else if (opcion.getId().equals("13")) {
            imagenMenuInicio.setImageResource(R.drawable.turtransporte);
        } else if (opcion.getId().equals("14")) {
            imagenMenuInicio.setImageResource(R.drawable.tursalud);
        } else if (opcion.getId().equals("15")) {
            imagenMenuInicio.setImageResource(R.drawable.turinformacionturistica);
        } else if (opcion.getId().equals("16")) {
            imagenMenuInicio.setImageResource(R.drawable.turserviciolinea);
        } else if (opcion.getId().equals("17")) {
            imagenMenuInicio.setImageResource(R.drawable.turfiscalia);
        } else if (opcion.getId().equals("18")) {
            imagenMenuInicio.setImageResource(R.drawable.turcajero);
        } else if (opcion.getId().equals("19")) {
            imagenMenuInicio.setImageResource(R.drawable.turdescarga);
        }
        return rowView;
    }


}
