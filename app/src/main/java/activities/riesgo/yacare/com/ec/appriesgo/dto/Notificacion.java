package activities.riesgo.yacare.com.ec.appriesgo.dto;

/**
 * Created by yacare on 22/09/2015.
 */
public class Notificacion {

    private String id;
    private String idRiesgo;
    private String mensaje;
    private String fecha;
    private String titulo;
    private String nombreRiesgo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRiesgo() {
        return idRiesgo;
    }

    public void setIdRiesgo(String idRiesgo) {
        this.idRiesgo = idRiesgo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreRiesgo() {
        return nombreRiesgo;
    }

    public void setNombreRiesgo(String nombreRiesgo) {
        this.nombreRiesgo = nombreRiesgo;
    }
}
