package sk.upjs.ics.wallet;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;
import sk.upjs.ics.wallet.provider.WalletContentProvider;

import static lecho.lib.hellocharts.util.ChartUtils.COLOR_GREEN;
///https://github.com/lecho/hellocharts-android
public class GrafActivity extends ActionBarActivity{

    public static final String UZIVATEL_ID_EXT = "userId";
    private Long userikId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graf);

        if(savedInstanceState!=null){
            String tag =(String) savedInstanceState.get("tag");
            if(tag!=null){
                userikId = (Long) getIntent().getSerializableExtra(UZIVATEL_ID_EXT);
                Bundle bundle = new Bundle();
                bundle.putLong("idecko", userikId);
                PlaceholderFragment fragInfo = new PlaceholderFragment();
                fragInfo.setArguments(bundle);
                getFragmentManager().beginTransaction().add(R.id.container, fragInfo,"graf").commit();
            }else{
                userikId = (Long) getIntent().getSerializableExtra(UZIVATEL_ID_EXT);
                Bundle bundle = new Bundle();
                bundle.putLong("idecko", userikId);
                PlaceholderFragment fragInfo = new PlaceholderFragment();
                fragInfo.setArguments(bundle);
                getFragmentManager().beginTransaction().add(R.id.container, fragInfo,"graf").commit();
            }
        }else {
            userikId = (Long) getIntent().getSerializableExtra(UZIVATEL_ID_EXT);
            System.out.println("Graf activiti saveInstance je rovne null");
            Bundle bundle = new Bundle();
            bundle.putLong("idecko", userikId);
            PlaceholderFragment fragInfo = new PlaceholderFragment();
            fragInfo.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.container, fragInfo).commit();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            if (isGraf()) {
                outState.putString("tag", "graf");
                return;
            }
    }

    private boolean isGraf() {
        return findViewById(R.id.chart)!=null;
    }

    public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
        private List<SubcolumnValue> values;
        private List<Column> columns ;
        private static final int DEFAULT_DATA = 0;
        private static final int SUBCOLUMNS_DATA = 1;
        private static final int STACKED_DATA = 2;
        private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
        private static final int NEGATIVE_STACKED_DATA = 4;

        private ColumnChartView chart;
        private ColumnChartData data;
        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLabels = false;
        private boolean hasLabelForSelected = false;
        private int dataType = DEFAULT_DATA;
        private long idUzivatel;
        private List<Udaj> udaje;

        public class Udaj{
            String suma;
            String month;
            String typ;
            String id;

            @Override
            public String toString() {
                return suma+" "+typ+" "+id+" "+month;
            }
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_graf, container, false);

            chart = (ColumnChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            generateData();
            getLoaderManager().initLoader(0,Bundle.EMPTY,this);
            return rootView;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            idUzivatel= getArguments().getLong("idecko");
            CursorLoader loader = new CursorLoader(this.getActivity());
            loader.setUri(Uri.parse(WalletContentProvider.CONTENT_URI3.toString() + "/graf"));
            Log.w("URI:", WalletContentProvider.CONTENT_URI3.toString() + "/graf");
            values= new ArrayList<>();
            columns = new ArrayList<>();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            udaje = new ArrayList<>();
            while(cursor.moveToNext()){
                Udaj udaj = new Udaj();
                    udaj.id=cursor.getString(0);
                    udaj.typ=cursor.getString(1);
                    udaj.month=cursor.getString(2);
                    udaj.suma=(cursor.getString(3));
                    udaje.add(udaj);
                System.out.println("Udaj "+udaj.toString());
                }
            double[] pole= new double[11];
            for(int i=0;i<udaje.size();i++){
                if(udaje.get(i).id.equals(idUzivatel+"")){
                    if(udaje.get(i).typ.equals("1")){
                        pole[Integer.parseInt(udaje.get(i).month)-1]=
                                pole[Integer.parseInt(udaje.get(i).month)-1]+Double.parseDouble(udaje.get(i).suma);
                    }
                    if(udaje.get(i).typ.equals("0")){
                        pole[Integer.parseInt(udaje.get(i).month)-1]=
                                pole[Integer.parseInt(udaje.get(i).month)-1]-Double.parseDouble(udaje.get(i).suma);
                    }

                }
            }
            System.out.println("pole graf suma za dany mesiac "+Arrays.toString(pole));

            for(int j=0;j<pole.length;j++){
                values=new ArrayList<>();
                if((int)(pole[j])<0){
                    values.add(new SubcolumnValue((int)(pole[j]),ChartUtils.COLOR_RED));
                }
                if((int)(pole[j])>0){
                    values.add(new SubcolumnValue((int)(pole[j]),ChartUtils.COLOR_GREEN));
                }
                if((int)(pole[j])==0){
                    values.add(new SubcolumnValue((int)(pole[j]),ChartUtils.COLOR_BLUE));
                }

                //System.out.println("cursor mesiac "+cursor.getString(0));
                //System.out.println("cursor data "+cursor.getInt(1));
                //System.out.println("datum "+cursor.getString(2));
                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
                System.out.print("Column ");
            }

            generateDefaultData();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

        // MENU
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_graf, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_reset) {
                reset();
                generateData();
                return true;
            }
            if (id == R.id.action_subcolumns) {
                dataType = SUBCOLUMNS_DATA;
                generateData();
                return true;
            }
            if (id == R.id.action_stacked) {
                dataType = STACKED_DATA;
                generateData();
                return true;
            }
            if (id == R.id.action_negative_subcolumns) {
                dataType = NEGATIVE_SUBCOLUMNS_DATA;
                generateData();
                return true;
            }
            if (id == R.id.action_negative_stacked) {
                dataType = NEGATIVE_STACKED_DATA;
                generateData();
                return true;
            }
            if (id == R.id.action_toggle_labels) {
                toggleLabels();
                return true;
            }
            if (id == R.id.action_toggle_axes) {
                toggleAxes();
                return true;
            }
            if (id == R.id.action_toggle_axes_names) {
                toggleAxesNames();
                return true;
            }
            if (id == R.id.action_animate) {
                prepareDataAnimation();
                chart.startDataAnimation();
                return true;
            }
            if (id == R.id.action_toggle_selection_mode) {
                toggleLabelForSelected();

                Toast.makeText(getActivity(),
                        "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_toggle_touch_zoom) {
                chart.setZoomEnabled(!chart.isZoomEnabled());
                Toast.makeText(getActivity(), "IsZoomEnabled " + chart.isZoomEnabled(), Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_zoom_both) {
                chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
                return true;
            }
            if (id == R.id.action_zoom_horizontal) {
                chart.setZoomType(ZoomType.HORIZONTAL);
                return true;
            }
            if (id == R.id.action_zoom_vertical) {
                chart.setZoomType(ZoomType.VERTICAL);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void reset() {
            hasAxes = true;
            hasAxesNames = true;
            hasLabels = false;
            hasLabelForSelected = false;
            dataType = DEFAULT_DATA;
            chart.setValueSelectionEnabled(hasLabelForSelected);

        }

        private void generateDefaultData() {
            int numSubcolumns = 1;
            int numColumns = 8;
            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            //List<Column> columns = new ArrayList<Column>();
            //List<SubcolumnValue> values;


            data = new ColumnChartData(columns);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Mesiac");
                    axisY.setName("Suma za mesiac");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);

        }



        private int getSign() {
            int[] sign = new int[]{-1, 1};
            return sign[Math.round((float) Math.random())];
        }

        private void generateData() {
            switch (dataType) {
                case DEFAULT_DATA:
                    generateDefaultData();
                    break;
                default:
                    generateDefaultData();
                    break;
            }
        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;
            chart.setValueSelectionEnabled(hasLabelForSelected);
            System.out.print("Som v toogleLabel "+(float) Math.random() * 100);
            if (hasLabelForSelected) {
                hasLabels = false;
            }

            generateData();
        }

        private void toggleAxes() {
            hasAxes = !hasAxes;

            generateData();
        }

        private void toggleAxesNames() {
            hasAxesNames = !hasAxesNames;

            generateData();
        }

        /**
         * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
         * method(don't confuse with View.animate()).
         */
        private void prepareDataAnimation() {
            for (Column column : data.getColumns()) {
                for (SubcolumnValue value : column.getValues()) {
                    value.setTarget((float) Math.random() * 100);
                    System.out.print("Set target prepare data final "+(float) Math.random() * 100);
                }
            }
        }

        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
                Toast.makeText(getActivity(), "Suma v mesiaci: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }

    }
}
