package com.example.freedb;

public class Pelicula {

    private long id;
    private String nom;
    private float valoracio;
    private String comentari;
    private String data;
    private byte[] foto;
    private long idGenere;
    private long idBSO;



    public Pelicula() {
        this.id = -1;
    }

    public String getNom() {
        return nom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getValoracio() {
        return valoracio;
    }

    public void setValoracio(float valoracio) {
        this.valoracio = valoracio;
    }

    public String getComentari() {
        return comentari;
    }

    public void setComentari(String comentari) {
        this.comentari = comentari;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) { this.foto = foto;
    }

    public long getIdGenere() {
        return idGenere;
    }

    public void setIdGenere(long idGenere) {
        this.idGenere = idGenere;
    }

    public long getIdBSO() {
        return idBSO;
    }

    public void setIdBSO(long idBSO) {
        this.idBSO = idBSO;
    }
}
