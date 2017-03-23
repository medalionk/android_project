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
import ee.ut.demo.mvp.model.Event;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */

public class FavouriteAdapter  extends RecyclerView.Adapter<FavouriteViewHolder> {

    private Context mContext;
    private List<Event> mEvents;

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

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.title.setText(event.getTitle());
        holder.location.setText(event.getLocation());
        holder.time.setText(event.getTime());

        Picasso.with(mContext)
                .load(Uri.parse(event.getDetails().getImageUrl()))
                .into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_remove_favourite:
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
