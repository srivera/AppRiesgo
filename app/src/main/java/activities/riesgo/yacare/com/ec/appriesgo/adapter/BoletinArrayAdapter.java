package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.boletin.BoletinFragment;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Boletin;


public class BoletinArrayAdapter extends ArrayAdapter<Boletin> {
    private final Context context;
    private final ArrayList<Boletin> values;
    private BoletinFragment boletinFragment;
    private DatosAplicacion datosAplicacion;

    private static final String GOOGLE_DRIVE_PDF_READER_PREFIX = "http://drive.google.com/viewer?url=";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";

    public BoletinArrayAdapter(Context context, ArrayList<Boletin> values, BoletinFragment boletinFragment) {
        super(context, R.layout.boletin_item, values);
        this.context = context;
        this.values = values;
        this.boletinFragment = boletinFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.boletin_item, parent, false);
        final Boletin boletin = (Boletin) values.get(position);
        TextView dia = (TextView) rowView.findViewById(R.id.txtDiaBoletin);
        TextView mes = (TextView) rowView.findViewById(R.id.txtMesBoletin);
        TextView anio = (TextView) rowView.findViewById(R.id.txtAnioBoletin);
        TextView hora = (TextView) rowView.findViewById(R.id.txtHoraBoletin);
        TextView titulo = (TextView) rowView.findViewById(R.id.txtTituloBoletin);
        TextView leyenda = (TextView) rowView.findViewById(R.id.txtLeyendaBoletin);

        String[] fechaTotal = boletin.getFecha().split(" ");
        String[] fecha = fechaTotal[0].split("/");
        dia.setText(fecha[2]);
        anio.setText(fecha[0]);
        mes.setText(getMonth(Integer.valueOf(fecha[1])));
        hora.setText(fechaTotal[1]);

        titulo.setText(boletin.getTitulo());
        leyenda.setText(boletin.getLeyenda());

        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
        Typeface fontBold = Typeface.createFromAsset(context.getAssets(), "Lato-Bold.ttf");
        dia.setTypeface(fontBold);
        anio.setTypeface(fontBold);
        mes.setTypeface(fontBold);
        hora.setTypeface(fontRegular);
        titulo.setTypeface(fontRegular);
        leyenda.setTypeface(fontRegular);

        datosAplicacion = (DatosAplicacion) boletinFragment.getActivity().getApplicationContext();

        boletin.setVerBoletin((Button) rowView.findViewById(R.id.btnVerBoletin));
        boletin.getVerBoletin().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!datosAplicacion.getRiesgoActual().getId().equals("1") ) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse(boletin.getUrl()), HTML_MIME_TYPE);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                boletinFragment.getActivity().startActivity(i);
            }

            else

            {
                boletinFragment.verDetalleBoletin(boletin);
                }
            }
        });
        return rowView;
    }

    public String getMonth(int month) {
        switch (month) {
            case 1:
                return "ENE";

            case 2:
                return "FEB";

            case 3:
                return "MAR";

            case 4:
                return "ABR";

            case 5:
                return "MAY";

            case 6:
                return "JUN";

            case 7:
                return "JUN";

            case 8:
                return "AGO";

            case 9:
                return "SEP";

            case 10:
                return "OCT";

            case 11:
                return "NOV";

            case 12:
                return "DIC";
        }

        return "";
    }

}
