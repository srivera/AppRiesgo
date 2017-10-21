package activities.riesgo.yacare.com.ec.appriesgo.dto;

import android.widget.Button;

/**
 * Created by yacare on 22/09/2015.
 */
public class Boletin {


    private String id;
    private String idRiesgo;
    private String titulo;
    private String url;
    private String leyenda;
    private String fecha;

    private Button verBoletin;

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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLeyenda() {
        return leyenda;
    }

    public void setLeyenda(String leyenda) {
        this.leyenda = leyenda;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Button getVerBoletin() {
        return verBoletin;
    }

    public void setVerBoletin(Button verBoletin) {
        this.verBoletin = verBoletin;
    }
}
