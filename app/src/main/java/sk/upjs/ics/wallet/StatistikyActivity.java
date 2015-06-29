package sk.upjs.ics.wallet;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.wallet.provider.WalletContentProvider;

import static sk.upjs.ics.wallet.GrafActivity.UZIVATEL_ID_EXT;

public class StatistikyActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView lblPocetVyb,lblPocVklad,lblVybery,lblVklady,lblPocetTran;
    public static final String UZIVATEL_ID_EX = "vyberId";

    private List<Udaj> udaje;
    private Long userId;

    public class Udaj{
        String suma;
        String typ;
        String id;

        @Override
        public String toString() {
            return suma+" "+typ+" "+id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0,Bundle.EMPTY,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiky);
        userId = (Long) getIntent().getSerializableExtra(UZIVATEL_ID_EX);
        lblPocetTran =(TextView) findViewById(R.id.lblPocetTran);
        lblVklady =(TextView) findViewById(R.id.lblVklady);
        lblVybery =(TextView) findViewById(R.id.lblVybery);
        lblPocVklad=(TextView) findViewById(R.id.lblPocVklady);
        lblPocetVyb=(TextView) findViewById(R.id.lblPocVybery);


    }

    public void zobrazGraf(View view){
        Intent intent = new Intent(this, GrafActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistiky, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.action_Graf) {
            Intent intent = new Intent(this, GrafActivity.class);
            intent.putExtra(UZIVATEL_ID_EXT,userId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(Uri.parse(WalletContentProvider.CONTENT_URI3.toString() + "/stats"));
        Log.w("URI:",WalletContentProvider.CONTENT_URI3.toString() + "/stats");

        return loader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext()){
            udaje = new ArrayList<>();
            while(cursor.moveToNext()){
                Udaj udaj = new Udaj();
                udaj.id=cursor.getString(0);
                udaj.typ=cursor.getString(1);
                udaj.suma=(cursor.getString(2));
                udaje.add(udaj);
                System.out.println("Udaj "+udaj.toString());
            }
            int pocetTran=0;
            int pocetVklad=0;
            int pocetVyb=0;
            double vklady=0.0;
            double vybery=0.0;
            for(int i=0;i<udaje.size();i++){
                if(udaje.get(i).id.equals(userId+"")){
                        pocetTran++;
                    if(udaje.get(i).typ.equals("0")){
                        double sum=Double.parseDouble(udaje.get(i).suma);
                        vybery=vybery+sum;
                        pocetVyb++;
                    }
                    if(udaje.get(i).typ.equals("1")){
                        double sum=Double.parseDouble(udaje.get(i).suma);
                        vklady=vklady+sum;
                        pocetVklad++;
                    }
                }
            }
            lblPocetVyb.setText(pocetVyb+"");
            lblPocVklad.setText(pocetVklad+"");
            lblPocetTran.setText(pocetTran+"");
            lblVybery.setText(new DecimalFormat("#.##").format(vybery)+"");
            lblVklady.setText(new DecimalFormat("#.##").format(vklady)+"");
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
/*
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(ZapasContentProvider.CONTENT_URI);
        return loader;
    }*/

}
