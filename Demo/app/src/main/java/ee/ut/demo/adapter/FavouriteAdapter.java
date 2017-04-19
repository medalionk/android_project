package ee.ut.demo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ee.ut.demo.R;
import ee.ut.demo.helpers.Parse;
import ee.ut.demo.mvp.model.Event;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */

public class FavouriteAdapter  extends RecyclerView.Adapter<FavouriteViewHolder> {

    private Context mContext;
    private List<Event> mEvents;
    private static FavoriteListener mListener;

    public FavouriteAdapter(Context mContext) {
        this.mContext = mContext;
        this.mEvents = new ArrayList<>();
    }

    public void addAll(Collection<Event> events) {
        this.mEvents.addAll(events);
        notifyDataSetChanged();
    }

    public void add(Event event) {
        this.mEvents.add(event);
        notifyDataSetChanged();
    }

    public void replaceEvents(Collection<Event> events) {
        mEvents.clear();
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    public void setFavoriteListener(FavoriteListener listener){
        mListener = listener;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, int position) {
        final Event event = mEvents.get(position);

        holder.title.setText(event.getTitle());
        holder.location.setText(event.getLocation());
        holder.date.setText(event.getDetails().getDate());
        holder.time.setText(event.getStartTime() + "-" + event.getEndTime());

        Picasso.with(mContext)
                .load(Uri.parse(Parse.imageUrl(event.getDetails().getImageUrl())))
                .into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, event);
            }
        });
    }

    private void showPopupMenu(View view, Event event) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(event));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        final Event event;
        public MyMenuItemClickListener(Event event) {
            this.event = event;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_remove_favourite:
                    mListener.toggleFavourite(event.getId());
                    Toast.makeText(mContext, "Removed from favourites", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
