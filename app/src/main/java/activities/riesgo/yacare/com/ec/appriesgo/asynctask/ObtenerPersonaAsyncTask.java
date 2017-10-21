package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import android.content.Context;
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

import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class ObtenerPersonaAsyncTask extends AsyncTask<String, Float, String>  {

	private ChatBlueToothActivity chatBlueToothActivity;
	private  Context context;

	public ObtenerPersonaAsyncTask(Context context, ChatBlueToothActivity chatBlueToothActivity) {
		super();
		this.chatBlueToothActivity = chatBlueToothActivity;
		this.context = context;
	}


	@Override
	protected String doInBackground(String... arg0) {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		HttpPost httppost = new HttpPost(Constantes.URL_OBTENER_PERSONA);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded");

		String respStr = "";
		try {
			String certificadoSeguridad = VariablesUtil.generarMD5(chatBlueToothActivity.getNumeroDocumentoBusqueda() + Constantes.CERTIFICADO_SEGURIDAD);
			String json =  "{\"obtenerPersona\":{\"cedulaidentidad\":\""+ chatBlueToothActivity.getNumeroDocumentoBusqueda()
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
		if(chatBlueToothActivity != null){
			chatBlueToothActivity.verificarObtenerPersona(respuesta);
		}

	}
}
