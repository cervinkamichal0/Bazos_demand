package org.example;




import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Inzerat {

    public String nadpis;
    public String popis;
    public String cena;
    public String lokace;
    public LocalDate datumVlozeni;
    public String img;
    public String url;
    public String model;

    public Inzerat(String nadpis, String popis, String cena, String lokace, LocalDate datumVlozeni, String img, String url, String model) {
        this.nadpis = nadpis;
        this.popis = popis;
        this.cena = cena;
        this.lokace = lokace;
        this.datumVlozeni = datumVlozeni;
        this.img = img;
        this.url = url;
        this.model = model;
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
