package reading_materials;

import exceptions.InvalidRentException;

import java.time.LocalDate;

public class Magazine extends ReadingMaterial implements Comparable<Magazine> {

    public enum Category implements reading_materials.ISortCriteria {
        SPORT, FASHION, SCI_FI, NATURE
    }

    private int number;
    private Category category;

    public Magazine(String name, LocalDate issuedOn, String publisher, int number, Category category) {
        super(name, issuedOn, publisher);
        this.number = number;
        this.category = category;
    }

    @Override
    public reading_materials.ReadingMaterialType getType() {
        return reading_materials.ReadingMaterialType.MAGAZINE;
    }

    @Override
    public reading_materials.ISortCriteria getCriteria() {
        return category;
    }

    @Override
    public int getMaxDuration() throws InvalidRentException {
        throw new InvalidRentException();
    }

    @Override
    public int compareTo(Magazine o) {
        if(this.name.compareTo(o.name) == 0){
            return this.number - o.number;
        }
        return this.name.compareTo(o.name);
    }
}
