package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class GuardarPersonaTimer extends TimerTask {

    DatosAplicacion datosAplicacion;

    public GuardarPersonaTimer(DatosAplicacion datosAplicacion) {
        super();
        this.datosAplicacion = datosAplicacion;
    }


    public void run() {

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);

        int timeoutSocket = 30000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

        HttpClient httpclient = new DefaultHttpClient(httpParams);
        HttpPost httppost = new HttpPost(Constantes.URL_GUARDAR_PERSONA);
        httppost.setHeader("content-type", "application/x-www-form-urlencoded");

        Cuenta  cuenta = datosAplicacion.getCuenta();


        String respuesta = "";
        try {
            String certificadoSeguridad = VariablesUtil.generarMD5(datosAplicacion.getIdDispositivo() + Constantes.CERTIFICADO_SEGURIDAD);
            String json =  "{\"guardarPersona\":{\"cedulaidentidad\":\""+ cuenta.getNumeroDocumento()
                    + "\",\"primerNombre\":\""+ cuenta.getPrimerNombre()
                    + "\",\"segundoNombre\":\""+ cuenta.getSegundoNombre()
                    + "\",\"primerApellido\":\""+ cuenta.getPrimerApellido()
                    + "\",\"segundoApellido\":\""+ cuenta.getSegundoApellido()
                    + "\",\"ciudad\":\""+ cuenta.getCiudad()
                    + "\",\"sector\":\""+ cuenta.getSector()
                    + "\",\"tipoDocumento\":\""+ cuenta.getTipoDocumento()
                    + "\",\"email\":\""+ cuenta.getEmail()
                    + "\",\"cedulaidentidadPadre\":\""+ ""
                    + "\",\"idDispositivo\":\""+ datosAplicacion.getIdDispositivo()
                    + "\",\"idPushMessage\":\""+ datosAplicacion.getRegId()
                    + "\",\"idPushMessageAzure\":\""+ datosAplicacion.getRegIdAzure()
                    + "\",\"tipoDispositivo\":\""+ "ANDROID"
                    + "\",\"certificadoseguridad\":\""+ certificadoSeguridad
                    + "\"}}";

            StringEntity se = new StringEntity(json);
            httppost.setEntity(se);
            HttpResponse resp = httpclient.execute(httppost);
            respuesta = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);

            Boolean statusFlag = null;
            try {
                JSONObject respuestaJSON = new JSONObject(respuesta);
                statusFlag = respuestaJSON.getBoolean("statusFlag");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (statusFlag != null && statusFlag) {
                String idCuenta = null;
                try {
                    JSONObject respuestaJSON = new JSONObject(respuesta);
                    JSONObject respuestaId = new JSONObject(respuestaJSON.getString("resultado"));
                    idCuenta = respuestaId.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CuentaDataSource datasource = new CuentaDataSource(datosAplicacion.getApplicationContext());
                datasource.open();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                cuenta.setFechaCreacion(dateFormat.format(date));
                cuenta.setId(idCuenta);
                if(datosAplicacion.getCuenta() != null){
                    datasource.updateCuenta(cuenta);
                }else {
                    cuenta = datasource.createCuenta(cuenta);
                }
                datasource.close();
                datosAplicacion.setCuenta(cuenta);

                this.cancel();
           }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}