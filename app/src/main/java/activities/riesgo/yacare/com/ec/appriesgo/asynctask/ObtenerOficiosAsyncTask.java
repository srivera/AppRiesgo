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
import activities.riesgo.yacare.com.ec.appriesgo.boletin.BoletinFragment;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.BoletinDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Boletin;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class ObtenerOficiosAsyncTask extends AsyncTask<String, Float, String>  {

	private  BoletinFragment boletinFragment;
	private  Context context;

	public ObtenerOficiosAsyncTask(Context context, BoletinFragment boletinFragment) {
		super();
		this.boletinFragment = boletinFragment;
		this.context = context;
	}


	@Override
	protected String doInBackground(String... arg0) {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		HttpPost httppost = new HttpPost(Constantes.URL_OBTENER_OFICIOS);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8");

		DatosAplicacion datosAplicacion = (DatosAplicacion) boletinFragment.getActivity().getApplicationContext();
		BoletinDataSource boletinDataSource = new BoletinDataSource(context);
		boletinDataSource.open();
		Integer maximo = boletinDataSource.getMaxBoletinByRiesgo(datosAplicacion.getRiesgoActual().getId());
		if(maximo  - 10 < 0){
			maximo = 0;
		}
		boletinDataSource.close();

		String respStr = "";
		try {
			String certificadoSeguridad = VariablesUtil.generarMD5(maximo + datosAplicacion.getRiesgoActual().getId() + Constantes.CERTIFICADO_SEGURIDAD);
			String json =  "{\"obtenerOficios\":{\"idUltimo\":\""+ maximo
					+ "\",\"idRiesgo\":\""+ datosAplicacion.getRiesgoActual().getId()
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

			if(statusFlag != null  && statusFlag){
				String idCuenta = null;

				JSONObject cuentaJSON = null;
				try {
					cuentaJSON = new JSONObject( respuestaJSON.getString("resultado"));
					JSONArray boletines = new JSONArray(cuentaJSON.getString("boletines"));

					if(boletines.length() > 0) {
						BoletinDataSource boletinDataSource = new BoletinDataSource(context);
						boletinDataSource.open();
						for (int i = 0, size = boletines.length(); i < size; i++) {
							JSONObject equipoInArray = boletines.getJSONObject(i);

							Boletin boletinBusqueda = boletinDataSource.getBoletinByRiesgoId(equipoInArray.getString("idriesgo"),equipoInArray.getString("id") );

							if(boletinBusqueda != null){
								boletinDataSource.deleteBoletin(equipoInArray.getString("idriesgo"),equipoInArray.getString("id") );
							}

							Boletin boletin = new Boletin();
							boletin.setId(equipoInArray.getString("id"));
							boletin.setLeyenda(equipoInArray.getString("leyenda"));
							boletin.setIdRiesgo(equipoInArray.getString("idriesgo"));
							boletin.setUrl(equipoInArray.getString("url"));
							boletin.setTitulo(equipoInArray.getString("titulo"));
							boletin.setFecha(equipoInArray.getString("fechaformateada"));
							boletinDataSource.createBoletin(boletin);

						}
						boletinDataSource.close();
						if(boletines.length() > 0 && boletinFragment!= null) {
							boletinFragment.verificarBoletines(true);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
