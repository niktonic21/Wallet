package sk.upjs.ics.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.wallet.provider.Provider;
import sk.upjs.ics.wallet.provider.WalletContentProvider;

public class ZoznamFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,AdapterView.OnItemClickListener{

    private  OnFragmentInteractionListener mListener;
    private static final Bundle NO_BUNDLE = null;
    public static final int LOADER_ID_ZOZNAM = 0;
    private ZoznamAdapter zoznamAdapter;
    private ListView zoznamFragmentListView;
    public ZoznamFragment() {
        // Required empty public constructor
    }
    Long idUzivatel;
    ArrayList<Transakcie> trans;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID_ZOZNAM,NO_BUNDLE,this);
        System.out.println("Id vo fragmente zoznam "+idUzivatel);
        View fragmentLayout = inflater.inflate(R.layout.fragment_zoznam, container, false);
        zoznamFragmentListView=(ListView) fragmentLayout.findViewById(R.id.zoznamFragmentListView);

        ArrayList<Transakcie> trans = new ArrayList<>();
         trans.add(new Transakcie("2","1","23.3","liehovina","12","3","2015"));

        zoznamAdapter=new ZoznamAdapter(this.getActivity(), trans);
        zoznamFragmentListView.setAdapter(zoznamAdapter);
        return fragmentLayout;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        idUzivatel= getArguments().getLong("idecko");
        System.out.println("Id loader ide "+idUzivatel);
        CursorLoader loader = new CursorLoader(this.getActivity());
        loader.setUri(WalletContentProvider.CONTENT_URI3);
        //loader.setUri(Uri.parse(WalletContentProvider.CONTENT_URI3.toString() + "/list/"+idUzivatel));
        System.out.println("URI: " + WalletContentProvider.CONTENT_URI3.toString() + "/list/"+idUzivatel);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Transakcie> trans=new ArrayList<>();
        System.out.println("Som vo fragmente zoznam on LoadFInish");

        if(cursor!=null){
            System.out.println("nemam nulovy cursor "+cursor.toString());
        }
        if(cursor==null){
            System.out.println("nulovy cursor");
        }else{
            //System.out.println(cursor.moveToFirst() + " coursov move to fisrt");
            while(cursor.moveToNext()){
                Transakcie t =new Transakcie();
                t.setIdUzivatel(cursor.getString(0));
                t.setTyp(cursor.getString(1));
                t.setSuma(cursor.getString(2));
                t.setPolozka(cursor.getString(3));
                t.setDay(cursor.getString(4));
                t.setMonth(cursor.getString(5));
                t.setYear(cursor.getString(6));
                String idecko=idUzivatel+"";
                if(cursor.getString(0).equals(idecko)){
                    trans.add(t);
                }
                System.out.println("transakcia"+ t.toString());
            }
        }
        System.out.println("velkost trans v zozonam fragment" + trans.size());
        cursor.close();
        zoznamAdapter.setAll(trans);
        zoznamAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void notifyAdapterDataSetChange() {
        getLoaderManager().restartLoader(LOADER_ID_ZOZNAM, NO_BUNDLE, this);
        this.zoznamAdapter.notifyDataSetChanged();
        //Log.w(getClass().getName(), "adapter notifyDataSetChanged");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener=(OnFragmentInteractionListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
        activity.invalidateOptionsMenu();
        //getLoaderManager().restartLoader(LOADER_ID_ZOZNAM, NO_BUNDLE, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        new AlertDialog.Builder(this.getActivity())
                .setMessage("Chcete vymazať položku?")
                .setTitle("Transakcia")
                .setPositiveButton("Vymaž", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFragmentInteraction(id);
                    }
                })
                .setNegativeButton("Zruš",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;

                    }
                })
                .show();
    }

    public interface  OnFragmentInteractionListener{
        public void onFragmentInteraction(long id);
    }
}
