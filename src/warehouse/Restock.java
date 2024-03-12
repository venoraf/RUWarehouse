package warehouse;


public class Restock {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        int count = StdIn.readInt(); 
        StdIn.readLine(); 
        Warehouse w = new Warehouse();

        for (int i = 0; i < count; i++) {
            String heeseung = StdIn.readString();
            if (heeseung.equals("add")) {
            int day = StdIn.readInt();
            int id = StdIn.readInt(); 
            String name = StdIn.readString(); 
            int stock = StdIn.readInt();
            int demand = StdIn.readInt();

            w.addProduct(id, name, stock, day, demand);
            }
            
            if (heeseung.equals("restock")) {
                int id = StdIn.readInt();
                int amount = StdIn.readInt();

                w.restockProduct(id, amount);
            }
        }

        StdOut.setFile(args[1]);
        StdOut.println(w);
    }
}
