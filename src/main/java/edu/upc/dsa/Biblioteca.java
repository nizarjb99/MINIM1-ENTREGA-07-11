package edu.upc.dsa;

import edu.upc.dsa.models.*;
import java.util.List;

public interface Biblioteca {
    void addLector(Lector lector);
    Lector getLector(String lectorId);
    List<Lector> getLectors();


    void addLlibreEmmagatzemat(LlibreEmmagatzemat llibre);

    int getTotalMagatzem();
    int getNombreMunts();
    int getMidaMunt(int index);
    LlibreCatalogat catalogarSeguent();
    List<LlibreCatalogat> getCataleg();
    LlibreCatalogat getLlibreCatalogat(String isbn);
    Prestec prestar(String lectorId, String isbn);

    void retornar(String prestecId);
    List<Prestec> getPrestecsDeLector(String lectorId);
    List<Prestec> getPrestecsEnTramit(String lectorId);

    void clear();
    int sizeLectors();
    int sizeCataleg();
    int sizePrestecs();
}


