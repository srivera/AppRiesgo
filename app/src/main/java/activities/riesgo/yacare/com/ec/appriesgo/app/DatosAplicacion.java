package activities.riesgo.yacare.com.ec.appriesgo.app;

import android.app.Application;
import android.location.Location;


import com.google.android.gms.maps.model.CameraPosition;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.alerta.DetalleAlertaFragment;
import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;
import activities.riesgo.yacare.com.ec.appriesgo.dto.auxiliar.ResumenCapa;
import activities.riesgo.yacare.com.ec.appriesgo.general.MenuActivity;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;


public class DatosAplicacion extends Application{


	private String idDispositivo;
	
	private String regId;

	private String regIdAzure;

	private Cuenta cuenta;


	private Location ultimaUbicacion;

	private Riesgo riesgoActual;

	private Riesgo riesgoMonitoreo;

	private ArrayList<ResumenCapa> resumenCapas = new ArrayList<ResumenCapa>();



	private PrincipalActivity principalActivity;

	private InicioFragment inicioFragment;

	private DetalleAlertaFragment detalleAlertaFragment;

	private ArrayList<Capa> lahares;

	private ChatBlueToothActivity chatBlueToothActivity;

	private MenuActivity menuActivity;

	public String modo;
	public String tipo;
	public Capa capa;
	public String titulo;

	public CameraPosition cameraPosition;
	/**
	 * Este m�todo se ejecuta cuando se carga la aplicaci�n por primera vez
	 *
	 */
	@Override
	public void onCreate(){

	}


	public Location getUltimaUbicacion() {
		return ultimaUbicacion;
	}

	public void setUltimaUbicacion(Location ultimaUbicacion) {
		this.ultimaUbicacion = ultimaUbicacion;
	}

	public ArrayList<ResumenCapa> getResumenCapas() {
		return resumenCapas;
	}

	public void setResumenCapas(ArrayList<ResumenCapa> resumenCapas) {
		this.resumenCapas = resumenCapas;
	}

	public Riesgo getRiesgoActual() {
		return riesgoActual;
	}

	public void setRiesgoActual(Riesgo riesgoActual) {
		this.riesgoActual = riesgoActual;
	}


	public PrincipalActivity getPrincipalActivity() {
		return principalActivity;
	}

	public void setPrincipalActivity(PrincipalActivity principalActivity) {
		this.principalActivity = principalActivity;
	}

	public InicioFragment getInicioFragment() {
		return inicioFragment;
	}

	public void setInicioFragment(InicioFragment inicioFragment) {
		this.inicioFragment = inicioFragment;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public String getIdDispositivo() {
		return idDispositivo;
	}

	public void setIdDispositivo(String idDispositivo) {
		this.idDispositivo = idDispositivo;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getRegIdAzure() {
		return regIdAzure;
	}

	public void setRegIdAzure(String regIdAzure) {
		this.regIdAzure = regIdAzure;
	}

	public DetalleAlertaFragment getDetalleAlertaFragment() {
		return detalleAlertaFragment;
	}

	public void setDetalleAlertaFragment(DetalleAlertaFragment detalleAlertaFragment) {
		this.detalleAlertaFragment = detalleAlertaFragment;
	}

	public ArrayList<Capa> getLahares() {
		return lahares;
	}

	public void setLahares(ArrayList<Capa> lahares) {
		this.lahares = lahares;
	}

	public ChatBlueToothActivity getChatBlueToothActivity() {
		return chatBlueToothActivity;
	}

	public void setChatBlueToothActivity(ChatBlueToothActivity chatBlueToothActivity) {
		this.chatBlueToothActivity = chatBlueToothActivity;
	}

	public Riesgo getRiesgoMonitoreo() {
		return riesgoMonitoreo;
	}

	public void setRiesgoMonitoreo(Riesgo riesgoMonitoreo) {
		this.riesgoMonitoreo = riesgoMonitoreo;
	}

	public MenuActivity getMenuActivity() {
		return menuActivity;
	}

	public void setMenuActivity(MenuActivity menuActivity) {
		this.menuActivity = menuActivity;
	}

}
