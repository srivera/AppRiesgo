package activities.riesgo.yacare.com.ec.appriesgo.dto;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yacare on 22/09/2015.
 */
public class Capa implements Serializable{
// idriesgo, nombrecapa,  direccion, telefono, foto, latitud, longitud,tipocapa
    private String id;
    private String idRiesgo;
    private String nombre;
    private String direccion;
    private String telefono;
    private String foto;
    private String coordenadas;
    private String tipocapa;
    private String latitud;
    private String longitud;
    private String hueco1;
    private String hueco2;
    private String hueco3;


    private ArrayList< LatLng[]> puntosRutaH;


    public String getId() {
        return id;
    }

    public String getIdRiesgo() {
        return idRiesgo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFoto() {
        return foto;
    }


    public String getTipocapa() {
        return tipocapa;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdRiesgo(String idRiesgo) {
        this.idRiesgo = idRiesgo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setTipocapa(String tipocapa) {
        this.tipocapa = tipocapa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
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

    public ArrayList<LatLng[]> getPuntosRutaH() {
        return puntosRutaH;
    }

    public void setPuntosRutaH(ArrayList<LatLng[]> puntosRutaH) {
        this.puntosRutaH = puntosRutaH;
    }

    public String getHueco1() {
        return hueco1;
    }

    public void setHueco1(String hueco1) {
        this.hueco1 = hueco1;
    }

    public String getHueco2() {
        return hueco2;
    }

    public void setHueco2(String hueco2) {
        this.hueco2 = hueco2;
    }

    public String getHueco3() {
        return hueco3;
    }

    public void setHueco3(String hueco3) {
        this.hueco3 = hueco3;
    }
}
