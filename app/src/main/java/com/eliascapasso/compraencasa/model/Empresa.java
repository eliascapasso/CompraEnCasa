package com.eliascapasso.compraencasa.model;

public class Empresa {
    private String idEmpresa;
    private String nombreEmpresa;
    private String nombreEncargado;
    private String ciudad;
    private String direccion;
    private String telefonoFijo;
    private String telefonoMovil;
    private Boolean tieneWsp;
    private String categoria;
    private String correo;
    private String urlFacebook;
    private String urlInstagram;
    private String imagenLogo;

    public Empresa(String idEmpresa, String facebook, String instagram, String imagenLogo, String correo, String categoria, String nombreEmpresa, String nombreEncargado, String ciudad, String direccion, String telefonoFijo, String telefonoMovil, Boolean tieneWsp) {
        this.nombreEmpresa = nombreEmpresa;
        this.nombreEncargado = nombreEncargado;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.telefonoFijo = telefonoFijo;
        this.telefonoMovil = telefonoMovil;
        this.tieneWsp = tieneWsp;
        this.idEmpresa = idEmpresa;
        this.categoria = categoria;
        this.correo = correo;
        this.imagenLogo = imagenLogo;
        this.urlFacebook = facebook;
        this.urlInstagram = instagram;
    }

    public Empresa(){

    }

    public String getUrlFacebook() {
        return urlFacebook;
    }

    public void setUrlFacebook(String urlFacebook) {
        this.urlFacebook = urlFacebook;
    }

    public String getUrlInstagram() {
        return urlInstagram;
    }

    public void setUrlInstagram(String urlInstagram) {
        this.urlInstagram = urlInstagram;
    }

    public String getImagenLogo() {
        return imagenLogo;
    }

    public void setImagenLogo(String imagenLogo) {
        this.imagenLogo = imagenLogo;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreEncargado() {
        return nombreEncargado;
    }

    public void setNombreEncargado(String nombreEncargado) {
        this.nombreEncargado = nombreEncargado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public Boolean getTieneWsp() {
        return tieneWsp;
    }

    public void setTieneWsp(Boolean tieneWsp) {
        this.tieneWsp = tieneWsp;
    }
}
