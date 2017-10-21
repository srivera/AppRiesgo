package activities.riesgo.yacare.com.ec.appriesgo.verificacion;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.ResumenCapaArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.dto.auxiliar.ResumenCapa;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.PuntosSegurosFragment;
import activities.riesgo.yacare.com.ec.appriesgo.simulacro.SimulacroContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.simulacro.SimulacroFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;


public class PuntosSegurosVerificarFragment extends Fragment {

	private ListView listView;
	private ResumenCapaArrayAdapter resumenCapaArrayAdapter;

	private Button btnRegresar;

	private DatosAplicacion datosAplicacion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_verificar_zona, container, false);

		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
		if(getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("ZONAS SEGURAS");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}

		listView = (ListView) rootView.findViewById(android.R.id.list);


		btnRegresar = (Button) rootView.findViewById(R.id.btnRegresarVerificarZona);
		btnRegresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getParentFragment() instanceof VerificarContainerFragment){
					MapaFragment fragment = new MapaFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				}else if(getParentFragment() instanceof InicioContainerFragment){
					InicioFragment fragment = new InicioFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				}else if(getParentFragment() instanceof SimulacroContainerFragment){
					SimulacroFragment fragment = new SimulacroFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				}

			}
		});

		cargarResumenCapas();

		return rootView;
	}

	public void cargarResumenCapas(){
		ArrayList<ResumenCapa> resumenCapas;
		Location ubicacionActual = VariablesUtil.getInstance().retornarUbicacion1(getActivity().getApplicationContext(), null);
		if(ubicacionActual != null){
			datosAplicacion.setUltimaUbicacion(ubicacionActual);

			resumenCapas = new ArrayList<ResumenCapa>();
			CapaDataSource capaDataSource = new CapaDataSource(getActivity());
			capaDataSource.open();
			LatLng puntoMasCerca = null;
			Capa capaMasCercana = null;
			Double distanciaMenor = null;

			ArrayList<Capa> capas;
			ResumenCapa resumenCapa = new ResumenCapa();

			String zonaRiesgo = VariablesUtil.getInstance().verificarZonaRiesgo(new LatLng(ubicacionActual.getLatitude(),ubicacionActual.getLongitude()), getActivity().getApplicationContext(), datosAplicacion.getRiesgoActual().getId());

			if(zonaRiesgo.equals(Constantes.CODIGO_ZONA_RIESGO)) {
				//RUTA EVACUACION
				capas = capaDataSource.getCapaByTipo(Constantes.CAPA_RUTA_EVACUACION,  datosAplicacion.getRiesgoActual().getId());
				resumenCapa.setTipocapa(Constantes.CAPA_RUTA_EVACUACION);
				resumenCapa.setTotal(String.valueOf(capas.size()));
				resumenCapa.setNombre(Constantes.CAPA_RUTA_EVACUACION_NOMBRE);
				capaMasCercana = null;
				distanciaMenor = null;

				for (Capa capa : capas) {

					String[] coordenadas = capa.getCoordenadas().split(" ");
					LatLng[] puntosRuta = new LatLng[coordenadas.length];
					int i = 0;
					for (String punto : coordenadas) {
						String[] coordenada = punto.split(",");
						if (ubicacionActual != null) {
							puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
							double distanciaCalculada = VariablesUtil.calcularDistancia(ubicacionActual.getLatitude(), ubicacionActual.getLongitude(),
									Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
							if (distanciaMenor == null || distanciaCalculada < distanciaMenor) {
								capaMasCercana = capa;
								distanciaMenor = distanciaCalculada;
								puntoMasCerca = puntosRuta[i];
							}
							i++;
						}
					}


				}
				resumenCapa.setPuntoRutaEvacuacion(puntoMasCerca);
				resumenCapa.setCapaCercana(capaMasCercana);
				resumenCapa.setDistancia(String.valueOf(distanciaMenor));
				resumenCapas.add(resumenCapa);

				//PUNTO SEGURO
				capas = capaDataSource.getCapaByTipo(Constantes.CAPA_PUNTO_SEGURO,  datosAplicacion.getRiesgoActual().getId());
				resumenCapa = new ResumenCapa();
				resumenCapa.setTipocapa(Constantes.CAPA_PUNTO_SEGURO);
				resumenCapa.setTotal(String.valueOf(capas.size()));
				resumenCapa.setNombre(Constantes.CAPA_PUNTO_SEGURO_NOMBRE);
				capaMasCercana = null;
				distanciaMenor = null;
				for (Capa capa : capas) {
					String[] cord = capa.getCoordenadas().split(",");
					if (ubicacionActual != null) {
						double distanciaCalculada = VariablesUtil.calcularDistancia(ubicacionActual.getLatitude(), ubicacionActual.getLongitude(),
								Double.valueOf(cord[1]), Double.valueOf(cord[0]));
						if (distanciaMenor == null || distanciaCalculada < distanciaMenor) {
							puntoMasCerca = new LatLng(Double.valueOf(cord[1]), Double.valueOf(cord[0]));
							capaMasCercana = capa;
							distanciaMenor = distanciaCalculada;
						}
					}
				}
				resumenCapa.setPuntoRutaEvacuacion(puntoMasCerca);
				resumenCapa.setCapaCercana(capaMasCercana);
				resumenCapa.setDistancia(String.valueOf(distanciaMenor));
				resumenCapas.add(resumenCapa);

				//ALBERGUES
				capas = capaDataSource.getCapaByTipo(Constantes.CAPA_ALBERGUES,  datosAplicacion.getRiesgoActual().getId());
				resumenCapa = new ResumenCapa();
				resumenCapa.setTipocapa(Constantes.CAPA_ALBERGUES);
				resumenCapa.setTotal(String.valueOf(capas.size()));
				resumenCapa.setNombre(Constantes.CAPA_ALBERGUES_NOMBRE);

				for (Capa capa : capas) {
					String[] cord = capa.getCoordenadas().split(",");

					if (ubicacionActual != null) {
						double distanciaCalculada = VariablesUtil.calcularDistancia(ubicacionActual.getLatitude(), ubicacionActual.getLongitude(),
								Double.valueOf(cord[1]), Double.valueOf(cord[0]));
						if (distanciaMenor == null || distanciaCalculada < distanciaMenor) {
							capaMasCercana = capa;
							distanciaMenor = distanciaCalculada;
							puntoMasCerca = new LatLng(Double.valueOf(cord[1]), Double.valueOf(cord[0]));
						}
					}
				}
				resumenCapa.setPuntoRutaEvacuacion(puntoMasCerca);
				resumenCapa.setCapaCercana(capaMasCercana);
				resumenCapa.setDistancia(String.valueOf(distanciaMenor));
				resumenCapas.add(resumenCapa);


				capaDataSource.close();
				datosAplicacion.setResumenCapas(resumenCapas);
			}else{
				//Zona Segura
				//RUTA EVACUACION
				resumenCapa.setTipocapa(Constantes.CAPA_RUTA_EVACUACION);
				resumenCapa.setNombre(Constantes.CAPA_RUTA_EVACUACION_NOMBRE);
				resumenCapas.add(resumenCapa);

				resumenCapa = new ResumenCapa();
				resumenCapa.setTipocapa(Constantes.CAPA_PUNTO_SEGURO);
				resumenCapa.setNombre(Constantes.CAPA_PUNTO_SEGURO_NOMBRE);
				resumenCapas.add(resumenCapa);

				resumenCapa = new ResumenCapa();
				resumenCapa.setTipocapa(Constantes.CAPA_ALBERGUES);
				resumenCapa.setNombre(Constantes.CAPA_ALBERGUES_NOMBRE);
				resumenCapas.add(resumenCapa);

			}
		}else{
			resumenCapas = datosAplicacion.getResumenCapas();
		}
//		resumenCapaArrayAdapter = new ResumenCapaArrayAdapter(getActivity(), resumenCapas, PuntosSegurosFragment.this);
//		listView.setAdapter(resumenCapaArrayAdapter);
//		datosAplicacion.setResumenCapas(resumenCapas);

	}


}
