package edu.upc.dsa.models;

import java.util.Date;

public class Prestec {

    private String id;
    private String lectorId;
    private String isbn;
    private Date dataPrestec;
    private Date dataRetornPrevist;
    private Date dataRetornReal;
    private EstatPrestec estat;

    public Prestec() {}

    public Prestec(String id, String lectorId, String isbn, Date dataPrestec, Date dataRetornPrevist) {
        this.id = id;
        this.lectorId = lectorId;
        this.isbn = isbn;
        this.dataPrestec = dataPrestec;
        this.dataRetornPrevist = dataRetornPrevist;
        this.estat = EstatPrestec.ENTRAMIT;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLectorId() { return lectorId; }
    public void setLectorId(String lectorId) { this.lectorId = lectorId; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Date getDataPrestec() { return dataPrestec; }
    public void setDataPrestec(Date dataPrestec) { this.dataPrestec = dataPrestec; }

    public Date getDataRetornPrevist() { return dataRetornPrevist; }
    public void setDataRetornPrevist(Date dataRetornPrevist) { this.dataRetornPrevist = dataRetornPrevist; }

    public Date getDataRetornReal() { return dataRetornReal; }
    public void setDataRetornReal(Date dataRetornReal) { this.dataRetornReal = dataRetornReal; }

    public EstatPrestec getEstat() { return estat; }
    public void setEstat(EstatPrestec estat) { this.estat = estat; }
}

