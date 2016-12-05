package prachihada.eatfoodv2;

/**
 * Created by laddu on 11/30/16.
 */

public class Item {

    private int id;
    private String name;
    private int quantity;
    private int originalquantity;
    private String weight_type;
    private String expDate;

    public Item(String name, int quantity, String weight_type){
        this.name = name;
        this.quantity = quantity;
        this.weight_type = weight_type;
    }

    public Item(int itemId, String name, String expDate, int quantity, String weight_type, int originalquantity){
        this.id = itemId;
        this.name = name;
        this.expDate = expDate;
        this.quantity = quantity;
        this.weight_type = weight_type;
        this.originalquantity = originalquantity;
    }

    public Item(int itemId, String name, String expDate, int quantity, String weight_type){
        this.id = itemId;
        this.name = name;
        this.expDate = expDate;
        this.quantity = quantity;
        this.weight_type = weight_type;
    }

    public Item(int id){
        this.id = id;
    }

    public int getOriginalquantity() {
        return originalquantity;
    }

    public void setOriginalquantity(int originalquantity) {
        this.originalquantity = originalquantity;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getWeight_type() {
        return weight_type;
    }

    public void setWeight_type(String weight_type) {
        this.weight_type = weight_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
