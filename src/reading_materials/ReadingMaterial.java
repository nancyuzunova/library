package reading_materials;

import exceptions.InvalidRentException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeMap;

public abstract class ReadingMaterial {

    private class Revizoro extends Thread{

        public static final int TAX_PERCENTAGE = 1;
        private int duration;

        Revizoro(int duration){
            this.duration = duration;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(duration*1000);
                if(isTaken()){
                    while(true){
                        if(isInterrupted()){
                            System.out.println("Stop accumulating tax!");
                            return;
                        }
                        Thread.sleep(1000);
                        tax += tax*TAX_PERCENTAGE / 100;
                        System.out.println("Revizoro accumulates "+ TAX_PERCENTAGE +"% tax, now is  " + tax);
                    }
                }

            } catch (InterruptedException e) {
                System.out.println("Revizoro finished tax accumulating");
            }
        }
    }

    protected String name;
    protected LocalDate issuedOn;
    private String publisher;
    protected double tax = 0;
    protected double initialTax = 0;
    private Revizoro revizoro;
    private TreeMap<LocalDateTime, LocalDateTime> rentHistory;

    public ReadingMaterial(String name, LocalDate issuedOn, String publisher) {
        this.name = name;
        this.issuedOn = issuedOn;
        this.publisher = publisher;
        this.rentHistory = new TreeMap<>();
    }


    public double getInitialTax() {
        return initialTax;
    }

    public String getName() {
        return name;
    }

    public abstract ReadingMaterialType getType();

    public void rentStarted(int duration) {
        this.rentHistory.put(LocalDateTime.now(), null);
        revizoro = new Revizoro(duration);
        revizoro.start();
    }

    public void rentFinished(){
        this.rentHistory.put(this.rentHistory.lastEntry().getKey(), LocalDateTime.now());
        revizoro.interrupt();
    }

    public boolean isTaken() {
        if(rentHistory.isEmpty()){
            return false;
        }
        return this.rentHistory.lastEntry().getValue() == null;
    }

    public double getTax(){
        return tax;
    }

    public abstract ISortCriteria getCriteria();

    public abstract int getMaxDuration() throws InvalidRentException;
}
