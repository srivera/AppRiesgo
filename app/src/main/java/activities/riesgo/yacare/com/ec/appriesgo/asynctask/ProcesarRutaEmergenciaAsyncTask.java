package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.RutaEmergenciaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.dto.DetallePuntosEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.DetalleRutaEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.RutaEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.mapas.DirectionsJSONParser;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.RutasRetorno;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class ProcesarRutaEmergenciaAsyncTask extends AsyncTask<String, Float, String>  {

	private  Location ubicacion;
	private Context context;
	private Boolean insertarTabla;
	private ArrayList<RutaEmergencia> rutasEmergencia;

	public ProcesarRutaEmergenciaAsyncTask(Location ubicacion, Context context, Boolean insertarTabla) {
		super();
		this.ubicacion = ubicacion;
		this.context = context;
		this.insertarTabla = insertarTabla;
	}


	@Override
	protected String doInBackground(String... arg0) {

		if (ubicacion != null) {
			DatosAplicacion datosAplicacion = (DatosAplicacion) context.getApplicationContext();
			rutasEmergencia = new ArrayList<RutaEmergencia>();
			CapaDataSource capaDataSource = new CapaDataSource(context);
			capaDataSource.open();
			ArrayList<Capa> rutasEvacuacion = capaDataSource.getCapaByTipo(Constantes.CAPA_RUTA_EVACUACION,  datosAplicacion.getRiesgoActual().getId());
			capaDataSource.close();

			Capa capaUno = null;
			Capa capaDos = null;
			Capa capaTres = null;

			Double distanciaMenorUno = null;
			Double distanciaMenorDos = null;
			Double distanciaMenorTres = null;

			LatLng puntoMasCercaRutaUno = null;
			LatLng puntoMasCercaRutaDos = null;
			LatLng puntoMasCercaRutaTres = null;

			for (Capa capa : rutasEvacuacion) {
				String[] coordenadas = capa.getCoordenadas().split(" ");
				LatLng[] puntosRuta = new LatLng[coordenadas.length];
				int i = 0;

				Double distanciaMenorRuta = null;
				LatLng puntoMasCercaRuta = null;

				for (String punto : coordenadas) {
					String[] coordenada = punto.split(",");

					puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
					double distanciaCalculada = VariablesUtil.calcularDistancia(ubicacion.getLatitude(), ubicacion.getLongitude(),
							Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));

					if (distanciaMenorRuta == null || distanciaCalculada < distanciaMenorRuta) {
						distanciaMenorRuta = distanciaCalculada;
						puntoMasCercaRuta = puntosRuta[i];
					}
					i++;
				}

				if (distanciaMenorUno == null || distanciaMenorRuta < distanciaMenorUno) {
					distanciaMenorUno = distanciaMenorRuta;
					capaUno = capa;
					puntoMasCercaRutaUno = puntoMasCercaRuta;
				} else if (distanciaMenorDos == null || distanciaMenorRuta < distanciaMenorDos) {
					distanciaMenorDos = distanciaMenorRuta;
					capaDos = capa;
					puntoMasCercaRutaDos = puntoMasCercaRuta;
				} else if (distanciaMenorTres == null || distanciaMenorRuta < distanciaMenorTres) {
					distanciaMenorTres = distanciaMenorRuta;
					capaTres = capa;
					puntoMasCercaRutaTres = puntoMasCercaRuta;
				}
			}

			RutaEmergencia rutaEmergencia = new RutaEmergencia();
			rutaEmergencia.setCapa(capaUno);
			rutaEmergencia.setIdCapa(capaUno.getId());
			rutaEmergencia.setLatitudOrigen(String.valueOf(ubicacion.getLatitude()));
			rutaEmergencia.setLongitudOrigen(String.valueOf(ubicacion.getLongitude()));
			rutaEmergencia.setLatitudDestino(String.valueOf(puntoMasCercaRutaUno.latitude));
			rutaEmergencia.setLongitudDestino(String.valueOf(puntoMasCercaRutaUno.longitude));
			rutaEmergencia.setDistancia(String.valueOf(distanciaMenorUno));
			rutaEmergencia.setId("1");
			rutasEmergencia.add(rutaEmergencia);

			rutaEmergencia = new RutaEmergencia();
			rutaEmergencia.setCapa(capaDos);
			rutaEmergencia.setIdCapa(capaDos.getId());
			rutaEmergencia.setLatitudOrigen(String.valueOf(ubicacion.getLatitude()));
			rutaEmergencia.setLongitudOrigen(String.valueOf(ubicacion.getLongitude()));
			rutaEmergencia.setLatitudDestino(String.valueOf(puntoMasCercaRutaDos.latitude));
			rutaEmergencia.setLongitudDestino(String.valueOf(puntoMasCercaRutaDos.longitude));
			rutaEmergencia.setDistancia(String.valueOf(distanciaMenorDos));
			rutaEmergencia.setId("2");
			rutasEmergencia.add(rutaEmergencia);

			rutaEmergencia = new RutaEmergencia();
			rutaEmergencia.setCapa(capaTres);
			rutaEmergencia.setIdCapa(capaTres.getId());
			rutaEmergencia.setLatitudOrigen(String.valueOf(ubicacion.getLatitude()));
			rutaEmergencia.setLongitudOrigen(String.valueOf(ubicacion.getLongitude()));
			rutaEmergencia.setLatitudDestino(String.valueOf(puntoMasCercaRutaTres.latitude));
			rutaEmergencia.setLongitudDestino(String.valueOf(puntoMasCercaRutaTres.longitude));
			rutaEmergencia.setDistancia(String.valueOf(distanciaMenorTres));
			rutaEmergencia.setId("3");
			rutasEmergencia.add(rutaEmergencia);
		}
		return "";
	}

	protected void onPostExecute(String respuesta) {
		if (insertarTabla && rutasEmergencia.size() > 0) {

			RutaEmergenciaDataSource rutaEmergenciaDataSource = new RutaEmergenciaDataSource(context);
			rutaEmergenciaDataSource.open();
			rutaEmergenciaDataSource.deleteAllRutaEmergencia();
			rutaEmergenciaDataSource.close();

			for (RutaEmergencia rutaEmergenciaC : rutasEmergencia) {
				String url = getDirectionsUrl(new LatLng(Double.valueOf(rutaEmergenciaC.getLatitudOrigen()), Double.valueOf(rutaEmergenciaC.getLongitudOrigen())),
						new LatLng(Double.valueOf(rutaEmergenciaC.getLatitudDestino()), Double.valueOf(rutaEmergenciaC.getLongitudDestino())));

				DownloadTaskRutaEvacuacion downloadTask = new DownloadTaskRutaEvacuacion(rutaEmergenciaC, context);

				downloadTask.execute(url);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	// Fetches data from url passed
	private class DownloadTaskRutaEvacuacion extends AsyncTask<String, Void, String> {

		RutaEmergencia rutaEmergencia;
		Context context;

		public DownloadTaskRutaEvacuacion(RutaEmergencia rutaEmergencia, Context context) {
			this.rutaEmergencia = rutaEmergencia;
			this.context = context;
		}

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
			// For storing data from web service
			String data = "";
			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ParserTaskRutaEvacuacion parserTask = new ParserTaskRutaEvacuacion(rutaEmergencia, context);
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}

		/**
		 * A method to download json data from url
		 */
		private String downloadUrl(String strUrl) throws IOException {
			String data = "";
			InputStream iStream = null;
			HttpURLConnection urlConnection = null;
			try {
				URL url = new URL(strUrl);
				// Creating an http connection to communicate with url
				urlConnection = (HttpURLConnection) url.openConnection();
				// Connecting to url
				urlConnection.connect();
				// Reading data from url
				iStream = urlConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
				StringBuffer sb = new StringBuffer();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				data = sb.toString();
				br.close();
			} catch (Exception e) {
				Log.d("Exception", "error");
			} finally {
				iStream.close();
				urlConnection.disconnect();
			}
			return data;
		}
	}

	/**
	 * A class to parse the Google Places in JSON format
	 */
	private class ParserTaskRutaEvacuacion extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		RutaEmergencia rutaEmergencia;
		Context context;
		RutasRetorno rutasRetorno;

		public ParserTaskRutaEvacuacion(RutaEmergencia rutaEmergencia, Context context) {
			this.rutaEmergencia = rutaEmergencia;
			this.context = context;
		}

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;
			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				rutasRetorno = parser.parse(jObject);
				routes = rutasRetorno.routes;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			ArrayList<DetallePuntosEmergencia> listaPuntos = new ArrayList<DetallePuntosEmergencia>();
			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					DetallePuntosEmergencia detallePuntosEmergencia = new DetallePuntosEmergencia();
					detallePuntosEmergencia.setLatitud(String.valueOf(lat));
					detallePuntosEmergencia.setLongitud(String.valueOf(lng));
					detallePuntosEmergencia.setIdRuta(rutaEmergencia.getId());
					listaPuntos.add(detallePuntosEmergencia);
				}

			}

			rutaEmergencia.setDetallePuntos(listaPuntos);
			// Drawing polyline in the Google Map for the i-th route

			ArrayList<DetalleRutaEmergencia> listaRutas = new ArrayList<DetalleRutaEmergencia>();
			for (String texto : rutasRetorno.direcciones) {
				DetalleRutaEmergencia detalleRutaEmergencia = new DetalleRutaEmergencia();
				detalleRutaEmergencia.setInstruccion(texto.toString());
				detalleRutaEmergencia.setIdRuta(rutaEmergencia.getId());
				listaRutas.add(detalleRutaEmergencia);
			}
			rutaEmergencia.setDetalleRuta(listaRutas);

			RutaEmergenciaDataSource rutaEmergenciaDataSource = new RutaEmergenciaDataSource(context);
			rutaEmergenciaDataSource.open();
			rutaEmergenciaDataSource.createRutaEmergencia(rutaEmergencia);
			rutaEmergenciaDataSource.close();

		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
		String sensor = "sensor=false";
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&language=es" + "&mode=walking";
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
		return url;
	}
}
