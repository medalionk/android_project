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
    public TextView except;
    //public TextView imageUrl;
    public TextView publicUrl;

    public ImageView thumbnail;
    public ImageView overflow;

    public HomeViewHolder(View view) {
        super(view);

        title = (TextView) view.findViewById(R.id.title);
        except = (TextView) view.findViewById(R.id.except);
        publicUrl = (TextView) view.findViewById(R.id.link);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        overflow = (ImageView) view.findViewById(R.id.overflow);
    }
}
