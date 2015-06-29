package sk.upjs.ics.wallet;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.wallet.provider.Provider;
import sk.upjs.ics.wallet.provider.WalletContentProvider;

import static sk.upjs.ics.wallet.util.Defaults.*;

public class RegistraciaActivity extends ActionBarActivity implements  LoaderManager.LoaderCallbacks<Cursor>{

    EditText eMeno, eHeslo,eHeslo2,eFinancie;
    Button bRegistracia;
    private static final int INSERT_NOTE_TOKEN = 0;
    private List<String> mena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracia);
        eMeno =(EditText) findViewById(R.id.editMeno);
        eHeslo =(EditText) findViewById(R.id.editHeslo);
        eHeslo2 =(EditText) findViewById(R.id.editHeslo2);
        eFinancie=(EditText) findViewById(R.id.editFinancie);
        bRegistracia=(Button) findViewById(R.id.btnPrihlasenie);
        getLoaderManager().initLoader(0,Bundle.EMPTY,this);

    }

    public void Registruj(View view){
        boolean existuje= false;
        for(int i=0;i<mena.size();i++){
            if(eMeno.getText().toString().equals(mena.get(i).toString())){
                System.out.println("meno"+ mena.get(i).toString());
                existuje=true;
            }
        }
        if(eMeno.getText().toString().equals("")) {
            Toast.makeText(RegistraciaActivity.this, "Zadajte meno", Toast.LENGTH_SHORT)
                    .show();
            return;
        }else if(existuje) {
            Toast.makeText(RegistraciaActivity.this, "Zadané meno už existuje", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else if(!eHeslo.getText().toString().equals(eHeslo2.getText().toString())){
            Toast.makeText(RegistraciaActivity.this, "Hesla sa nezhodujú", Toast.LENGTH_SHORT)
                    .show();
            return;
        }else if(eFinancie.getText().toString().equals("")) {
            Toast.makeText(RegistraciaActivity.this, "Zadajte financie", Toast.LENGTH_SHORT)
                    .show();
            return;
        }else {
            System.out.println("Pred pridaj zapas");
            System.out.println("V pridaj zapas");
            String meno = eMeno.getText().toString();
            String heslo = eHeslo.getText().toString();
            String financie = eFinancie.getText().toString();
            insertIntoContentProvider(meno,heslo,financie);
            System.out.println("V pridaj zapas po inserte");
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registracia, menu);
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
        if (id == R.id.registruj) {
            String meno = eMeno.getText().toString();
            String heslo = eHeslo.getText().toString();
            String financie = eFinancie.getText().toString();
            insertIntoContentProvider(meno,heslo,financie);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void pridajZapas() {
        System.out.println("V pridaj zapas");
        String meno = eMeno.getText().toString();
        String heslo = eHeslo.getText().toString();
        String financie = eFinancie.getText().toString();
        insertIntoContentProvider(meno,heslo,financie);
        System.out.println("V pridaj zapas po inserte");
    }

    public void zrusZapas(View view){
        finish();
    }

    private void insertIntoContentProvider(String meno,String heslo,String financie) {
        Uri uri = WalletContentProvider.CONTENT_URI;
        System.out.println("Uri v inserte "+WalletContentProvider.CONTENT_URI);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.Uzivatel.MENO, meno);
        contentValues.put(Provider.Uzivatel.HESLO,heslo );
        contentValues.put(Provider.Uzivatel.FINANCIE, financie);
        System.out.println("V inserte");
        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(RegistraciaActivity.this, "Registrácia bola úspešna", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        insertHandler.startInsert(INSERT_NOTE_TOKEN, NO_COOKIE, uri, contentValues);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(Uri.parse(WalletContentProvider.CONTENT_URI.toString() + "/mena"));
        System.out.println("URI: "+WalletContentProvider.CONTENT_URI.toString() + "/mena");
        return loader;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mena = new ArrayList<>();
        while(cursor.moveToNext()){
            mena.add(cursor.getString(0));
            System.out.println("cursor meno "+cursor.getString(0));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
