package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class Warehouse {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }
    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        Product p = new Product(id, name, stock, day, demand);
        int sunoo = id%10;
        sectors[sunoo].add(p);
    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        int heap = id%10; 
        Sector s = sectors[heap];
        int sunoo = s.getSize();
        s.swim(sunoo); 
    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
       int heap = id%10; 
       Sector s = sectors[heap]; 
       int jake = 1;
       
       if (s.getSize() < 5) 
       return;
       
       for (int i = 1; i < s.getSize(); i++) {
           if (s.get(jake).getPopularity() > s.get(i+1).getPopularity()) {
               jake = i+1;
           }
       }
       s.swap(jake, s.getSize());
       s.deleteLast();
       s.sink(jake);
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        int heap = id%10; 
        Sector s = sectors[heap];
        
        for (int i = 1; i < s.getSize()+1; i++) {
            if (s.get(i).getId() == id) {
                s.get(i).updateStock(amount); 
            }
        }
    }

    private int findId(int id, Sector s) {
        for (int i = 1; i < s.getSize()+1; i++) {
            if (s.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        int heap = id%10; 
        Sector s = sectors[heap];
        int deleteid = findId(id, s);

        if (deleteid < 0) 
        return;

        s.swap(deleteid, s.getSize());
        s.deleteLast();
        s.sink(deleteid);
    }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        int heap = id%10; 
        Sector s = sectors[heap];
        int updateid = findId(id, s);

        if (updateid < 0) 
        return;

        Product p = s.get(updateid);

        if (p.getStock() < amount) 
        return;

        int stock = p.getStock() - amount;
        p.setStock(stock);
        p.setLastPurchaseDay(day);
        int demand = p.getDemand() + amount;
        p.setDemand(demand);
        deleteProduct(p.getId()22222222222222222211);
        addProduct(p.getId(), p.getName(), p.getStock(), p.getLastPurchaseDay(), p.getDemand());
    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        int heap = id%10; 
        Sector s = sectors[heap];

        Product p = new Product(id, name, stock, day, demand);
        
        if (s.getSize() < 5) {
            addProduct(id, name, stock, day, demand);
            return;
        }

        for (int i = 1; i < 10; i++) {
            int index = (heap + i)%10;
            Sector sunoo = sectors [index];
            if (sunoo.getSize() < 5) {
                sunoo.add(p);
                sunoo.swim(sunoo.getSize());
                return;
            }
        }

        evictIfNeeded(id);
        addProduct(id, name, stock, day, demand);
    }

    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
