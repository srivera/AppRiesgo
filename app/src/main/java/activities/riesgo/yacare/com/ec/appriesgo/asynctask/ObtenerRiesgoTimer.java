package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import android.os.AsyncTask;

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

import java.io.IOException;
import java.util.TimerTask;

import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class ObtenerRiesgoTimer extends TimerTask {

    private PrincipalActivity principalActivity;
    ObtenerRiesgoAsyncTask obtenerRiesgoAsyncTask;

    public ObtenerRiesgoTimer(PrincipalActivity principalActivity) {
        super();
        this.principalActivity = principalActivity;
    }


    public void run() {

        ObtenerRiesgoAsyncTask obtenerRiesgoAsyncTask = new ObtenerRiesgoAsyncTask(principalActivity);
        obtenerRiesgoAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    final class ObtenerRiesgoAsyncTask extends AsyncTask<String, Float, String> {

        PrincipalActivity principalActivity;

        ObtenerRiesgoAsyncTask(PrincipalActivity principalActivity) {
            super();
            this.principalActivity = principalActivity;
        }


   @Override
        protected String doInBackground(String... params) {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);

            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpPost httppost = new HttpPost(Constantes.URL_OBTENER_RIESGO);
            httppost.setHeader("content-type", "application/x-www-form-urlencoded");

            DatosAplicacion datosAplicacion = (DatosAplicacion) principalActivity.getApplicationContext();

            String respStr = "";
            try {
                String certificadoSeguridad = VariablesUtil.generarMD5( datosAplicacion.getRiesgoActual().getId()  + Constantes.CERTIFICADO_SEGURIDAD);
                String json = "{\"obtenerRiesgoV1\":{\"idriesgo\":\"" +  datosAplicacion.getRiesgoActual().getId()
                        + "\",\"certificadoseguridad\":\""+ certificadoSeguridad
                        + "\"}}";
                StringEntity se = new StringEntity(json);
                httppost.setEntity(se);
                HttpResponse resp = httpclient.execute(httppost);
                respStr = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return "ERR";

            } catch (IOException e) {
                e.printStackTrace();
                return "ERR";

            }
            return respStr;
        }

        protected void onPostExecute(String respuesta) {
            principalActivity.verificarObtenerRiesgo(respuesta);
        }

    }
}