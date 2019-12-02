import exceptions.InvalidRentException;
import reading_materials.ISortCriteria;
import reading_materials.ReadingMaterial;
import reading_materials.ReadingMaterialType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Library {

    private class RentLog{
        private LocalDateTime start;
        private LocalDateTime end;
        private String client;

        public RentLog(LocalDateTime start, LocalDateTime end, String client) {
            this.start = start;
            this.end = end;
            this.client = client;
        }
    }

    private class Revisor extends Thread{
        @Override
        public void run() {
            try {
                while(true){
                    Thread.sleep(5000); //revision is done in every 5 seconds
                    printAllReadingMaterials();
                    writeTakenOnFile();
                    writeOverdues();
                }
            } catch (InterruptedException e) {
                System.out.println("Revisor interrupted");
            }
        }
    }

    private void writeOverdues() {
        File file = new File("overdue.txt");
        double total = 0;
        try(PrintWriter pr = new PrintWriter(new FileOutputStream(file, true))){
            HashMap<String, Double> toBeWritten = new HashMap<>();
            for(Map.Entry<ReadingMaterial, RentLog> e : rentLog.entrySet()){
                if(e.getValue().end.isBefore(LocalDateTime.now())){
                    double interest = e.getKey().getTax() - e.getKey().getInitialTax();
                    total += interest;
                    toBeWritten.put(e.getValue().client + " - " + e.getKey().getName(), e.getKey().getTax());
                }
            }
            ArrayList<Map.Entry<String, Double>> overdue = new ArrayList<>();
            overdue.addAll(toBeWritten.entrySet());
            overdue.sort(new Comparator<Map.Entry<String, Double>>() {
                @Override
                public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            for(Map.Entry<String, Double> e : overdue){
                pr.println(e.getKey() + " - amount due = " + e.getValue());
            }
            pr.println("Total amount = " + total);
        } catch (IOException e){
            System.out.println("something wrong with overdue");
        }
    }

    private void writeTakenOnFile() {
        File file = new File("all_taken.txt");
        try(PrintWriter pr = new PrintWriter(new FileOutputStream(file, true))){
            ArrayList<Map.Entry<ReadingMaterial, RentLog>> rented = new ArrayList<>();
            rented.addAll(rentLog.entrySet());
            rented.sort(new Comparator<Map.Entry<ReadingMaterial, RentLog>>() {
                @Override
                public int compare(Map.Entry<ReadingMaterial, RentLog> o1, Map.Entry<ReadingMaterial, RentLog> o2) {
                    return o1.getValue().start.compareTo(o2.getValue().start);
                }
            });
            for(Map.Entry<ReadingMaterial, RentLog> e : rented){
                pr.println(e.getKey().getName() + " - taken on " + e.getValue().start + " must be returned on "
                        + e.getValue().end);
            }
            pr.println("Total number of rented = " + rented.size());
        }
        catch (IOException e){
            System.out.println("something wrong with writeTaken");
        }
    }

    private void printAllReadingMaterials() {
        int count = 0;
        for(Map.Entry<ReadingMaterialType, HashMap<ISortCriteria,TreeSet<ReadingMaterial>>> e : catalogue.entrySet()){
            for(TreeSet<ReadingMaterial> tree : e.getValue().values()){
                for(ReadingMaterial r : tree){
                    if(!r.isTaken()){
                        count++;
                    }
                }
            }
        }
        System.out.println("All not taken = " + count);
    }

    private HashMap<ReadingMaterialType, HashMap<ISortCriteria, TreeSet<ReadingMaterial>>> catalogue;
    private HashMap<ReadingMaterial, RentLog> rentLog;
    private Revisor revisor;

    public Library() {
        this.catalogue = new HashMap<>();
        this.catalogue.put(ReadingMaterialType.BOOK, new HashMap<>());
        this.catalogue.put(ReadingMaterialType.TEXTBOOK, new HashMap<>());
        this.catalogue.put(ReadingMaterialType.MAGAZINE, new HashMap<>());
        this.rentLog = new HashMap<>();
        this.revisor = new Revisor();
        revisor.setDaemon(true);
        revisor.start();
    }

    public void addReadingMaterial(ReadingMaterial r){
        if(!this.catalogue.get(r.getType()).containsKey(r.getCriteria())){
            this.catalogue.get(r.getType()).put(r.getCriteria(), new TreeSet<>());
        }
        this.catalogue.get(r.getType()).get(r.getCriteria()).add(r);
    }
    public void rent(ReadingMaterial r, int duration){
        try {
            if(duration > r.getMaxDuration()){
                System.out.println("Sorry, maximum rent duration exceeded");
            }
            else {
                if(r.isTaken()){
                    System.out.println("Sorry! It's taken.");
                }
                else{
                    r.rentStarted(duration);
                    this.rentLog.put(r, new RentLog(LocalDateTime.now(),
                            LocalDateTime.now().plus(Duration.ofSeconds(duration)),
                            Thread.currentThread().getName()));
                }
            }
        } catch (InvalidRentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getBack(ReadingMaterial r){
        if(r.isTaken()){
            System.out.println("You owe us " + r.getTax() + " money.");
            r.rentFinished();
            rentLog.remove(r);
        }
    }
}
