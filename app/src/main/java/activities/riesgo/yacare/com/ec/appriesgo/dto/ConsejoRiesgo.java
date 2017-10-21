package activities.riesgo.yacare.com.ec.appriesgo.dto;

import android.widget.Button;

/**
 * Created by yacare on 22/09/2015.
 */
public class ConsejoRiesgo {

    private String id;
    private String idRiesgo;
    private String orden;
    private String nombre;
    private String icono;
    private String foto;
    private String archivo;
    private String titulo;


    private Button verDetalleConsejo;

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

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Button getVerDetalleConsejo() {
        return verDetalleConsejo;
    }

    public void setVerDetalleConsejo(Button verDetalleConsejo) {
        this.verDetalleConsejo = verDetalleConsejo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
