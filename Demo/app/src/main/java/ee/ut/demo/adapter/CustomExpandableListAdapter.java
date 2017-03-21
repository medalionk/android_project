package ee.ut.demo.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.R;
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

    @Bind(R.id.eventImage)
    ImageView imageView;

    private Context mContext;
    private List<Event> mEvents;

    public CustomExpandableListAdapter(Context context) {
        mContext = context;
        mEvents = new ArrayList<>();
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
        timeView.setText(event.getTime());
        locationView.setText(event.getLocation());
        organizerView.setText(event.getDetails().getOrganizer());
        eventInfoView.setText(event.getDetails().getAdditionalInfo());

        Picasso.with(mContext)
                .load(event.getDetails().getImagePath())
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
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        Event event = (Event) getGroup(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.eventTitleGrp);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setText(event.getTitle());

        TextView titleTime = (TextView) convertView.findViewById(R.id.eventTimeGrp);
        titleTime.setText(event.getTime());

        TextView titleLocation = (TextView) convertView.findViewById(R.id.eventLocationGrp);
        titleLocation.setText(event.getLocation());

        return convertView;
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
