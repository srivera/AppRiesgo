package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.dto.MensajeOffLine;

public class MensajeArrayAdapter extends ArrayAdapter<MensajeOffLine> {
    private final Context context;
    private final ArrayList<MensajeOffLine> values;

    private ChatBlueToothActivity chatBlueToothActivity;

    public MensajeArrayAdapter(Context context, ArrayList<MensajeOffLine> values, ChatBlueToothActivity chatBlueToothActivity) {
        super(context, R.layout.mensajeol_item, values);
        this.context = context;
        this.values = values;
        this.chatBlueToothActivity = chatBlueToothActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mensajeol_item, parent, false);

        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
        Typeface fontBlack = Typeface.createFromAsset(context.getAssets(), "Lato-Black.ttf");

        final MensajeOffLine mensajeOffLine = (MensajeOffLine) values.get(position);

        TextView textView = (TextView) rowView.findViewById(R.id.txtFecha);
        textView.setText(mensajeOffLine.getFecha());
        textView.setTag(mensajeOffLine);
        textView.setTypeface(fontBlack);

        textView = (TextView) rowView.findViewById(R.id.txtMensaje);
        textView.setText(mensajeOffLine.getMensaje());
        textView.setTag(mensajeOffLine);

        textView.setTypeface(fontRegular);

        return rowView;
    }


}
