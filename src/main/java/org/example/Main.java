package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.valueOf;


public class Main {
    public static void main(String[] args) {
        DiscordBot.sendImage("test");
        /*try {
            URL url = Main.class.getResource("config.txt");
            File config = new File(url.getPath());
            Scanner myReader = new Scanner(config);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                String[] rozdelenyData = data.split(";");
                List<Inzerat> inzeraty = Scraper.scrapeBazos(rozdelenyData[0], Integer.parseInt(rozdelenyData[1]), Integer.parseInt(rozdelenyData[2]));
                for (Inzerat inzerat : inzeraty) {

                }
                System.out.println(rozdelenyData[0] + ": " + inzeraty.size());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Nebyl nalezen soubour config.txt");
            e.printStackTrace();
        }*/

    }
}