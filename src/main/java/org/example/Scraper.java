package org.example;

import org.jetbrains.annotations.Nullable;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

public class Scraper {
    @Nullable
    public static List<Inzerat> scrapeBazos(String searchTerm, int cenaOd, int cenaDo, String auto) {
        String scrapeUrl = "https://www.bazos.cz/search.php?hledat=" + searchTerm + "&rubriky=www&hlokalita=&humkreis=25&cenaod="+cenaOd+"&cenado=" + cenaDo + "&Submit=Hledat&order=&kitx=ano";
        if (cenaOd>cenaDo){
            throw new IllegalArgumentException("Cena od musi byt mensi nez cena do");
        }
        try{
            Document html = Jsoup.connect(scrapeUrl).get();
            Elements inzeraty=html.select(".inzeraty");
            String regex = "\\[(\\d{1,2}\\.\\d{1,2}\\. \\d{4})]";
            Pattern patternDate = Pattern.compile(regex);
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
                System.out.println("Nepodarilo se ziskat pocet inzeratu");
            }

            String nadpis, popis, lokace, datumVlozeni, img, url, model;
            LocalDate date;
            int cena;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M. yyyy");
            formatter = formatter.withLocale( Locale.getDefault() );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
            List<Inzerat> inzeratyList = new ArrayList<>();
            Inzerat bazosInzerat;

            for(Element inzerat: inzeraty){
                nadpis = inzerat.select(".nadpis").text();
                popis = inzerat.select(".popis").text();
                cena = Integer.parseInt(inzerat.select(".inzeratycena").text().replaceAll(" Kč","").replaceAll(" ",""));
                lokace = inzerat.select(".inzeratylok").text();
                datumVlozeni = inzerat.select(".velikost10").text();
                Matcher matcherDate = patternDate.matcher(datumVlozeni);
                matcherDate.find();
                date =   LocalDate.parse(matcherDate.group(1), formatter);
                img = inzerat.select(".obrazek").attr("src");
                url = inzerat.select("a").attr("href");
                model = auto;
                bazosInzerat = new Inzerat(nadpis, popis, cena, lokace, date, img, url, model);
                inzeratyList.add(bazosInzerat);
            }

            for (int i = 1; i<= pocetStranek; i++){
                scrapeUrl = "https://www.bazos.cz/search.php?hledat=" + searchTerm + "&hlokalita=&humkreis=25&cenaod="+cenaOd +"&cenado="+cenaDo+"&order=&crz=" + i*20;

                html = Jsoup.connect(scrapeUrl).get();
                inzeraty = html.select(".inzeraty");
                for(Element inzerat: inzeraty){
                    nadpis = inzerat.select(".nadpis").text();
                    popis = inzerat.select(".popis").text();
                    cena = parseInt(inzerat.select(".inzeratycena").text().replaceAll(" Kč","").replaceAll(" ",""));
                    lokace = inzerat.select(".inzeratylok").text();
                    datumVlozeni = inzerat.select(".velikost10").text();
                    Matcher matcherDate = patternDate.matcher(datumVlozeni);
                    matcherDate.find();
                    date =   LocalDate.parse(matcherDate.group(1), formatter);
                    img = inzerat.select(".obrazek").attr("src");
                    url = inzerat.select("a").attr("href");
                    model = auto;
                    bazosInzerat = new Inzerat(nadpis, popis, cena, lokace, date, img, url, model);
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
