package activities.riesgo.yacare.com.ec.appriesgo.dto;

/**
 * Created by yacare on 22/09/2015.
 */
public class Link {

    private String id;
    private String idRiesgo;
    private String nombre;
    private String url;
    private String tipo;
    private String icono;
    private String orden;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }
}
