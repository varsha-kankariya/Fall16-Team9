package prachihada.eatfoodv2;


import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Inventory extends Fragment {

    private DBHandler dateBaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private HashMap<Integer,Item> itemsList;
    private ListDataAdapter mListDataAdapter;

    View mview;
    public Inventory() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_inventory, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        displayList();
        //mListDataAdapter.notifyDataSetChanged();
        Button button = (Button) mview.findViewById(R.id.updateInvButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Integer, Item> inventoryList =  mListDataAdapter.getMlist();
                int position = 0;

                for (Map.Entry<Integer, Item> entry : inventoryList.entrySet()) {
                    Item item = entry.getValue();
                    int id = item.getId();
                    int oquantity = item.getOriginalquantity();
                    int quantity = item.getQuantity();
                    if(quantity > oquantity) {
                        dateBaseHelper.updateData(id, quantity);
                        System.out.println(id + "id is updated with quantity" + quantity);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Cannot increase the product quantity!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    }
                    position++;
                }
                dateBaseHelper.isConsumed();
                dateBaseHelper.isExpired();
            }
        });
        return mview;
    }

    public void displayList(){
        ListView listView = (ListView) mview.findViewById(R.id.InvList);
        itemsList = new HashMap<Integer,Item>();
        dateBaseHelper = new DBHandler(getContext());
        //sqLiteDatabase = dateBaseHelper.getReadableDatabase();
        Cursor cursor = dateBaseHelper.getNotExpired();
        int position = 0;
        if (cursor.moveToFirst()){
            do{
                int itemId;
                String item_name;
                int expDate;
                String expday;
                int quantity;
                int oquantity;
                String weight;
                itemId = cursor.getInt(0);
                item_name = cursor.getString(1);
                oquantity = cursor.getInt(2);
                quantity = cursor.getInt(3);
                weight = cursor.getString(4);
                expDate = (int) Float.parseFloat(cursor.getString(5));
                if(expDate == 0){
                    expday = "Expires Today";
                }
                else {
                    expday = "Expires in " + String.valueOf(expDate) + " days";
                }
                //if(isConsumed.equals("FALSE") && isExpired.equals("FALSE")) {
                Item dataProvider = new Item(itemId, item_name, expday, quantity, weight, oquantity);
                itemsList.put(position, dataProvider);
                printItemDetails(dataProvider, position);
                position++;
                // }
            }while (cursor.moveToNext());
        }

        mListDataAdapter = new ListDataAdapter(getContext(),R.layout.inventory_list,itemsList);

        listView.setAdapter(mListDataAdapter);
        //  mListDataAdapter.notifyDataSetChanged();
    }


    public void printItemDetails(Item item, int position){

        System.out.println("Postition : " + position  + " Item Id : " + item.getId() + "Name : "+ item.getName() + "Quantity : " + item.getQuantity() );
    }
}
