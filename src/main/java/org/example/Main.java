package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.valueOf;
import static java.lang.System.exit;


public class Main {
    public static void main(String[] args) {
        AtomicInteger completedCount = new AtomicInteger();

        int pocetProdeju = 0;
        int timeOut =300000;
        if (args.length == 1) {
            try {
                timeOut = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println("Neplatny vstup. Vstup musí být číslo. (TimeOut ukončneí programu v ms). Bude použit defaultní TimeOut 300 000ms.");
            }
        }
        if (args.length != 1)
        {
            System.out.println("Bude použit defaultní TimeOut 300 000ms pro ukončení programu. TimeOut lze nastavit pomocí prvního argumentu.");
        }
        try {

            File config = new File("config.txt");
            Scanner myReader = new Scanner(config);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);

                String[] rozdelenyData = data.split(";");
                List<Inzerat> stareInzeraty = FileManager.loadScrape(rozdelenyData[0], Integer.parseInt(rozdelenyData[1]), Integer.parseInt(rozdelenyData[2]));
                List<Inzerat> noveInzeraty = Scraper.scrapeBazos(rozdelenyData[0], Integer.parseInt(rozdelenyData[1]), Integer.parseInt(rozdelenyData[2]), rozdelenyData[3]);
               /*
                if (stareInzeraty == null) {
                    for (Inzerat inzerat : noveInzeraty) {
                        DiscordBot.storeImage(inzerat, () -> {
                            // Increment the completed count when a storeImage call completes
                            completedCount.getAndIncrement();

                            // Check if all storeImage calls have completed
                            if (completedCount.get() == noveInzeraty.size()) {
                                // Call FileManager.saveScrape when all storeImage calls are completed
                                FileManager.saveScrape(noveInzeraty, rozdelenyData[0],
                                        Integer.parseInt(rozdelenyData[1]),
                                        Integer.parseInt(rozdelenyData[2]));
                    }
                });}
                }

                else {
                    List<Inzerat> noveStareInzeraty = new ArrayList<>();
                    for (Inzerat inzerat : noveInzeraty) {
                        if (!stareInzeraty.contains(inzerat)) {
                            noveStareInzeraty.add(inzerat);
                        }
                    }
                    for (Inzerat inzerat: noveStareInzeraty){
                        DiscordBot.storeImage(inzerat, () -> {
                            completedCount.getAndIncrement();
                            if (completedCount.get() == noveStareInzeraty.size()) {
                                FileManager.saveScrape(noveStareInzeraty, rozdelenyData[0], Integer.parseInt(rozdelenyData[1]), Integer.parseInt(rozdelenyData[2]));
                            }
                        });
                    }
                }
*/
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
        System.exit(0);
    }
}