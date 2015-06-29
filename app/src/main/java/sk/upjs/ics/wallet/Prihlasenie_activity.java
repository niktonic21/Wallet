package sk.upjs.ics.wallet;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import static sk.upjs.ics.wallet.VyberActivity.UZIVATEL_ID_EXTRA;
import sk.upjs.ics.wallet.provider.WalletContentProvider;

public class Prihlasenie_activity extends ActionBarActivity implements  LoaderManager.LoaderCallbacks<Cursor> {

    EditText eMeno, eHeslo;
    Button bPrihlasit;
    TextView tRegistracia;
    private List<Udaj> udaje;
    private Long vyberId;

    public class Udaj{
        String meno;
        String heslo;
        Long id;

        @Override
        public String toString() {
            return meno+" "+heslo+" "+id;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0,Bundle.EMPTY,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prihlasenie_activity);

        eMeno =(EditText) findViewById(R.id.editMeno);
        eHeslo =(EditText) findViewById(R.id.editHeslo);

        bPrihlasit=(Button) findViewById(R.id.btnPrihlasenie);
        tRegistracia=(TextView) findViewById(R.id.textRegistracia);
        tRegistracia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Prihlasenie_activity.this, RegistraciaActivity.class);
                startActivity(intent);
            }
            });

    }

    public void Prihlasenie(View view){
        boolean ok=false;
        System.out.println("meno1: "+eMeno.getText().toString()+" heslo1: "+eHeslo.getText().toString()+" Udaje: "+udaje.size());
        for(int i=0;i<udaje.size();i++){
            System.out.println("meno: "+udaje.get(i).meno+" heslo: "+udaje.get(i).heslo);
            if(udaje.get(i).meno.equals(eMeno.getText().toString())
                    && udaje.get(i).heslo.equals(eHeslo.getText().toString())){
                ok=true;
                vyberId=(Long)udaje.get(i).id;
                System.out.println("idecko: "+vyberId);
            }
        }
        if(ok){
            Intent intent = new Intent(this, VyberActivity.class);
            intent.putExtra(UZIVATEL_ID_EXTRA,vyberId);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Zadané meno alebo heslo nie je správne", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prihlasenie_activity, menu);
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
        loader.setUri(Uri.parse(WalletContentProvider.CONTENT_URI.toString() + "/prihlasenie"));
        System.out.println("URI: "+WalletContentProvider.CONTENT_URI.toString() + "/prihlasenie");
        return loader;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        udaje = new ArrayList<>();
        while(cursor.moveToNext()){
            Udaj udaj = new Udaj();
            udaj.meno=cursor.getString(0);
            udaj.heslo=cursor.getString(1);
            udaj.id=Long.parseLong(cursor.getString(2));
            udaje.add(udaj);
            System.out.println("Udaj "+udaj.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
