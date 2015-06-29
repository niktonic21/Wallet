package sk.upjs.ics.wallet;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import sk.upjs.ics.wallet.provider.Provider;
import sk.upjs.ics.wallet.provider.WalletContentProvider;
import static sk.upjs.ics.wallet.util.Defaults.NO_COOKIE;


public class DetailFragment extends Fragment {

    EditText eden,emesiac,erok,esuma,einfo;
    CheckBox cbVklad,cbVyber;
    Button bUloz;
    private static final int INSERT_NOTE_TOKEN=0;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_detail, container, false);
        eden=(EditText) fragmentLayout.findViewById(R.id.editDen);
        emesiac=(EditText) fragmentLayout.findViewById(R.id.editMesiac);
        erok=(EditText) fragmentLayout.findViewById(R.id.editRok);
        esuma=(EditText) fragmentLayout.findViewById(R.id.editSuma);
        einfo=(EditText) fragmentLayout.findViewById(R.id.editPolozka);
        cbVklad=(CheckBox) fragmentLayout.findViewById(R.id.vklad);
        cbVyber=(CheckBox) fragmentLayout.findViewById(R.id.vyber);
        bUloz=(Button) fragmentLayout.findViewById(R.id.btnUloz);

        return fragmentLayout;
    }



}
