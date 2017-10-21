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

import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class GuardarContactoAsyncTask extends AsyncTask<String, Float, String> {


	private ChatBlueToothActivity chatBlueToothActivity;
	private String accion;


	public GuardarContactoAsyncTask(ChatBlueToothActivity chatBlueToothActivity, String accion) {
		super();
		this.chatBlueToothActivity = chatBlueToothActivity;
		this.accion = accion;
	}

	@Override
	protected String doInBackground(String... arg0) {
		DatosAplicacion datosAplicacion;
		
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		
		int timeoutSocket = 30000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		HttpPost httppost = new HttpPost(Constantes.URL_GUARDAR_CONTACTO);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded");

		datosAplicacion  = ((DatosAplicacion) chatBlueToothActivity.getActivity().getApplicationContext());

		String respStr = "";
		String certificadoSeguridad = VariablesUtil.generarMD5(datosAplicacion.getCuenta().getId() + chatBlueToothActivity.getIdContactoAgregar() + accion + Constantes.CERTIFICADO_SEGURIDAD);
		try {
			String json =  "{\"guardarContacto\":{\"idOrigen\":\""+ datosAplicacion.getCuenta().getId()
					+ "\",\"idContacto\":\""+ chatBlueToothActivity.getIdContactoAgregar()
					+ "\",\"accion\":\""+ accion
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
		chatBlueToothActivity.verificarAgregarContacto(respuesta, accion);
	}
}
