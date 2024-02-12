package org.example;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.valueOf;
import static java.lang.System.exit;


public class Main {
    public static void main(String[] args) {
        int pocetProdeju = 0;
        int timeOut = 300000;
        try {
            URL configURL = Main.class.getResource("config.txt");
            File config = new File(configURL.getPath());
            Scanner myReader = new Scanner(config);
            try {
                timeOut = Integer.parseInt(myReader.nextLine());
            }catch (Exception e){
                System.out.println("Zadejte do prvního řádku configu timeOut programu v milisekundách");
                e.printStackTrace();
            }
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);

                String[] rozdelenyData = data.split(";");
                List<Inzerat> stareInzeraty = FileManager.loadScrape(rozdelenyData[0], Integer.parseInt(rozdelenyData[1]), Integer.parseInt(rozdelenyData[2]));
                List<Inzerat> noveInzeraty = Scraper.scrapeBazos(rozdelenyData[0], Integer.parseInt(rozdelenyData[1]), Integer.parseInt(rozdelenyData[2]));
                if (stareInzeraty == null) {
                    for (Inzerat inzerat : noveInzeraty) {
                        DiscordBot.storeImage(inzerat);
                    }
                }
                else {
                    for (Inzerat inzerat : noveInzeraty) {
                        if (!stareInzeraty.contains(inzerat)) {
                            DiscordBot.storeImage(inzerat);
                        }
                    }
                }

                List<Inzerat> prodaneInzeraty = new ArrayList<>();

                if (stareInzeraty != null) {
                    for (Inzerat inzerat : stareInzeraty) {
                        if (!noveInzeraty.contains(inzerat)) {
                            pocetProdeju++;
                            prodaneInzeraty.add(inzerat);
                        }
                    }
                }
                FileManager.saveScrape(noveInzeraty, rozdelenyData[0], Integer.parseInt(rozdelenyData[1]), Integer.parseInt(rozdelenyData[2]));
                FileManager.saveSells(prodaneInzeraty, rozdelenyData[0] , Integer.parseInt(rozdelenyData[1]), Integer.parseInt(rozdelenyData[2]));
                System.out.println(rozdelenyData[0] + ": " + noveInzeraty.size());

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Nebyl nalezen soubour config.txt");
            e.printStackTrace();
        }

        System.out.println("Pocet prodeju: " + pocetProdeju);
        System.out.println("Aplikaci nezavírejte. Počkejte až Discord bot přestane odesílat obrázky.");
        try {
            Thread.sleep(timeOut);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exit(0);
    }
}