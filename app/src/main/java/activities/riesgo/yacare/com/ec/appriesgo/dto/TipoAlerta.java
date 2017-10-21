package activities.riesgo.yacare.com.ec.appriesgo.dto;

import android.widget.Button;

import java.io.Serializable;

/**
 * Created by yacare on 22/09/2015.
 */
public class TipoAlerta implements Serializable{

    private String id;
    private String codigoTipoAlerta;
    private String nombre;
    private String leyendaCorta;
    private String codigoColor;
    private String codigoColorOscuro;
    private String codigoColorLetra;
    private String icono;
    private String pregunta;
    private String leyendaLarga;
    private String idRiesgo;

    private Button verTipoAlerta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoTipoAlerta() {
        return codigoTipoAlerta;
    }

    public void setCodigoTipoAlerta(String codigoTipoAlerta) {
        this.codigoTipoAlerta = codigoTipoAlerta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLeyendaCorta() {
        return leyendaCorta;
    }

    public void setLeyendaCorta(String leyendaCorta) {
        this.leyendaCorta = leyendaCorta;
    }

    public String getCodigoColor() {
        return codigoColor;
    }

    public void setCodigoColor(String codigoColor) {
        this.codigoColor = codigoColor;
    }

    public String getCodigoColorOscuro() {
        return codigoColorOscuro;
    }

    public void setCodigoColorOscuro(String codigoColorOscuro) {
        this.codigoColorOscuro = codigoColorOscuro;
    }

    public String getCodigoColorLetra() {
        return codigoColorLetra;
    }

    public void setCodigoColorLetra(String codigoColorLetra) {
        this.codigoColorLetra = codigoColorLetra;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getLeyendaLarga() {
        return leyendaLarga;
    }

    public void setLeyendaLarga(String leyendaLarga) {
        this.leyendaLarga = leyendaLarga;
    }

    public String getIdRiesgo() {
        return idRiesgo;
    }

    public void setIdRiesgo(String idRiesgo) {
        this.idRiesgo = idRiesgo;
    }

    public Button getVerTipoAlerta() {
        return verTipoAlerta;
    }

    public void setVerTipoAlerta(Button verTipoAlerta) {
        this.verTipoAlerta = verTipoAlerta;
    }
}
