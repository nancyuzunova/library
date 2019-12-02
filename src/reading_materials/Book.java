package reading_materials;

import java.time.LocalDate;

public class Book extends ReadingMaterial implements Comparable<Book> {

    public enum Genre implements ISortCriteria{
        HORROR, SCI_FI, THRIlLER, CRIMINAL
    }

    private String author;
    private Genre genre;

    public Book(String name, LocalDate issuedOn, String publisher, String author, Genre genre) {
        super(name, issuedOn, publisher);
        this.author = author;
        this.genre = genre;
        this.tax = 2;
        this.initialTax = 2;
    }

    @Override
    public ReadingMaterialType getType() {
        return ReadingMaterialType.BOOK;
    }

    @Override
    public ISortCriteria getCriteria() {
        return genre;
    }

    @Override
    public int getMaxDuration() {
        return 300;
    }

    @Override
    public int compareTo(Book o) {
        return this.issuedOn.compareTo(o.issuedOn);
    }
}
