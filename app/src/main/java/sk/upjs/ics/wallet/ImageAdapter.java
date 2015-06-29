package sk.upjs.ics.wallet;

/**
 * Created by Maťo21 on 28. 6. 2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import de.ecotastic.android.camerautil.util.BitmapHelper;


/**
 * Created by Maťo21 on 13. 6. 2015.
 */
public class ImageAdapter extends BaseAdapter {
    private static final int REQUEST_CODE = 0;
    private Context mContext;
    private ArrayList<Foto> fotky = new ArrayList<>();
    private Activity activity;

    public ImageAdapter(Context c, Activity activity) {
        mContext = c;
        this.activity = activity;
    }

    public int getCount() {
        return fotky.size();
    }

    public void insert(Foto photo, int position){
        fotky.add(position, photo);
    }

    public Foto getItem(int position) {
        return fotky.get(position);
    }

    public long getItemId(int position) {
        return position;    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = inflater.inflate(R.layout.activity_image_adaper,parent,false);
            TextView dateTextView = (TextView) grid.findViewById(R.id.photoGridItemDate);
            ImageView imageView = (ImageView) grid.findViewById(R.id.photoGridItemImageView);

            final Foto foto = fotky.get(position);

            String date = foto.getDay() + "." +foto.getMonth()+ "." + foto.getYear();
            //Log.w(ImageAdapter.class.getName(), "date: " + date);
            dateTextView.setText(date);

            Bitmap photo1 = BitmapHelper.readBitmap(mContext, Uri.parse(fotky.get(position).getUri()));
            if (foto != null){
                imageView.setImageBitmap(photo1);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.w(getClass().getName(), "On photo click!");
                    Intent intent = new Intent(mContext, FotoDetailActivity.class);
                    intent.putExtra("photo", foto.getUri());
                    intent.putExtra("description", foto.getDescription());
                    intent.putExtra("year", foto.getYear());
                    intent.putExtra("month", foto.getMonth());
                    intent.putExtra("day", foto.getDay());
                    intent.putExtra("id", foto.getId());
                    activity.startActivityForResult(intent, REQUEST_CODE);
                }
            });

        } else {
            grid = (View) convertView;
        }

        return grid;
    }

    public void clear() {
        this.fotky.clear();
    }

}
