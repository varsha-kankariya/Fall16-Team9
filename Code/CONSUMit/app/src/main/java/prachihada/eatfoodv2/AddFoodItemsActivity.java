package prachihada.eatfoodv2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Referred Videos :
 * For list view : http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 * For action bar :http://stackoverflow.com/questions/34311601/how-to-add-a-bottom-menu-to-android-activity
 *
 *
 *
 * Toast API : for small views containing messages, doesn't receive focus
 */

public class AddFoodItemsActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, Item> finalList;
    Context context;
    DBHandler dataHelper;
    EditText scanDate;
    static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "AddFoodItemsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_items);

        context = this;
        dataHelper = new DBHandler(context);

        finalList = new HashMap<String, Item>();
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();
        prepareFinalListData();

        //Prepare the adapter whose methods will be invoked on any action onto the dropdown lists
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild,finalList);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Toast.makeText(getApplicationContext(),
                        "Group Clicked " + listDataHeader.get(groupPosition),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                System.out.println("Child clicked : " + groupPosition + " " + childPosition );
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
        System.out.println("Back button pressed!!!");
        Intent homeIntent = new Intent(this,MainActivity.class);
        startActivity(homeIntent);
    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Fruits");
        listDataHeader.add("Vegetables");
        listDataHeader.add("Others");

        // Adding child data
        List<String> fruits = new ArrayList<String>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Grapes");
        fruits.add("Cherry");
        fruits.add("Strawberry");
        fruits.add("Peaches");

        List<String> vegetables = new ArrayList<String>();
        vegetables.add("Tomato");
        vegetables.add("Onion");
        vegetables.add("Spinach");
        vegetables.add("Beans");
        vegetables.add("Garlic");
        vegetables.add("Brocolli");

        List<String> other_products = new ArrayList<String>();
        other_products.add("");


        listDataChild.put(listDataHeader.get(0), fruits); // Header, Child data
        listDataChild.put(listDataHeader.get(1), vegetables);
        listDataChild.put(listDataHeader.get(2), other_products);
    }

    //Invoked on clicking fridge button
    //Add data to db
    //Create notifications
    public void processUserData(View view) {

        finalList  = listAdapter.getFinalList();
       /* Set keys = finalData.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            Integer value = (Integer) finalData.get(key);
            System.out.println(key + " = " + value);
        }*/
        displayItemList();
        saveDataToDb();

        //Invoke method to add notifications
        //createNotifications();

        Toast.makeText(getApplicationContext(),"Data Saved", Toast.LENGTH_SHORT).show();

        //Code to reset the data
        /*
        // preparing list data
        prepareListData();
        prepareFinalListData();

        //Prepare the adapter whose methods will be invoked on any action onto the dropdown lists
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild,finalList);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        */

        Intent mainIntent = new Intent(this,MainActivity.class);
        startActivity(mainIntent);

    }


    public void viewData(View view){

        Cursor res = dataHelper.getAllData();
        if (res.getCount() == 0){
            //show message
            showMessage("Error","Nothing Found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append("Id : "+ res.getString(0) +"\n" );
            buffer.append("Item Name : "+ res.getString(1) +"\n" );
            buffer.append("Quantity : "+ res.getString(2) +"\n" );
            buffer.append("Purchase Date: "+ res.getString(5) +"\n" );
            buffer.append("Expiry_Date: "+ res.getString(6) +"\n\n" );
        }

        //Show all data
        showMessage("Food Items", buffer.toString());

    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    //Save data to db
    public void saveDataToDb(){

      for (Map.Entry<String, Item> entry : finalList.entrySet()) {
            Item item = entry.getValue();
//            System.out.println(entry.getKey()+" : "+item.getWeight_type()+ " : "+item.getQuantity());
          if(item.getQuantity()>0 && !item.getName().equals("")) {
              dataHelper.insertData(entry.getKey(),item);
          }
        }

    }


    public void displayItemList(){

        for (Map.Entry<String, Item> entry : finalList.entrySet()) {
            Item item = entry.getValue();
            System.out.println(entry.getKey()+" : "+item.getWeight_type()+ " : "+item.getQuantity());
        }

    }


    //Preparing hash map for storing all the user selected choices
    private void prepareFinalListData() {


        finalList.put("Apple",new Item("Apple",0,"no"));
        finalList.put("Banana",new Item("Banana",0,"no"));
        finalList.put("Grapes",new Item("Grapes",0,"lb"));
        finalList.put("Cherry",new Item("Cherry",0,"lb"));
        finalList.put("Strawberry",new Item("Strawberry",0,"no"));
        finalList.put("Peaches",new Item("Peaches",0,"no"));
        finalList.put("Tomato",new Item("Tomato",0,"no"));
        finalList.put("Onion",new Item("Onion",0,"no"));
        finalList.put("Spinach",new Item("Spinach",0,"no"));
        finalList.put("Beans",new Item("Spinach",0,"lb"));
        finalList.put("Garlic",new Item("Garlic",0,"no"));
        finalList.put("Brocolli",new Item("Brocolli",0,"no"));
        finalList.put("Other" ,new Item("Other",1,"no"));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    scanDate = (EditText) findViewById(R.id.editText_Date);
                    String text = data.getStringExtra(OcrCaptureActivity.ScanDate);
                    scanDate.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            }
        }
    }
}
