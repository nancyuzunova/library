import reading_materials.ReadingMaterial;

public class Client extends Thread{

    public static Library library;
    private ReadingMaterial toRead;

    public Client(String name, ReadingMaterial toRead){
        super(name);
        this.toRead = toRead;
    }


    @Override
    public void run() {
        try {
            library.rent(toRead, 10);
            Thread.sleep(30000);
            library.getBack(toRead);
        } catch (InterruptedException e) {
            System.out.println("client interrupted");
        }
    }
}
