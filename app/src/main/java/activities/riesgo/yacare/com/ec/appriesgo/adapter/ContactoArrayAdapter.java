package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.GuardarContactoAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Contacto;

public class ContactoArrayAdapter extends ArrayAdapter<Contacto> {
    private final Context context;
    private final ArrayList<Contacto> values;

    private ChatBlueToothActivity chatBlueToothActivity;

    public ContactoArrayAdapter(Context context, ArrayList<Contacto> values, ChatBlueToothActivity chatBlueToothActivity) {
        super(context, R.layout.contacto_item, values);
        this.context = context;
        this.values = values;
        this.chatBlueToothActivity = chatBlueToothActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contacto_item, parent, false);

        Typeface fontBlack = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
        Typeface fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");

        final Contacto contacto = (Contacto) values.get(position);

        TextView textView = (TextView) rowView.findViewById(R.id.nombreContacto);
        textView.setText(contacto.getNombre());
        textView.setTag(contacto);

        textView.setTypeface(fontBlack);

        Button btnEliminarContacto = (Button) rowView.findViewById(R.id.btnEliminarContacto);
        btnEliminarContacto.setTypeface(fontAwesome);
        contacto.setEliminar(btnEliminarContacto);
        contacto.getEliminar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatBlueToothActivity.setIdContactoAgregar(contacto.getId());
                GuardarContactoAsyncTask guardarContactoAsyncTask = new GuardarContactoAsyncTask(chatBlueToothActivity, "D");
                guardarContactoAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        return rowView;
    }


}
