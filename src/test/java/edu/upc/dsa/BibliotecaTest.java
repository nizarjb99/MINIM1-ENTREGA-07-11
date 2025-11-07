package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class BibliotecaTest {

    private Biblioteca b;

    @Before
    public void setUp() {
        b = BibliotecaImpl.getInstance();
        b.clear();
    }

    @After
    public void tearDown() {
        b.clear();
    }

    @Test
    public void testFuncionament() {
        Lector lector = new Lector("L1", "Nizar", "Jabri", "54001221G", new Date(), "Barcelona", "Carrer Fluvia 126");
        b.addLector(lector);

        LlibreEmmagatzemat l1 = new LlibreEmmagatzemat("F1", "ISBN01", "1984", "Casanova", 2020, 1, "Rowling", "Ficcio");
        LlibreEmmagatzemat l2 = new LlibreEmmagatzemat("F2", "ISBN01", "1984", "Casanova", 2020, 1, "Rowling", "Ficcio");

        b.addLlibreEmmagatzemat(l1);
        b.addLlibreEmmagatzemat(l2);

        LlibreCatalogat c1 = b.catalogarSeguent();
        LlibreCatalogat c2 = b.catalogarSeguent();

        Assert.assertEquals("ISBN01", c1.getIsbn());
        Assert.assertEquals("ISBN01", c2.getIsbn());
        Assert.assertEquals(1, b.sizeCataleg());
        Assert.assertEquals(2, b.getLlibreCatalogat("ISBN01").getExemplarsTotals());
        Assert.assertEquals(2, b.getLlibreCatalogat("ISBN01").getExemplarsDisponibles());

        Prestec p = b.prestar("L1", "ISBN01");
        Assert.assertNotNull(p);
        Assert.assertEquals(EstatPrestec.ENTRAMIT, p.getEstat());
        Assert.assertEquals(1, b.getLlibreCatalogat("ISBN01").getExemplarsDisponibles());
        Assert.assertEquals(1, b.getPrestecsEnTramit("L1").size());

        b.retornar(p.getId());
        Assert.assertEquals(2, b.getLlibreCatalogat("ISBN01").getExemplarsDisponibles());
        List<Prestec> tots = b.getPrestecsDeLector("L1");
        Assert.assertEquals(1, tots.size());
        Assert.assertEquals(EstatPrestec.RETORNAT, tots.get(0).getEstat());
    }

    @Test
    public void testMuntsMagatzem() {
        for (int i = 1; i <= 11; i++) {
            b.addLlibreEmmagatzemat(new LlibreEmmagatzemat("N3" + i, "ISBN" + i, "After" + i, "Alianza", 2000 + i, 1, "Garcia Lorca", "Entretinemnt"));
        }
        Assert.assertEquals(11, b.getTotalMagatzem());
        Assert.assertEquals(2, b.getNombreMunts());
        Assert.assertEquals(10, b.getMidaMunt(0));
        Assert.assertEquals(1, b.getMidaMunt(1));

        b.catalogarSeguent();
        Assert.assertEquals(10, b.getTotalMagatzem());
        Assert.assertEquals(2, b.getNombreMunts());
        Assert.assertEquals(9, b.getMidaMunt(0));

        for (int i = 0; i < 9; i++) b.catalogarSeguent();
        Assert.assertEquals(1, b.getNombreMunts());
        Assert.assertEquals(1, b.getMidaMunt(0));
    }

    @Test
    public void testCatalegandObtencio() {
        b.addLlibreEmmagatzemat(new LlibreEmmagatzemat("M1", "ISBN02", "Aventures a Girona", "Editorial6", 2021, 4, "Cervantes", "Comedia"));
        b.addLlibreEmmagatzemat(new LlibreEmmagatzemat("M2", "ISBN02", "Semafor verd", "Editorial3", 2021, 4, "Cervantes", "Terror"));
        b.catalogarSeguent();
        b.catalogarSeguent();

        Assert.assertEquals(1, b.sizeCataleg());
        Assert.assertNotNull(b.getLlibreCatalogat("ISBN02"));
        Assert.assertEquals(2, b.getLlibreCatalogat("ISBN02").getExemplarsTotals());
        Assert.assertEquals(2, b.getLlibreCatalogat("ISBN02").getExemplarsDisponibles());
    }

    @Test
    public void testLectors() {
        b.addLector(new Lector("L1", "Arnau", "Munte", "54678534N", new Date(), "Sant Feliu de Llobregat", "Carrer Roses 54"));
        b.addLector(new Lector("L2", "Manel", "Colominas", "54884538L", new Date(), "Eixample", "Carrer Blaves 32"));
        Assert.assertEquals(2, b.sizeLectors());
        Assert.assertEquals("Arnau", b.getLector("L1").getNom());
        Assert.assertEquals(2, b.getLectors().size());
    }

    @Test
    public void testPrestecSenseDisponibilitat() {
        Lector l = new Lector("P1", "Pablo", "Gonzalez", "56793421Y", new Date(), "Badalona", "Carrer Nou 78");
        b.addLector(l);
        b.addLlibreEmmagatzemat(new LlibreEmmagatzemat("F1", "ISBN04", "La vida es bella", "EditorialMA", 2023, 1, "Rowling", "Amor"));
        b.catalogarSeguent();

        Prestec p1 = b.prestar("P1", "ISBN04");
        Prestec p2 = b.prestar("P1", "ISBN04");

        Assert.assertNotNull(p1);
        Assert.assertNull(p2);
        Assert.assertEquals(0, b.getLlibreCatalogat("ISBN04").getExemplarsDisponibles());
    }
}


