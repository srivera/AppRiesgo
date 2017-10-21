package activities.riesgo.yacare.com.ec.appriesgo.dto.auxiliar;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;

/**
 * Created by yacare on 22/09/2015.
 */
public class ResumenCapa implements Serializable{

    private String tipocapa;
    private String total;
    private Capa capaCercana;
    private String distancia;
    private Button btnVerTodos;
    private Button btnVerPunto;
    private String nombre;
    private LatLng puntoRutaEvacuacion;
    private ImageView imagen;

    public String getTipocapa() {
        return tipocapa;
    }

    public void setTipocapa(String tipocapa) {
        this.tipocapa = tipocapa;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Capa getCapaCercana() {
        return capaCercana;
    }

    public void setCapaCercana(Capa capaCercana) {
        this.capaCercana = capaCercana;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public Button getBtnVerTodos() {
        return btnVerTodos;
    }

    public void setBtnVerTodos(Button btnVerTodos) {
        this.btnVerTodos = btnVerTodos;
    }

    public Button getBtnVerPunto() {
        return btnVerPunto;
    }

    public void setBtnVerPunto(Button btnVerPunto) {
        this.btnVerPunto = btnVerPunto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LatLng getPuntoRutaEvacuacion() {
        return puntoRutaEvacuacion;
    }

    public void setPuntoRutaEvacuacion(LatLng puntoRutaEvacuacion) {
        this.puntoRutaEvacuacion = puntoRutaEvacuacion;
    }

    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(ImageView imagen) {
        this.imagen = imagen;
    }
}
