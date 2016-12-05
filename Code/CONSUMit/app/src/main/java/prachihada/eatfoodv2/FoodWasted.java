package prachihada.eatfoodv2;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class    FoodWasted extends Fragment {

    private DBHandler myDb;
    private SQLiteDatabase sqLiteDatabase;
    private static String TAG = "FoodWasted";
    View mview;
    PieChart pieChart;
    //public float[] yData;
    //public String[] xData;



    public FoodWasted() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_food_wasted, container, false);

        mview = inflater.inflate(R.layout.fragment_food_wasted, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        myDb = new DBHandler(getContext());
        Float foodWasted = myDb.getFoodWasted();
        //Integer foodCount = myDb.getNextTwoDayNotificationCount();
        //Integer foodCount = myDb.getSameDayNotificationCount();
        Float foodEaten = 100-foodWasted;
        Log.d(TAG, "onCreateView: foodWasted: "+ foodWasted.toString());
        Log.d(TAG, "onCreateView: foodEaten: "+ foodEaten.toString());
        //Log.d(TAG, "onCreateView: Count: "+ foodCount.toString());
        //Toast.makeText(getContext(), foodCount.toString(),Toast.LENGTH_LONG).show();
        final float[] yData = {foodEaten,foodWasted};
        final String[] xData = {"Food Consumed","Food Wasted"};
        pieChart = (PieChart) mview.findViewById(R.id.idPieChart);

        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        //pieChart.setCenterText("How much you ate");
        pieChart.setCenterTextSize(16);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.BLACK);

        addDataSet(pieChart, yData, xData);

        //to highlight
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value selected from chart");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("y: ");
                String total = e.toString().substring(pos1+3);
                Log.d(TAG, "onValueSelected: " + total);
                String consumedType;

                for(int i = 0; i<yData.length; i++){
                    if(yData[i]==Float.parseFloat(total)){
                        consumedType = xData[i];
                        Log.d(TAG, "onValueSelected: " + consumedType);
                        total = total.substring(0, (total.indexOf(".") + 3));
                        Toast.makeText(getContext(), total + " Percent \n" + consumedType, Toast.LENGTH_LONG ).show();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });




        return mview;
    }

    private void addDataSet(PieChart chart,float[] yData,String[] xData){
        Log.d(TAG, "addDataSet: started");
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        for(int i=0; i<(yData.length); i++){
            yEntry.add(new PieEntry(yData[i], xData[i]));
        }

        for(int i=1; i<(xData.length); i++){
            xEntry.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntry, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(18);
        pieDataSet.setSelectionShift(3);

        //Add color to data set
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(32, 188, 32));
        colors.add(Color.rgb(216, 69, 19));

        pieDataSet.setColors(colors);

        Legend l = chart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(Color.BLACK);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

}
