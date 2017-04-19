package ee.ut.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ee.ut.demo.R;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */

public class HomeViewHolder extends RecyclerView.ViewHolder{

    public TextView title;
    public TextView location;
    public TextView date;
    public TextView time;

    public ImageView thumbnail;
    public ImageView overflow;

    public HomeViewHolder(View view) {
        super(view);

        title = (TextView) view.findViewById(R.id.title);
        location = (TextView) view.findViewById(R.id.location);
        date = (TextView) view.findViewById(R.id.date);
        time = (TextView) view.findViewById(R.id.time);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        overflow = (ImageView) view.findViewById(R.id.overflow);
    }
}
