package org.test;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainClass {

    public static void main(String[] args) throws IOException {
        List<Map<String, Object>> trucks = readFile();

        System.out.println("__________________");
        String name;
        double probeg;
        double onr;
        double procent;
        if (args.length == 3) {

            for (Map<String, Object> truck : trucks) {
                probeg = (double) truck.get("probeg");
                onr = (double) truck.get("onr");
                procent = (double) truck.get("percent");
                name = (String) truck.get("name");
                System.out.println("Расчет для: " + name);
                double result = Math.ceil(probeg * ((onr + onr * (procent / 100)) / 100));
                System.out.println("Расход топлива: " + (result) + " л.");
                double rashod_masla = Math.ceil(result * 0.029);

                if (rashod_masla >= 2) {
                    rashod_masla -= Math.ceil(Math.random() * 10) / 10;
                }
                System.out.println("Расход масла: " + (rashod_masla) + " л.");
                System.out.println("__________________");
            }
        } else {
            System.out.println("Не хватает параметров");
        }
    }

    private static List<Map<String, Object>> readFile() throws IOException {
        ICsvMapReader mapReader = null;
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> trucksMap;

        try {
            mapReader = new CsvMapReader(new FileReader("../resources/main/trucks.csv"), CsvPreference.STANDARD_PREFERENCE);

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();

            while ((trucksMap = mapReader.read(header, processors)) != null) {
                list.add(trucksMap);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            if (mapReader != null) {

                mapReader.close();

            }
        }
        return list;
    }

    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(), // customerNo (must be unique)
                new Optional(new ParseDouble()), // firstName
                new Optional(new ParseDouble()),
                new Optional(new ParseDouble())
        };
        return processors;
    }
}
