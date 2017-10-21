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
import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.MensajeOffLineDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.MensajeOffLine;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class EnviarMensajeAsyncTask extends AsyncTask<String, Float, String> {


	private ChatBlueToothActivity chatBlueToothActivity;
	private ArrayList<MensajeOffLine> mensajes;
	private PrincipalActivity principalActivity;


	public EnviarMensajeAsyncTask(ChatBlueToothActivity chatBlueToothActivity,  ArrayList<MensajeOffLine> mensajes, PrincipalActivity principalActivity) {
		super();
		this.chatBlueToothActivity = chatBlueToothActivity;
		this.mensajes = mensajes;
		this.principalActivity = principalActivity;
	}

	@Override
	protected String doInBackground(String... arg0) {

		DatosAplicacion datosAplicacion;
		
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		HttpPost httppost = new HttpPost(Constantes.URL_ENVIAR_MENSAJE);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded");

		String respStr = "";

		if(principalActivity != null ) {
			datosAplicacion = ((DatosAplicacion) principalActivity.getApplicationContext());


			try {
				for (MensajeOffLine mensajeOffLine : mensajes) {
					String certificadoSeguridad = VariablesUtil.generarMD5(mensajeOffLine.getIdContacto() + Constantes.CERTIFICADO_SEGURIDAD);
					String json = "{\"enviarMensaje\":{\"idcuenta\":\"" + mensajeOffLine.getIdContacto()
							+ "\",\"mensaje\":\"" + mensajeOffLine.getMensaje()
							+ "\",\"latitud\":\"" + "0"
							+ "\",\"longitud\":\"" + "0"
							+ "\",\"idmensaje\":\"" + mensajeOffLine.getId()
							+ "\",\"certificadoseguridad\":\""+ certificadoSeguridad
							+ "\"}}";

					StringEntity se = new StringEntity(json);
					httppost.setEntity(se);
					HttpResponse resp = httpclient.execute(httppost);
					respStr = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
					if (mensajeOffLine.getEstado().equals("PEN")) {
						mensajeOffLine.setEstado("PRO");
						MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(principalActivity);
						mensajeOffLineDataSource.open();
						mensajeOffLineDataSource.updateMensajeOffLine(mensajeOffLine);
						mensajeOffLineDataSource.close();
					} else {
						MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(principalActivity);
						mensajeOffLineDataSource.open();
						mensajeOffLineDataSource.deleteMensajeOffLine(mensajeOffLine.getId());
						mensajeOffLineDataSource.close();
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return "ERR";
			} catch (IOException e) {
				e.printStackTrace();
				return "ERR";
			}
		}
		return respStr;
	}
	
	protected void onPostExecute(String respuesta) {
		if(respuesta.equals("ERR") && chatBlueToothActivity != null) {
			chatBlueToothActivity.verificarEnviarMensaje(respuesta);
		}
	}
}
