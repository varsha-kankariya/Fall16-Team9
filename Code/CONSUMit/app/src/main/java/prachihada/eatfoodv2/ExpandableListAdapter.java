package prachihada.eatfoodv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    private HashMap<String,Item> finalList ;
    private static final int RC_OCR_CAPTURE = 9003;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, HashMap<String,Item> finalList) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.finalList = finalList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);


          if(groupPosition == 0 || groupPosition == 1) {
       //     if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
         //   }


            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);

            final EditText editText = (EditText) convertView.findViewById(R.id.editText);
            editText.setText("0");
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    String itemQuant = editText.getText().toString();
                    if (!hasFocus && itemQuant!=null && !itemQuant.equals("")) {
                        //editText.getText().toString();
                        //String quantity = editText.getText().toString();
                        // System.out.println("Item : " + childText + "Quantity : " +editText.getText().toString() );
                        finalList.get(childText).setQuantity(Integer.parseInt(editText.getText().toString()));

                    }
                }
            });


            //Retrieve the spinner and set listener and adapter for that
            Spinner spinner = (Spinner) convertView.findViewById(R.id.weight_spinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(_context,
                    R.array.wght_types, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    // System.out.println("Selected weight type for "+childText+" : " + adapterView.getItemAtPosition(pos));
                    ((TextView) view).setTextColor(Color.BLACK);
                    finalList.get(childText).setWeight_type(adapterView.getItemAtPosition(pos).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
//                    System.out.println("Nothing Selected for "+childText+ "!");
                }
            });



        }else if(groupPosition ==2){

            //if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_other_item, null);
            //}
            final View finalConvertedView = convertView;

            addListenerToItemName(convertView);

            addListenerToQuantity(convertView);

            addListenerToDate(convertView);
            
            Button buttonScan = (Button) convertView.findViewById(R.id.scan_button);
            buttonScan.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(_context, OcrCaptureActivity.class);
                    intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                    intent.putExtra(OcrCaptureActivity.UseFlash, false);

                    ((Activity)_context).startActivityForResult(intent, RC_OCR_CAPTURE);
                }
            });

            convertView = finalConvertedView;
        }

        return convertView;

    }

    private void addListenerToDate(View convertView) {

        final EditText date = (EditText) convertView.findViewById(R.id.editText_Date);
        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

//                System.out.println("Other Section : Item expiry date changed!!");
                String expDate = date.getText().toString();
                if(!date.equals("")){
                    finalList.get("Other").setExpDate(expDate);
                }


            }
        });

    }

    private void addListenerToQuantity(View convertView) {
        final EditText quantity = (EditText) convertView.findViewById(R.id.editText2_Qty);
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

//                System.out.println("Other Section : Item quantity changed!!");
                String quantityAsStr = quantity.getText().toString();
                if(!quantity.equals("")){
                    finalList.get("Other").setQuantity(Integer.parseInt(quantityAsStr));
                }


            }
        });
    }

    private void addListenerToItemName(View convertView) {

        final EditText itemName = (EditText) convertView.findViewById(R.id.editText_ItemName);
        itemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                System.out.println("Other Section : Item name changed!!");
                finalList.get("Other").setName(itemName.getText().toString());

            }
        });


    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public HashMap<String, Item> getFinalList() {
        return finalList;
    }

    public void setFinalList(HashMap<String, Item> finalList) {
        this.finalList = finalList;
    }
}