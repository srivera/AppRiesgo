package activities.riesgo.yacare.com.ec.appriesgo.dto;

/**
 * Created by yacare on 22/09/2015.
 */
public class Riesgo {

    private String id;
    private String nombreRiesgo;
    private String estadoRiesgo;
    private String fechaEstado;
    private String alertaActual;
    private String fechaAlerta;

    private TipoAlerta tipoAlerta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreRiesgo() {
        return nombreRiesgo;
    }

    public void setNombreRiesgo(String nombreRiesgo) {
        this.nombreRiesgo = nombreRiesgo;
    }

    public String getEstadoRiesgo() {
        return estadoRiesgo;
    }

    public void setEstadoRiesgo(String estadoRiesgo) {
        this.estadoRiesgo = estadoRiesgo;
    }

    public String getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(String fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public String getAlertaActual() {
        return alertaActual;
    }

    public void setAlertaActual(String alertaActual) {
        this.alertaActual = alertaActual;
    }

    public String getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(String fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }

    public TipoAlerta getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(TipoAlerta tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }
}
