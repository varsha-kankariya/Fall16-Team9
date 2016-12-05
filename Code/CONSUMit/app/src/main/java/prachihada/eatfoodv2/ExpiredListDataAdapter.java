package prachihada.eatfoodv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul on 12/4/2016.
 */

public class ExpiredListDataAdapter extends ArrayAdapter {
    DBHandler db;

    public HashMap<Integer, Item> getMlist() {
        return mlist;
    }

    public void setMlist(HashMap<Integer, Item> mlist) {
        this.mlist = mlist;
    }

    private HashMap<Integer, Item> mlist;

    public ExpiredListDataAdapter(Context context, int resource, HashMap<Integer, Item> list) {
        super(context, resource);
        mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }


    public void removeElementFromList(int position) {
        System.out.println("positon : " + position);
        mlist.remove(position);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ExpiredListDataAdapter adapter = new ExpiredListDataAdapter(getContext(), R.layout.inventory_list, mlist);
        db = new DBHandler(getContext());
        View mview = convertView;
        //final LayoutHandler layoutHandler;

        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mview = layoutInflater.inflate(R.layout.expiredinventory_list, parent, false);
        //layoutHandler = new LayoutHandler();
        final TextView itemId = (TextView) mview.findViewById(R.id.InvId);
        TextView name = (TextView) mview.findViewById(R.id.InvName);
        final TextView expDate = (TextView) mview.findViewById(R.id.InvExp);
        final TextView quantity = (TextView) mview.findViewById(R.id.InvQuant);
        TextView weight = (TextView) mview.findViewById(R.id.Invweight);
        final Item dataProvider = (Item) this.getItem(position);
        itemId.setText(Integer.toString(dataProvider.getId()));
        name.setText(dataProvider.getName());
        expDate.setText(dataProvider.getExpDate());
        quantity.setText(Integer.toString(dataProvider.getQuantity()));
        weight.setText(dataProvider.getWeight_type());
        printItemDetails(dataProvider);

        quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                String itemQuant = quantity.getText().toString();
                if (!hasFocus && itemQuant!=null && !itemQuant.equals("")) {
                    mlist.get(position).setQuantity(Integer.parseInt(itemQuant));
                }
            }
        });

        return mview;
    }


    public void updateListForRemovedElement() {

        HashMap<Integer, Item> tempList = new HashMap<Integer, Item>();

        int position = 0;

        for (Map.Entry<Integer, Item> entry : mlist.entrySet()) {
            Item item = entry.getValue();
            tempList.put(position, item);
            position++;
        }

        mlist = tempList;

    }

    public void changeColor(){
    }

    public void printItemDetails(Item item) {

        System.out.println("Item Id : " + item.getId() + "Name : " + item.getName() + "Quantity : " + item.getQuantity());
    }
}
