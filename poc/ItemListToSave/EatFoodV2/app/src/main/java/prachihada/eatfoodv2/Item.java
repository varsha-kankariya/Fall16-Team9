package prachihada.eatfoodv2;

/**
 * Created by laddu on 11/29/16.
 */

public class Item {

    public Item(String item_name, String quantity){
        this.item_name = item_name;
        this.quantity = quantity;
    }


    private String item_name;

    private String quantity;


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }




}
