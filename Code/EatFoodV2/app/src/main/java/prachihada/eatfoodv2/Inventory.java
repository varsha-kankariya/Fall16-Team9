package prachihada.eatfoodv2;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
                quantity = cursor.getInt(2);
                weight = cursor.getString(3);
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
