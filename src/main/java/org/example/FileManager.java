package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    public static List<Inzerat> loadScrape(String hledanyVyraz, int cenaOd, int cenaDo) {
        Gson gson = new Gson();
        String filePath = hledanyVyraz + " - od" + cenaOd + ", do" + cenaDo + ".json";
        try {
            File file = new File(filePath);
            String json = new Scanner(file).useDelimiter("\\A").next();
            Type listType = TypeToken.getParameterized(List.class, Inzerat.class).getType();
            List<Inzerat> inzeraty = gson.fromJson(json, listType);
            for (Inzerat inzerat : inzeraty) {
                System.out.println(inzerat.nadpis);
            }
            return inzeraty;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
    public static void saveScrape(List<Inzerat> inzeraty, String hledanyVyraz, int cenaOd, int cenaDo) {
        Gson gson = new Gson();
        String filePath = hledanyVyraz + " - od" + cenaOd + ", do" + cenaDo + ".json";
        try {
            File file = new File(filePath);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(filePath);
            gson.toJson(inzeraty, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
