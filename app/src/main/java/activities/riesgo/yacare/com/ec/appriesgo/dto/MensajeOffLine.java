package activities.riesgo.yacare.com.ec.appriesgo.dto;




public class MensajeOffLine{
	private String id;
	private String mensaje;
	private String addressBlueTooth;
	private String estado;
	private String idContacto;
	private String latitud;
	private String longitud;
	private String fecha;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getAddressBlueTooth() {
		return addressBlueTooth;
	}
	public void setAddressBlueTooth(String addressBlueTooth) {
		this.addressBlueTooth = addressBlueTooth;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(String idContacto) {
		this.idContacto = idContacto;
	}
}
