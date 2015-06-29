package sk.upjs.ics.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import sk.upjs.ics.wallet.provider.Provider;
import sk.upjs.ics.wallet.provider.WalletContentProvider;
import static sk.upjs.ics.wallet.util.Defaults.NO_SELECTION;
import static sk.upjs.ics.wallet.util.Defaults.NO_SELECTION_ARGS;
import static sk.upjs.ics.wallet.util.Defaults.NO_COOKIE;

public class ZoznamActivity extends ActionBarActivity implements ZoznamFragment.OnFragmentInteractionListener{

    public static final String UZIVATEL_ID_EX = "vyberId";
    private static final int INSERT_NOTE_TOKEN = 0;
    private static final int DELETE_NOTE_TOKEN = 0;
    private Long userId;
    private long type;
    private MenuItem mSpinnerItem1=null;
    EditText eden,emesiac,erok,esuma,einfo;
    CheckBox cbVklad,cbVyber;
    Button bUloz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoznam);
        userId = (Long) getIntent().getSerializableExtra(UZIVATEL_ID_EX);
        System.out.println("idecko v activity zoznam " + userId);

        if(savedInstanceState != null) {
            if (isSinglePane()) {
                String tag =(String) savedInstanceState.get("tag");
                if(tag!=null){
                    switch (tag){
                        case"ZoznamFragment":
                            Bundle bundle = new Bundle();
                            bundle.putLong("idecko", userId);
                            ZoznamFragment fragInfo = new ZoznamFragment();
                            fragInfo.setArguments(bundle);
                           // ZoznamFragment zoznamFragment=(ZoznamFragment) getFragmentManager().findFragmentByTag("ZoznamFragment");
                            getFragmentManager().beginTransaction().replace(R.id.singleFragmentLayout,fragInfo,"ZoznamFragment")
                                    .commit();
                            break;
                        case"DetailFragment":
                            DetailFragment detialFragment=(DetailFragment) getFragmentManager().findFragmentByTag("DetailFragment");
                            getFragmentManager().beginTransaction().replace(R.id.singleFragmentLayout,detialFragment,"DetailFragment")
                                    .commit();
                            break;
                    }
                }else{
                    showZoznamPane();
                }
            }
            }else{
                if(isSinglePane()){
                    showZoznamPane();
                }
            }
    }

    private void showZoznamPane() {
        Bundle bundle = new Bundle();
        bundle.putLong("idecko", userId);
        ZoznamFragment fragInfo = new ZoznamFragment();
        fragInfo.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.singleFragmentLayout,fragInfo,"ZoznamFragment")
                .commit();
    }
    private void showDetailPane() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.singleFragmentLayout, new DetailFragment(), "DetailFragment")
                .commit();
    }

    public boolean isSinglePane() {
        return findViewById(R.id.singleFragmentLayout) != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.menu_zoznam, menu);
        mSpinnerItem1 = menu.findItem( R.id.menu_spinner1);
        String[] moznosti ={"Vsetko","Vklady","Vybery"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, moznosti);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        View view1 = mSpinnerItem1.getActionView();
        if (view1 instanceof Spinner)
        {
            final Spinner spinner = (Spinner) view1;
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    return;
                }
            });

        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem detailItem = menu.findItem(R.id.detialAction);
        MenuItem zoznamItem = menu.findItem(R.id.zoznamAction);
        MenuItem spinerItem = menu.findItem(R.id.menu_spinner1);
        if(isSinglePane()){
            if(isDetailFragmentShown()){
                zoznamItem.setVisible(true);
                detailItem.setVisible(false);
                spinerItem.setVisible(false);
            }
            if(isZoznamFragmentShown()){
                spinerItem.setVisible(true);
                zoznamItem.setVisible(false);
                detailItem.setVisible(true);
            }
        }else{
            spinerItem.setVisible(true);
            zoznamItem.setVisible(false);
            detailItem.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.zoznamAction:
                showZoznamPane();
                invalidateOptionsMenu();
                return true;
            case R.id.detialAction:
                showDetailPane();
                invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(isSinglePane()) {
            if (isZoznamFragmentShown()) {
                outState.putString("tag", "ZoznamFragment");
                return;
            }
            if (isDetailFragmentShown()) {
                outState.putString("tag", "DetailFragment");
                return;
            }
        }
    }


    private boolean isZoznamFragmentShown() {
        return findViewById(R.id.zoznamFragmentListView)!=null;
    }


    public boolean isDetailFragmentShown() {
        return findViewById(R.id.textDen )!=null;
    }

    @Override
    public void onFragmentInteraction(long id) {
        type = id;
        if(isSinglePane()){
            vymaz(type);
            invalidateOptionsMenu();
        }else{
            vymaz(type);
                return;
            }
            //detailFragment.init;
    }

    public void uloz(View view){
        eden=(EditText) findViewById(R.id.editDen);
        emesiac=(EditText) findViewById(R.id.editMesiac);
        erok=(EditText) findViewById(R.id.editRok);
        esuma=(EditText) findViewById(R.id.editSuma);
        einfo=(EditText) findViewById(R.id.editPolozka);
        cbVklad=(CheckBox) findViewById(R.id.vklad);
        cbVyber=(CheckBox) findViewById(R.id.vyber);
        bUloz=(Button) findViewById(R.id.btnUloz);

        String den=eden.getText().toString();
        String mesiac=emesiac.getText().toString();
        String rok = erok.getText().toString();
        String suma=esuma.getText().toString();
        String info=einfo.getText().toString();
        String ideUziva=userId+"";
        String typ="1";
        if(cbVklad.isChecked() && cbVyber.isChecked()) {
            Toast.makeText(ZoznamActivity.this, "Zaškrtnie len jednu  z možností (vyber/vklad) ", Toast.LENGTH_SHORT)
                    .show();
            return;
        }else if(!cbVklad.isChecked() && !cbVyber.isChecked()){
            Toast.makeText(ZoznamActivity.this, "Zaškrtnie jednu  z možností (vyber/vklad) ", Toast.LENGTH_SHORT)
                    .show();
            return;
        }else if(cbVklad.isChecked() && !cbVyber.isChecked()){
             typ="1";
        }else if(cbVyber.isChecked() && !cbVklad.isChecked()) {
            typ="0";
        }

        insertIntoContentProvider(den,mesiac,rok,suma,typ,info,ideUziva);
        if(isSinglePane()){
            showZoznamPane();
        }
    }



    private void insertIntoContentProvider(String den,String mesiac,String rok,String suma,String typ,String info,String idUziv){
        Uri uri = WalletContentProvider.CONTENT_URI3;
        System.out.println("COntent uri pridanie: "+WalletContentProvider.CONTENT_URI3);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.Transakcia.DAY, den);
        contentValues.put(Provider.Transakcia.MONTH, mesiac);
        contentValues.put(Provider.Transakcia.YEAR, rok);
        contentValues.put(Provider.Transakcia.TYP, typ);
        contentValues.put(Provider.Transakcia.POLOZKA,info);
        contentValues.put(Provider.Transakcia.SUMA, suma);
        contentValues.put(Provider.Transakcia.ID_UZIVATEL, idUziv);


        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(ZoznamActivity.this, "Transakcia bola pridaná", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        insertHandler.startInsert(INSERT_NOTE_TOKEN, NO_COOKIE, uri, contentValues);
    }

    public void vymaz(long id) {
        Uri selectedNoteUri = ContentUris.withAppendedId(WalletContentProvider.CONTENT_URI3, id);
        AsyncQueryHandler deleteHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                Toast.makeText(ZoznamActivity.this, "Transakcia bola zmazaná", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        deleteHandler.startDelete(DELETE_NOTE_TOKEN, NO_COOKIE, selectedNoteUri,
                NO_SELECTION, NO_SELECTION_ARGS);
    }

}
