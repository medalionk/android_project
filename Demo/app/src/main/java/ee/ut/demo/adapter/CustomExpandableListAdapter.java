package ee.ut.demo.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.helpers.Parse;
import ee.ut.demo.mvp.model.Event;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    @Bind(R.id.eventTitle)
    TextView titleView;

    @Bind(R.id.eventTime)
    TextView timeView;

    @Bind(R.id.eventLocation)
    TextView locationView;

    @Bind(R.id.eventOrganizer)
    TextView organizerView;

    @Bind(R.id.eventInfo)
    TextView eventInfoView;

    @Bind(R.id.eventUrl)
    TextView urlView;

    @Bind(R.id.eventImage)
    ImageView imageView;

    private static FavoriteListener mListener;
    private Context mContext;
    private List<Event> mEvents;

    public CustomExpandableListAdapter(Context context) {
        mContext = context;
        mEvents = new ArrayList<>();
    }

    public void setFavoriteListener(FavoriteListener listener){
        mListener = listener;
    }

    public void addAll(Collection<Event> events) {
        this.mEvents.addAll(events);
        Collections.sort(mEvents);
        notifyDataSetChanged();
    }

    public void add(Event event) {
        this.mEvents.add(event);
        Collections.sort(mEvents);
        notifyDataSetChanged();
    }

    public void replaceEvents(Collection<Event> events) {
        mEvents.clear();
        mEvents.addAll(events);
        Collections.sort(mEvents);
        notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return mEvents.get(listPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Event event = (Event) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expanded_list, null);
        }

        ButterKnife.bind(this,convertView);

        titleView.setText(event.getTitle());
        timeView.setText(event.getStartTime() + "-" + event.getEndTime());
        locationView.setText(event.getLocation());
        organizerView.setText(event.getDetails().getOrganizer());

        String contact = Parse.splitJoinString(event.getDetails().getAdditionalInfo(), ",", "\n");
        eventInfoView.setText(contact);

        urlView.setMovementMethod(LinkMovementMethod.getInstance());
        urlView.setText(Parse.fromHtml("<a href=\""+ event.getDetails().getPublicUrl() + "\">"
                + event.getTitle() + "</a>"));
        urlView.setClickable(true);

        Picasso.with(mContext)
                .load(Parse.imageUrl(event.getDetails().getImageUrl()))
                .fit()
                .centerCrop()
                .into(imageView);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return mEvents.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return mEvents.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(final int listPosition, final boolean isExpanded,
                             View convertView, final ViewGroup parent) {

        final Event event = (Event) getGroup(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.eventTitleGrp);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setText(event.getTitle());

        TextView titleTime = (TextView) convertView.findViewById(R.id.eventTimeGrp);
        titleTime.setText(event.getStartTime() + "-" + event.getEndTime());

        int imgResourceId = event.isFavorite() ? R.mipmap.ic_favorite_selected : R.mipmap.ic_favorite_unselected;
        ImageView imageView = (ImageView) convertView.findViewById(R.id.favorite);

        imageView.setImageResource(imgResourceId);
        imageView.setColorFilter(0xFFEA7608);
        imageView.setOnClickListener(new View.OnClickListener(){
            String id = event.getId();
            @Override
            public void onClick(View v){
                mListener.toggleFavourite(id);
                toggleFavorite(id);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void toggleFavorite(String id){
        for (int i = 0; i < mEvents.size(); i++) {
            if(mEvents.get(i).getId().equals(id)){
                mEvents.get(i).setFavorite(!mEvents.get(i).isFavorite());
                return;
            }
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
