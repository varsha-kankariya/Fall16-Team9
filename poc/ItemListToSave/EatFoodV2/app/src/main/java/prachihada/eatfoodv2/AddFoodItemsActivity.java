package prachihada.eatfoodv2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    HashMap<String,Integer> finalList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_items);

        finalList = new HashMap<String,Integer>();
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

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
        finalList.put("Apple",0);
        fruits.add("Banana");
        finalList.put("Banana",0);
        fruits.add("Grapes");
        finalList.put("Grapes",0);
        fruits.add("Cherry");
        finalList.put("Cherry",0);
        fruits.add("Strawberry");
        finalList.put("Strawberry",0);
        fruits.add("Peaches");
        finalList.put("Peaches",0);


        List<String> vegetables = new ArrayList<String>();
        vegetables.add("Tomato");
        finalList.put("Tomato",0);
        vegetables.add("Onion");
        finalList.put("Onion",0);
        vegetables.add("Spinach");
        finalList.put("Spinach",0);
        vegetables.add("Beans");
        finalList.put("Beans",0);
        vegetables.add("Garlic");
        finalList.put("Garlic",0);
        vegetables.add("Brocolli");
        finalList.put("Brocolli",0);

        List<String> other_products = new ArrayList<String>();
        other_products.add("Milk");
        finalList.put("Milk",0);
        other_products.add("Eggs");
        finalList.put("Eggs",0);
        other_products.add("Bread");
        finalList.put("Bread",0);


        listDataChild.put(listDataHeader.get(0), fruits); // Header, Child data
        listDataChild.put(listDataHeader.get(1), vegetables);
        listDataChild.put(listDataHeader.get(2), other_products);
    }


    public void saveData(View view) {

        HashMap<String,Integer> finalData  = listAdapter.getFinalList();
        Set keys = finalData.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            Integer value = (Integer) finalData.get(key);
            System.out.println(key + " = " + value);
        }

        Toast.makeText(getApplicationContext(),"Data Saved", Toast.LENGTH_SHORT).show();


    }
}
