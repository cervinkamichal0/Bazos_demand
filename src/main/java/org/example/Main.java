package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Inzerat> inzeraty = Scraper.scrapeBazos("bmw+e46");
        for (Inzerat inzerat : inzeraty) {
            System.out.println(inzerat.nadpis);
        }

    }
}