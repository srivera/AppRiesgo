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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.ContactoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Contacto;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class ObtenerReferenciasAsyncTask extends AsyncTask<String, Float, String>  {

	private ChatBlueToothActivity chatBlueToothActivity;
	private  Context context;

	public ObtenerReferenciasAsyncTask(Context context, ChatBlueToothActivity chatBlueToothActivity) {
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
		HttpPost httppost = new HttpPost(Constantes.URL_OBTENER_REFERENCIAS);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded");

		DatosAplicacion datosAplicacion = (DatosAplicacion) context;
		if(datosAplicacion.getCuenta() == null){
			CuentaDataSource cuentaDataSource = new CuentaDataSource(context);
			cuentaDataSource.open();
			datosAplicacion.setCuenta(cuentaDataSource.getCuenta());
			cuentaDataSource.close();
		}


		String respStr = "";
		try {
			String certificadoSeguridad = VariablesUtil.generarMD5(datosAplicacion.getCuenta().getId() + Constantes.CERTIFICADO_SEGURIDAD);
			String json =  "{\"obtenerReferencias\":{\"idPersona\":\""+ datosAplicacion.getCuenta().getId()
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
		if(!respuesta.equals("ERR")){
			Boolean statusFlag = null;
			JSONObject respuestaJSON = null;
			try {
				respuestaJSON = new JSONObject(respuesta);
				statusFlag = respuestaJSON.getBoolean("statusFlag");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if(statusFlag != null && statusFlag){
				JSONObject cuentaJSON = null;
				try {
					cuentaJSON = new JSONObject( respuestaJSON.getString("resultado"));
					JSONArray contactos = new JSONArray(cuentaJSON.getString("personas"));

					if(contactos.length() > 0) {
						ContactoDataSource contactoDataSource = new ContactoDataSource(context);
						contactoDataSource.open();
						contactoDataSource.deleteContactoByTipo("I");
						for (int i = 0, size = contactos.length(); i < size; i++) {
							JSONObject contactoInArray = contactos.getJSONObject(i);
							Contacto contactoBusqueda = contactoDataSource.getContactoId(contactoInArray.getString("id"));
							if(contactoBusqueda == null || contactoBusqueda.getId() == null) {
								Contacto contacto = new Contacto();
								contacto.setId(contactoInArray.getString("id"));
								contacto.setNombre(contactoInArray.getString("nombre") + " " + contactoInArray.getString("apellidopaterno"));
								contacto.setTipo("I");
								contactoDataSource.createContacto(contacto);
							}
						}
						contactoDataSource.close();
						if(contactos.length() > 0 && chatBlueToothActivity!= null) {
							chatBlueToothActivity.verificarContactos();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
