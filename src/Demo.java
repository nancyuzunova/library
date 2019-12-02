import reading_materials.Book;
import reading_materials.Magazine;
import reading_materials.ReadingMaterial;
import reading_materials.Textbook;

import java.time.LocalDate;

public class Demo {

    public static void main(String[] args) {
        Library library = new Library();
        Client.library = library;
        ReadingMaterial r = new Book("Fire and Blood", LocalDate.now(), "Ciela", "George Martin", Book.Genre.SCI_FI);
        library.addReadingMaterial(r);
        r = new Magazine("Vogue", LocalDate.now(), "Vogue", 5, Magazine.Category.FASHION);
        library.addReadingMaterial(r);
        r = new Textbook("Java for everyone", LocalDate.now(), "Ciela", "Krasimir Stoev", Textbook.Subject.PROGRAMMING);
        library.addReadingMaterial(r);
        r = new Book("Jane Eyre", LocalDate.now(), "Abagar", "Charlotte Bronte", Book.Genre.THRIlLER);
        library.addReadingMaterial(r);
        ReadingMaterial rm = new Textbook("English history", LocalDate.now(), "Ciela", "Radostina Mihaleva", Textbook.Subject.HISTORY);
        library.addReadingMaterial(rm);
        ReadingMaterial rm1 = new Magazine("Elle", LocalDate.now(), "Vogue", 7, Magazine.Category.FASHION);
        library.addReadingMaterial(rm1);

        new Client("Nancy", r).start();
        new Client("Lora", rm).start();
        new Client("Gosho", rm1).start();
    }
}
