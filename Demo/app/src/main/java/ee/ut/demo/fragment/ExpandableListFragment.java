package ee.ut.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ee.ut.demo.R;
import ee.ut.demo.adapter.CustomExpandableListAdapter;
import ee.ut.demo.data.ExpandableListDataPump;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */
public class ExpandableListFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private HashMap<String, List<String>> mExpandableListDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expandable_list, container, false);

        mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.expandable_list);
        mExpandableListDetail = ExpandableListDataPump.getData();

        mExpandableListTitle = new ArrayList<>(mExpandableListDetail.keySet());
        mExpandableListAdapter = new CustomExpandableListAdapter(rootView.getContext(),
                mExpandableListTitle, mExpandableListDetail);

        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView
                        .getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                return false;
            }
        });

        return rootView;
    }
}
