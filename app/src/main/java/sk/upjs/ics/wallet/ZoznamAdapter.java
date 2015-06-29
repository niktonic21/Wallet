package sk.upjs.ics.wallet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ZoznamAdapter extends BaseAdapter {

    private ArrayList<Transakcie> list;
    private Context context;
    private Resources res;

    public ZoznamAdapter() {
    }

    public ZoznamAdapter(Context context,ArrayList<Transakcie> list){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.zoznam_adapter, parent, false);

        TextView dateView = (TextView) rowView.findViewById(R.id.zoznamListViewDate);
        TextView sumaView = (TextView) rowView.findViewById(R.id.zoznamListViewSuma);
        TextView polozkaView = (TextView) rowView.findViewById(R.id.zoznamListViewPolozka);

        Transakcie trans = list.get(position);
        StringBuilder sb = new StringBuilder();
        sb.append(trans.getDay());
        sb.append(".");
        sb.append(trans.getMonth());
        sb.append(".");
        sb.append(trans.getYear());

        dateView.setText(sb.toString());

        StringBuilder sb2 = new StringBuilder();
        String znamienko;
        if(trans.getTyp().equals("0")){
            znamienko="-";
            sumaView.setBackgroundColor(Color.RED);
            dateView.setBackgroundColor(Color.RED);
            polozkaView.setBackgroundColor(Color.RED);
        }else {
            znamienko="+";
            sumaView.setBackgroundColor(Color.GREEN);
            dateView.setBackgroundColor(Color.GREEN);
            polozkaView.setBackgroundColor(Color.GREEN);
        }
        sb2.append(znamienko);
        sb2.append(trans.getSuma());
        sb2.append(" € ");
        sumaView.setText(sb2.toString());

        polozkaView.setText(trans.getPolozka());

        return rowView;
    }
    public void setAll(ArrayList<Transakcie> trans){
        list.clear();
        list.addAll(trans);
        System.out.println("velkost listu "+list.size());
        for(int i=0;i<list.size();i++){
            System.out.print("List :" + list.get(i).toString());
        }
    }
}
