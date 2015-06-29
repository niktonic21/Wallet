package sk.upjs.ics.wallet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import sk.upjs.ics.wallet.provider.Provider;
import sk.upjs.ics.wallet.provider.WalletContentProvider;

public class MapaCesta extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>,LocationListener, GpsStatus.Listener{
    private LocationManager mService;
    private GpsStatus mStatus;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_DISTANCE = 10.0f;

    // Reference to the LocationManager and LocationListener
    private LocationListener mLocationListener;
    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration;
    private List<Bank> bankomaty;
    ArrayList<LatLng> pointList = new ArrayList<LatLng>();
    Marker mMarker;
    Button bBanky;
    private LatLng startP;
    private LatLng startPe= new LatLng(1.0, 1.0);
    private String titleE;

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
        this.setContentView(R.layout.activity_mapa_cesta);
        mService = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mService.addGpsStatusListener(this);
        //this.tvDistanceDuration = (TextView) this.findViewById(R.id.tv_distance_time);
        // Initializing
        this.markerPoints = new ArrayList<LatLng>();
        bBanky=(Button) findViewById(R.id.btnBankomaty);
        tvDistanceDuration=(TextView) findViewById(R.id.tv_distance_time);
        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        this.map = fm.getMap();
        map.setOnMyLocationChangeListener(myLocationChangeListener);
        // Enable MyLocation Button in the Map
        this.map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        if(savedInstanceState!=null){
            savedInstanceState.getDouble("lat");
            savedInstanceState.getDouble("lon");
            savedInstanceState.getFloat("zoom");
            LatLng startPos=new LatLng(48.73, 19.85);
            LatLng endPos=new LatLng(48.73, 19.85);
            if(savedInstanceState.containsKey("points")){
                System.out.println("Som v pointoch");
                pointList = savedInstanceState.getParcelableArrayList("points");
                if(pointList!=null){
                    for(int i=0;i<pointList.size();i++){
                        System.out.println("Body v save instance"+ pointList.get(i).toString());
                        drawMarker(pointList.get(i));
                        if(i==0){
                            endPos=pointList.get(i);
                        }
                        if(i==1){
                            startPos=pointList.get(i);
                        }
                    }
                }
            }

            String url = MapaCesta.this.getDirectionsUrl(startPos, endPos);

            DownloadTask downloadTask = new DownloadTask();
            System.out.println("Posielm url " + url.toString());
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.73, 19.85), 6.0f));
        }
    }
    @Override
    public void onGpsStatusChanged(int event) {
        mStatus = mService.getGpsStatus(mStatus);
        System.out.println("GPS nieco");
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                Toast.makeText(this, "GPS zapnute",
                        Toast.LENGTH_SHORT).show();
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                Toast.makeText(this, "GPS zastavene",
                        Toast.LENGTH_SHORT).show();
                break;

            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Toast.makeText(this, "GPS first status",
                        Toast.LENGTH_SHORT).show();
                break;

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                /*Toast.makeText(this, "Nacitavam vasu polohu",
                        Toast.LENGTH_SHORT).show();*/
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private int pocitadlo=0;
    private double latS;private double lonS;
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            startP = new LatLng(location.getLatitude(), location.getLongitude());
            System.out.println("Klikol som na najdi bankomat mam poziciu" + startP.toString());
            if(pocitadlo==0){
                mMarker = map.addMarker(new MarkerOptions().position(startP).title("Vaša poloha"));
                if(map != null){
                    // mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(startP, 14.0f));
                }
                pocitadlo++;
            }
        }
    };

    public void najdiBankomaty(View view){
        map.clear();
        double minVzdialenost=Double.MAX_VALUE;
        int id=0;
        double cislo;
        //LatLng endPos=new LatLng(bankomaty.get(0).lati,bankomaty.get(0).lngi);
        LatLng endPos;
        System.out.println("Klikol som na najdi bankomat a startP "+startP);
       /* while(startP.equals(startPe)){
            if(startP.equals(startPe))
                System.out.print( startP);
        }*/
        LatLng origin= startP;
        if(startP==null|| startP.equals(startPe)){
            Toast.makeText(this, "Nenasli sme vasu polohu", Toast.LENGTH_SHORT)
                    .show();
            return;
        }else if(!startP.equals(startPe) || startP!=null) {
            mMarker = map.addMarker(new MarkerOptions().position(startP).title("Vaša poloha"));
            for (int i = 0; i < bankomaty.size(); i++) {
                endPos = new LatLng(bankomaty.get(i).lati, bankomaty.get(i).lngi);
                cislo = CalculationByDistance(startP,endPos);
                System.out.println("Vzdialenost " + cislo);
                if (minVzdialenost > cislo) {
                    minVzdialenost = cislo;
                    id = i;
                    System.out.println(" Min Vzdialenost " + minVzdialenost);
                }
            }
            LatLng bankBod =new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi);
            map.addMarker(new MarkerOptions().position(bankBod)
                    .title(bankomaty.get(id).titlei));
            pointList.add(bankBod);
            pointList.add(origin);
            titleE=bankomaty.get(id).titlei;
            if (map != null && minVzdialenost < 1.0) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi), 14.0f));
            } else if (map != null && minVzdialenost < 4.0) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi), 12.0f));
            } else if (map != null && minVzdialenost < 8.0) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi), 11.0f));
            } else if (map != null && minVzdialenost > 10.0) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi), 10.0f));
            }
            // Getting URL to the Google Directions API
            String url = MapaCesta.this.getDirectionsUrl(origin, new LatLng(bankomaty.get(id).lati, bankomaty.get(id).lngi));

            DownloadTask downloadTask = new DownloadTask();
            System.out.println("Posielm url " + url.toString());
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }else {
            Toast.makeText(this, "Nenasli sme vasu polohu", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    //http://stackoverflow.com/questions/14394366/find-distance-between-two-points-on-map-using-google-map-api-v2
    public double CalculationByDistance(LatLng startPos,LatLng endP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = startPos.latitude;
        double lat2 = endP.latitude;
        double lon1 = startPos.longitude;
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
        System.out.println("Radius Value " + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c ;
    }



    private String getDirectionsUrl(LatLng origin, LatLng dest)
    {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try
        {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e)
        {
           // Log.d("Exception while downloading url", e.toString());
        } finally
        {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>
    {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url)
        {

            // For storing data from web service
            String data = "";

            try
            {
                // Fetching the data from web service
                data = MapaCesta.this.downloadUrl(url[0]);
            } catch (Exception e)
            {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
    {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
        {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try
            {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result)
        {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1)
            {
                Toast.makeText(MapaCesta.this.getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++)
            {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++)
                {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0)
                    { // Get distance from the list
                        distance = point.get("distance");
                        System.out.println("Distance"+distance);
                        continue;
                    } else if (j == 1)
                    { // Get duration from the list
                        duration = point.get("duration");
                        System.out.println("Duration"+duration);
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }
            // Drawing polyline in the Google Map for the i-th route
            MapaCesta.this.map.addPolyline(lineOptions);

            MapaCesta.this.tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);


        }
    }
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putDouble("lat", map.getCameraPosition().target.latitude);
        bundle.putDouble("lon", map.getCameraPosition().target.longitude);
        bundle.putFloat("zoom", map.getCameraPosition().zoom);
        bundle.putParcelableArrayList("points", pointList);

        // Saving the bundle
        super.onSaveInstanceState(bundle);
    }
    // Draw a marker at the "point"
    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Setting a title for this marker
        markerOptions.title("Lat:"+point.latitude+","+"Lng:"+point.longitude);

        // Adding marker on the Google Map
        map.addMarker(markerOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.menu_mapa_cesta, menu);
        return true;
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