package activities.riesgo.yacare.com.ec.appriesgo.general;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.microsoft.windowsazure.notifications.NotificationsHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.ContactoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.MensajeOffLineDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Contacto;
import activities.riesgo.yacare.com.ec.appriesgo.dto.MensajeOffLine;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;


public class MyHandler extends NotificationsHandler {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    //    NotificationCompat.Builder builder;
    Context ctx;


    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;

        String nhMessage = null;
        String titulo;

//        try {
//
//            nhMessage = URLDecoder.decode(bundle.getString("message"), "UTF-8");
//            titulo = URLDecoder.decode(bundle.getString("title"), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
            nhMessage = bundle.getString("message");
            titulo = bundle.getString("title");
//        }


        //String nhMessage = bundle.getString("message");nhMessage = bundle.getString("message");


        String[] mensaje = nhMessage.split(";");


        if (nhMessage.startsWith("CHAT")) {
            mNotificationManager = (NotificationManager)
                    ctx.getSystemService(Context.NOTIFICATION_SERVICE);

            ContactoDataSource contactoDataSource = new ContactoDataSource(context);
            contactoDataSource.open();
            Contacto contacto = contactoDataSource.getContactoId(mensaje[2]);
            contactoDataSource.close();

            String msgTitulo = "Ha recibido un mensaje ";


            PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                    new Intent(ctx, SplashActivity.class), 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(R.drawable.iconop)
                            .setContentTitle(msgTitulo)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(mensaje[1]))
                            .setSound(RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_NOTIFICATION))
                            .setContentText(mensaje[1])
                            .setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            MensajeOffLine mensajeOffLine = new MensajeOffLine();
            mensajeOffLine.setId(UUID.randomUUID().toString());
            mensajeOffLine.setEstado("PRO");
            mensajeOffLine.setFecha(dateFormat.format(date));
            mensajeOffLine.setIdContacto(mensaje[2]);
            mensajeOffLine.setMensaje(mensaje[1]);
            MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(context);
            mensajeOffLineDataSource.open();
            mensajeOffLineDataSource.createMensajeOffLineNew(mensajeOffLine);
            mensajeOffLineDataSource.close();

            DatosAplicacion datosAplicacion = (DatosAplicacion) context.getApplicationContext();

            if (datosAplicacion.getChatBlueToothActivity() != null) {
                datosAplicacion.getChatBlueToothActivity().verificarMensajes();
            }
        } else if (nhMessage.startsWith("MSJ") && mensaje.length == 7) {
            //Mensaje por zona

            Location location = VariablesUtil.getInstance().retornarUbicacion1(ctx, null);

            if(location != null) {
                Double distancia = VariablesUtil.calcularDistancia(location.getLatitude(), location.getLongitude(), Double.valueOf(mensaje[5]), Double.valueOf(mensaje[4]));
                Double radio = Double.valueOf(mensaje[6]);
                if (distancia <= radio) {
                    sendNotification(nhMessage, titulo);
                }
            }
        } else {
            sendNotification(nhMessage, titulo);
        }
    }

    private void sendNotification(String msg, String titulo) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        //Determinar los tipos de mensajes

        // AMA;id;Tenga precaución alerta amarilla;Se ha decretado alerta amarilla;
        // ROJ;id;Iniciar evacuación, tome precauciones;Se ha decretado alerta roja;
        // NAR;id;Deben evacuar ninos y ancianos;Se ha decretado alerta naranja;
        // BLA;id;No hay peligro, se ha decretado alerta blanca;
        // MSJ;id;Este atento a los mensajes del MICS;
        //{"data":{"message":"NAR;180;MENSAJE;MENSAJE RIESGO;", "title":"Evacuen todos"}}
        //{"data":{"message":"MSJ;180;MENSAJE;MENSAJE RIESGO;LAT;LON;RAD", "title":"Evacuen todos"}}
        //{"data":{"message":"MSJ;180;MENSAJE;MENSAJE RIESGO;LAT;LON;RAD", "title":"Evacuen todos"}}

        mostrarNotificacion(msg, titulo);

    }

    private void mostrarNotificacion(String msg, String titulo) {


        VariablesUtil.getInstance().enviarNotificacion(titulo, msg, mNotificationManager, ctx);
    }


}
