package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jxl.write.DateTime;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public static void saveSells(List<Inzerat> inzeraty, String hledanyVyraz, int cenaOd, int cenaDo) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File("prodeje.xlsx"));

        } catch (FileNotFoundException e) {
            File newFile = new File("prodeje.xlsx");
            try {
                newFile.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException("Nepodarilo se vytvorit soubor prodeje.xlsx");
            }
            saveSells(inzeraty,hledanyVyraz,cenaOd,cenaDo);
            return;
        }
        try {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet("prodeje");
            int zacateZapisu = sheet.getLastRowNum()+1;
            Row row = null;
            LocalDateTime dateTimeNow = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM.dd yyyy HH:mm");
            for (Inzerat inzerat: inzeraty) {
                row = sheet.createRow(zacateZapisu);


                row.createCell(0).setCellValue(hledanyVyraz + " - od:" + cenaOd + ", do:" + cenaDo);
                row.createCell(1).setCellValue(inzerat.nadpis);
                row.createCell(2).setCellValue(inzerat.cena);
                row.createCell(3).setCellValue(inzerat.datumVlozeni);
                row.createCell(4).setCellValue(dateTimeNow.format(dateTimeFormatter));
                row.createCell(5).setCellValue(inzerat.lokace);
                row.createCell(6).setCellValue(inzerat.popis);
                row.createCell(7).setCellValue(inzerat.img);
                row.createCell(8).setCellValue(inzerat.url);

                zacateZapisu++;
            }
            try {
                FileOutputStream writeFile = new FileOutputStream("prodeje.xlsx");
                workbook.write(writeFile);
                writeFile.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

}
