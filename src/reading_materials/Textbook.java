package reading_materials;

import java.time.LocalDate;

public class Textbook extends ReadingMaterial implements Comparable<Textbook> {

    public enum Subject implements ISortCriteria{
        MATHS, HISTORY, PROGRAMMING, BIOLOGY
    }

    private String author;
    private Subject subject;

    public Textbook(String name, LocalDate issuedOn, String publisher, String author, Subject subject) {
        super(name, issuedOn, publisher);
        this.author = author;
        this.subject = subject;
        this.tax = 3;
        this.initialTax = 3;
    }

    @Override
    public ReadingMaterialType getType() {
        return ReadingMaterialType.TEXTBOOK;
    }

    @Override
    public ISortCriteria getCriteria() {
        return subject;
    }

    @Override
    public int getMaxDuration() {
        return 150;
    }

    @Override
    public int compareTo(Textbook o) {
        if(this.subject.compareTo(o.subject) == 0){
            return this.name.compareTo(o.name);
        }
        return this.subject.compareTo(o.subject);
    }
}
