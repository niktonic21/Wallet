package sk.upjs.ics.wallet;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.wallet.provider.Provider;
import sk.upjs.ics.wallet.provider.WalletContentProvider;

public class BankomatyActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    GoogleMap mapa;
    private List<Bank> bankomaty;
    Marker mMarker;
    Button bBanky;
    TextView tVzdialenost;
    private LatLng startP= new LatLng(1.0, 1.0);
    private LatLng startPe= new LatLng(1.0, 1.0);
    public class Bank{
        double lati;
        double lngi;
        String titlei;

        @Override
        public String toString() {
            return "Lat: "+lati+" Lng: "+lngi+" Title: "+titlei;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, Bundle.EMPTY, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankomaty);
        SupportMapFragment smap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapa = smap.getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.setMyLocationEnabled(true);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.73,19.85),6.0f));
        bBanky=(Button) findViewById(R.id.btnBankomaty);
        tVzdialenost=(TextView) findViewById(R.id.textVzdialenost);
    }
    private int pocitadlo=0;
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            startP = new LatLng(location.getLatitude(), location.getLongitude());
            System.out.println("Klikol som na najdi bankomat mam poziciu" + startP.toString());
            if(pocitadlo==0){
                mMarker = mapa.addMarker(new MarkerOptions().position(startP).title("Vaša poloha"));
                if(mapa != null){
                   // mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(startP, 14.0f));
                }
                pocitadlo++;
            }
        }
    };

    public void najdiBankomat(View view){
        mapa.setOnMyLocationChangeListener(myLocationChangeListener);
        double minVzdialenost=Double.MAX_VALUE;
        int id=0;
        double cislo;
        LatLng endPos;
        System.out.println("Klikol som na najdi bankomat");
       while(!startP.equals(startPe)){
            if(startP.equals(startPe))
            System.out.println("rovnaju sa");
        }

        for(int i=0;i<bankomaty.size();i++){
            endPos= new LatLng(bankomaty.get(i).lati,bankomaty.get(i).lngi);
            cislo=CalculationByDistance(endPos);
            System.out.println("Vzdialenost" + cislo);
            if(minVzdialenost>cislo){
                minVzdialenost=cislo;
                id=i;
                System.out.println(" Min Vzdialenost" + minVzdialenost);
            }
        }
        tVzdialenost.setText((int)minVzdialenost+"");
        mapa.addMarker(new MarkerOptions().position(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi))
                .title(bankomaty.get(id).titlei));
        if(mapa != null && minVzdialenost<100){
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi), 14.0f));
        }else
        if(mapa != null && minVzdialenost<300){
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi), 12.0f));
        }else
        if(mapa != null && minVzdialenost<450){
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi), 11.0f));
        }else
        if(mapa != null && minVzdialenost>600){
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi), 10.0f));
        }

    }

    //http://stackoverflow.com/questions/14394366/find-distance-between-two-points-on-map-using-google-map-api-v2
    public double CalculationByDistance(LatLng endP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = startP.latitude;
        double lat2 = endP.latitude;
        double lon1 = startP.longitude;
        double lon2 = endP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        System.out.println("Radius Value" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c %1000;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bankomaty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(WalletContentProvider.CONTENT_URI2);
        System.out.println("URI: "+WalletContentProvider.CONTENT_URI2);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        bankomaty = new ArrayList<>();
        while(cursor.moveToNext()){
            Bank b = new Bank();
            b.lati=cursor.getDouble(cursor.getColumnIndex(Provider.Bankomat.LAT));
            b.lngi=cursor.getDouble(cursor.getColumnIndex(Provider.Bankomat.LNG));
            b.titlei=cursor.getString(cursor.getColumnIndex(Provider.Bankomat.TITLE));
            bankomaty.add(b);
            System.out.println("Bank "+b.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
