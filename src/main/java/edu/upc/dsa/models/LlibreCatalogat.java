package edu.upc.dsa.models;

import java.util.Objects;

public class LlibreCatalogat {

    private String isbn;
    private String titol;
    private String editorial;
    private int anyPublicacio;
    private int numeroEdicio;
    private String autor;
    private String tematica;

    private int exemplarsTotals;
    private int exemplarsDisponibles;

    public LlibreCatalogat() {}

    public LlibreCatalogat(String isbn, String titol, String editorial, int anyPublicacio,
                           int numeroEdicio, String autor, String tematica,
                           int exemplarsTotals, int exemplarsDisponibles) {
        this.isbn = isbn;
        this.titol = titol;
        this.editorial = editorial;
        this.anyPublicacio = anyPublicacio;
        this.numeroEdicio = numeroEdicio;
        this.autor = autor;
        this.tematica = tematica;
        this.exemplarsTotals = exemplarsTotals;
        this.exemplarsDisponibles = exemplarsDisponibles;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitol() { return titol; }
    public void setTitol(String titol) { this.titol = titol; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public int getAnyPublicacio() { return anyPublicacio; }
    public void setAnyPublicacio(int anyPublicacio) { this.anyPublicacio = anyPublicacio; }

    public int getNumeroEdicio() { return numeroEdicio; }
    public void setNumeroEdicio(int numeroEdicio) { this.numeroEdicio = numeroEdicio; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getTematica() { return tematica; }
    public void setTematica(String tematica) { this.tematica = tematica; }

    public int getExemplarsTotals() { return exemplarsTotals; }
    public void setExemplarsTotals(int exemplarsTotals) { this.exemplarsTotals = exemplarsTotals; }

    public int getExemplarsDisponibles() { return exemplarsDisponibles; }
    public void setExemplarsDisponibles(int exemplarsDisponibles) { this.exemplarsDisponibles = exemplarsDisponibles; }


    public void incrementarExemplars(int quantitat) {
        this.exemplarsTotals += quantitat;
        this.exemplarsDisponibles += quantitat;
    }


    public boolean decrementarDisponible() {
        if (exemplarsDisponibles > 0) {
            exemplarsDisponibles--;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "LlibreCatalogat{isbn='" + isbn + "', titol='" + titol + "', disponibles=" + exemplarsDisponibles + "}";
    }
}
