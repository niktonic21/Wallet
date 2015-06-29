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
import android.widget.TextView;
import android.widget.Toast;

import sk.upjs.ics.wallet.provider.Provider;
import sk.upjs.ics.wallet.provider.WalletContentProvider;
import static sk.upjs.ics.wallet.ZoznamActivity.UZIVATEL_ID_EX;

public class VyberActivity extends ActionBarActivity implements  LoaderManager.LoaderCallbacks<Cursor> {
    private static final int UZIVATEL_LOADER_ID = 0;

    TextView lblFinancie;
    Button bZoznam,bStatistiky,bBankomaty;
    //public static final String ZAPAS_BUNDLE_KEY = "zapas";
    public static final String UZIVATEL_ID_EXTRA = "vyberId";
    Long vyberId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vyberId = (Long) getIntent().getSerializableExtra(UZIVATEL_ID_EXTRA);
        System.out.println("ID " + vyberId);
        getLoaderManager().initLoader(UZIVATEL_LOADER_ID, Bundle.EMPTY, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vyber);

        lblFinancie=(TextView) findViewById(R.id.editFinancie);
        bZoznam=(Button) findViewById(R.id.btnZoznam);
        bBankomaty=(Button) findViewById(R.id.btnBankomaty);
        bStatistiky=(Button) findViewById(R.id.btnStatistiky);
    }

    public void bankomaty(View view) {
        Toast.makeText(this, "Bankomaty", Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, MapaCesta.class);
        startActivity(intent);
    }
    public void vyberZoznam(View view) {
        Toast.makeText(this, "Zoznam", Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, ZoznamActivity.class);
        intent.putExtra(UZIVATEL_ID_EX,vyberId);
        startActivity(intent);
    }

    public void statistiky(View view) {
        Toast.makeText(this, "Statistiky", Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, StatistikyActivity.class);
        intent.putExtra(UZIVATEL_ID_EX,vyberId);
        startActivity(intent);
    }
    public void fotky(View view) {
        Toast.makeText(this, "Foto", Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, FotoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vyber, menu);
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
        loader.setUri(Uri.parse(WalletContentProvider.CONTENT_URI.toString()+"/"+vyberId));
        System.out.println("Uricko id " + WalletContentProvider.CONTENT_URI.toString() + "/" + vyberId);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext()){
            String financie =cursor.getString(cursor.getColumnIndex(Provider.Uzivatel.FINANCIE));
            StringBuilder sb=new StringBuilder();
            sb.append(financie);
            sb.append(" €");
            lblFinancie.setText(sb);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
