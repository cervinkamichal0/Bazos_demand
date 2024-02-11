package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static java.lang.Integer.valueOf;

public class Scraper {
    public static List<Inzerat> scrapeBazos(String searchTerm, int cenaOd, int cenaDo) {
        String scrapeUrl = "https://www.bazos.cz/search.php?hledat=" + searchTerm + "&rubriky=www&hlokalita=&humkreis=25&cenaod="+cenaOd+"&cenado=" + cenaDo + "&Submit=Hledat&order=&kitx=ano";
        if (cenaOd>cenaDo){
            throw new IllegalArgumentException("Cena od musi byt mensi nez cena do");
        }
        try{
            Document html = Jsoup.connect(scrapeUrl).get();
            Elements inzeraty=html.select(".inzeraty");

            String pocetInzeratuText = html.select(".inzeratynadpis").text();
            pocetInzeratuText = pocetInzeratuText.replaceAll("\\s","");
            Pattern pattern = Pattern.compile("inzerátůz(\\d+)");
            Matcher matcher = pattern.matcher(pocetInzeratuText);
            int pocetStranek = 0;
            if (matcher.find()) {
                pocetInzeratuText = matcher.group(1);
                pocetStranek = valueOf(pocetInzeratuText)/20;
                if (pocetStranek >= 40)
                    pocetStranek = 40;
            }
            else {
                throw new RuntimeException("Nepodarilo se ziskat pocet inzeratu");
            }

            String nadpis, popis, cena, lokace, datumVlozeni, img, url;
            List<Inzerat> inzeratyList = new ArrayList<>();
            Inzerat bazosInzerat;

            for(Element inzerat: inzeraty){
                nadpis = inzerat.select(".nadpis").text();
                popis = inzerat.select(".popis").text();
                cena = inzerat.select(".inzeratycena").text();
                lokace = inzerat.select(".inzeratylok").text();
                datumVlozeni = inzerat.select(".velikost10").text();
                img = inzerat.select(".obrazek").attr("src");
                InputStream input = new java.net.URL(img).openStream();
                url = inzerat.select("a").attr("href");
                bazosInzerat = new Inzerat(nadpis, popis, cena, lokace, datumVlozeni, img, url);
                inzeratyList.add(bazosInzerat);
            }

            for (int i = 1; i<= pocetStranek; i++){
                scrapeUrl = "https://www.bazos.cz/search.php?hledat=" + searchTerm + "&hlokalita=&humkreis=25&cenaod="+cenaOd +"&cenado="+cenaDo+"&order=&crz=" + i*20;

                html = Jsoup.connect(scrapeUrl).get();
                inzeraty = html.select(".inzeraty");
                for(Element inzerat: inzeraty){
                    nadpis = inzerat.select(".nadpis").text();
                    popis = inzerat.select(".popis").text();
                    cena = inzerat.select(".inzeratycena").text();
                    lokace = inzerat.select(".inzeratylok").text();
                    datumVlozeni = inzerat.select(".velikost10").text();
                    img = inzerat.select(".obrazek").attr("src");
                    url = inzerat.select("a").attr("href");
                    bazosInzerat = new Inzerat(nadpis, popis, cena, lokace, datumVlozeni, img, url);
                    inzeratyList.add(bazosInzerat);
                }
            }
            return inzeratyList;

        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }


}
