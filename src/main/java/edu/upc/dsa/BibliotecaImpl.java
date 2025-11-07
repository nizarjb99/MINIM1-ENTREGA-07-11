package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.util.*;

public class BibliotecaImpl implements Biblioteca {

    private static BibliotecaImpl instance;
    private final Map<String, Lector> lectors;
    private final Map<String, LlibreCatalogat> cataleg;
    private final Map<String, Prestec> prestecs;
    private final List<Deque<LlibreEmmagatzemat>> munts;
    private static final Logger logger = Logger.getLogger(BibliotecaImpl.class);

    private BibliotecaImpl() {
        this.lectors = new HashMap<>();
        this.cataleg = new HashMap<>();
        this.prestecs = new HashMap<>();
        this.munts = new LinkedList<>();
    }

    public static Biblioteca getInstance() {
        if (instance == null) instance = new BibliotecaImpl();
        return instance;
    }

    @Override
    public void addLector(Lector lector) {
        logger.info("addLector id=" + (lector != null ? lector.getId() : "null"));
        if (lector == null || lector.getId() == null) return;
        lectors.put(lector.getId(), lector);
        logger.info("lector afegit/actualitzat id=" + lector.getId());
    }

    @Override
    public Lector getLector(String lectorId) {
        logger.info("getLector id=" + lectorId);
        Lector l = lectors.get(lectorId);
        logger.info("getLector found=" + (l != null));
        return l;
    }

    @Override
    public List<Lector> getLectors() {
        logger.info("getLectors");
        return new ArrayList<>(lectors.values());
    }

    @Override
    public void addLlibreEmmagatzemat(LlibreEmmagatzemat llibre) {
        logger.info("addLlibreEmmagatzemat id=" + (llibre != null ? llibre.getId() : "null"));
        if (llibre == null) return;
        if (munts.isEmpty() || munts.get(munts.size() - 1).size() >= 10) {
            munts.add(new ArrayDeque<>());
        }
        munts.get(munts.size() - 1).addLast(llibre);
        logger.info("magatzem: munts=" + munts.size() + " darrerMida=" + munts.get(munts.size() - 1).size());
    }

    @Override
    public int getTotalMagatzem() {
        int total = 0;
        for (Deque<LlibreEmmagatzemat> d : munts) total += d.size();
        logger.info("getTotalMagatzem=" + total);
        return total;
    }

    @Override
    public int getNombreMunts() {
        int n = munts.size();
        logger.info("getNombreMunts=" + n);
        return n;
    }

    @Override
    public int getMidaMunt(int index) {
        int mida = (index >= 0 && index < munts.size()) ? munts.get(index).size() : 0;
        logger.info("getMidaMunt index=" + index + " mida=" + mida);
        return mida;
    }

    @Override
    public LlibreCatalogat catalogarSeguent() {
        logger.info("catalogarSeguent");
        if (munts.isEmpty()) {
            logger.info("catalogarSeguent buit");
            return null;
        }
        Deque<LlibreEmmagatzemat> primer = munts.get(0);
        if (primer.isEmpty()) {
            munts.remove(0);
            if (munts.isEmpty()) {
                logger.info("catalogarSeguent no hi ha llibres");
                return null;
            }
            primer = munts.get(0);
        }
        LlibreEmmagatzemat volum = primer.removeLast();
        if (primer.isEmpty()) munts.remove(0);
        LlibreCatalogat cat = cataleg.get(volum.getIsbn());
        if (cat == null) {
            cat = new LlibreCatalogat(
                    volum.getIsbn(),
                    volum.getTitol(),
                    volum.getEditorial(),
                    volum.getAnyPublicacio(),
                    volum.getNumeroEdicio(),
                    volum.getAutor(),
                    volum.getTematica(),
                    1,
                    1
            );
            cataleg.put(cat.getIsbn(), cat);
        } else {
            cat.incrementarExemplars(1);
        }
        logger.info("catalogat isbn=" + cat.getIsbn() + " totals=" + cat.getExemplarsTotals() + " disp=" + cat.getExemplarsDisponibles());
        return cat;
    }

    @Override
    public List<LlibreCatalogat> getCataleg() {
        logger.info("getCataleg");
        return new ArrayList<>(cataleg.values());
    }

    @Override
    public LlibreCatalogat getLlibreCatalogat(String isbn) {
        logger.info("getLlibreCatalogat isbn=" + isbn);
        return cataleg.get(isbn);
    }

    @Override
    public Prestec prestar(String lectorId, String isbn) {
        logger.info("prestar lectorId=" + lectorId + " isbn=" + isbn);
        if (lectorId == null || isbn == null) return null;
        if (!lectors.containsKey(lectorId)) {
            logger.error("prestar lector inexistent " + lectorId);
            return null;
        }
        LlibreCatalogat cat = cataleg.get(isbn);
        if (cat == null) {
            logger.error("prestar isbn inexistent " + isbn);
            return null;
        }
        if (!cat.decrementarDisponible()) {
            logger.error("prestar sense disponibilitat isbn=" + isbn);
            return null;
        }
        String id = UUID.randomUUID().toString();
        Prestec p = new Prestec(id, lectorId, isbn, new Date(), null);
        prestecs.put(p.getId(), p);
        logger.info("prestat idPrestec=" + p.getId());
        return p;
    }

    @Override
    public void retornar(String prestecId) {
        logger.info("retornar prestecId=" + prestecId);
        Prestec p = prestecs.get(prestecId);
        if (p == null) {
            logger.error("retornar prestec inexistent " + prestecId);
            return;
        }
        if (p.getEstat() == EstatPrestec.RETORNAT) {
            logger.info("retornar ja retornat " + prestecId);
            return;
        }
        p.setEstat(EstatPrestec.RETORNAT);
        p.setDataRetornReal(new Date());
        LlibreCatalogat cat = cataleg.get(p.getIsbn());
        if (cat != null) cat.setExemplarsDisponibles(cat.getExemplarsDisponibles() + 1);
        logger.info("retornat prestecId=" + prestecId + " isbn=" + p.getIsbn());
    }

    @Override
    public List<Prestec> getPrestecsDeLector(String lectorId) {
        logger.info("getPrestecsDeLector lectorId=" + lectorId);
        List<Prestec> out = new ArrayList<>();
        for (Prestec p : prestecs.values()) if (Objects.equals(p.getLectorId(), lectorId)) out.add(p);
        return out;
    }

    @Override
    public List<Prestec> getPrestecsEnTramit(String lectorId) {
        logger.info("getPrestecsEnTramit lectorId=" + lectorId);
        List<Prestec> out = new ArrayList<>();
        for (Prestec p : prestecs.values())
            if (Objects.equals(p.getLectorId(), lectorId) && p.getEstat() == EstatPrestec.ENTRAMIT) out.add(p);
        return out;
    }

    @Override
    public void clear() {
        logger.info("clear");
        lectors.clear();
        cataleg.clear();
        prestecs.clear();
        munts.clear();
    }

    @Override
    public int sizeLectors() {
        int s = lectors.size();
        logger.info("sizeLectors=" + s);
        return s;
    }

    @Override
    public int sizeCataleg() {
        int s = cataleg.size();
        logger.info("sizeCataleg=" + s);
        return s;
    }

    @Override
    public int sizePrestecs() {
        int s = prestecs.size();
        logger.info("sizePrestecs=" + s);
        return s;
    }
}
