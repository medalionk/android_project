package ee.ut.demo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import ee.ut.demo.R;
import ee.ut.demo.data.ExpandableListDataPump;
import ee.ut.demo.mvp.model.Event;

/**
 * @Source: http://www.journaldev.com/9942/android-expandablelistview-example-tutorial
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mExpandableListTitle;
    private HashMap<String, List<String>> mExpandableListDetail;
    private List<Event> events = new ArrayList<>();

    public CustomExpandableListAdapter(Context context) {
        mContext = context;
        //mExpandableListTitle = new ArrayList<>();
        //mExpandableListDetail = new HashMap<>();
        mExpandableListDetail = ExpandableListDataPump.getData();
        mExpandableListTitle = new ArrayList<>(mExpandableListDetail.keySet());
    }

    public void addAll(Collection<Event> events) {
        this.events.addAll(events);
        notifyDataSetChanged();
    }

    public void add(Event event) {
        this.events.add(event);
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return mExpandableListDetail.get(mExpandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expanded_list, null);
        }

        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expanded_list_item);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return mExpandableListDetail.get(mExpandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return mExpandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return mExpandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

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
