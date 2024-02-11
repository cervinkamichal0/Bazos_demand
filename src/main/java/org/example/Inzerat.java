package org.example;

import java.util.Objects;

public class Inzerat {

    public String nadpis;
    public String popis;
    public String cena;
    public String lokace;
    public String datumVlozeni;
    public String img;
    public String url;

    public Inzerat(String nadpis, String popis, String cena, String lokace, String datumVlozeni, String img, String url) {
        this.nadpis = nadpis;
        this.popis = popis;
        this.cena = cena;
        this.lokace = lokace;
        this.datumVlozeni = datumVlozeni;
        this.img = img;
        this.url = url;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inzerat inzerat = (Inzerat) o;
        return Objects.equals(url, inzerat.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nadpis, popis, cena, lokace, datumVlozeni, img, url);
    }
}
