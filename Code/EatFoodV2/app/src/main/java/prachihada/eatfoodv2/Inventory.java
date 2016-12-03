package prachihada.eatfoodv2;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
                    int quantity = item.getQuantity();
                    dateBaseHelper.updateData(id, quantity);
                    position++;
                    System.out.println(id + "id is updated with quantity" +quantity);
                }
            }
        });
        return mview;
    }

    public void displayList(){
        ListView listView = (ListView) mview.findViewById(R.id.InvList);
        itemsList = new HashMap<Integer,Item>();
        dateBaseHelper = new DBHandler(getContext());
        //sqLiteDatabase = dateBaseHelper.getReadableDatabase();
        Cursor cursor = dateBaseHelper.getAllData();
        int position = 0;
        if (cursor.moveToFirst()){
            do{
                int itemId;
                String item_name;
                String expDate;
                int quantity;
                String weight;
                itemId = cursor.getInt(0);
                item_name = cursor.getString(1);
                quantity = cursor.getInt(3);
                weight = cursor.getString(4);
                expDate = dateBaseHelper.getExpDay();
                Item dataProvider = new Item(itemId, item_name, expDate, quantity ,weight);
                itemsList.put(position,dataProvider);
                printItemDetails(dataProvider,position);
                position++;
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
