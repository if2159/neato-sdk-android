package com.neatorobotics.sdk.android.example.robots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neatorobotics.sdk.android.NeatoCallback;
import com.neatorobotics.sdk.android.NeatoClient;
import com.neatorobotics.sdk.android.NeatoError;
import com.neatorobotics.sdk.android.example.R;
import com.neatorobotics.sdk.android.model.NeatoRobot;

import java.util.ArrayList;

public class RobotsFragment extends Fragment {

    private static final String TAG = "RobotsFragment";

    private NeatoClient neatoClient;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;

    private ArrayList<NeatoRobot> robots = new ArrayList<>();

    public RobotsFragment() {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ROBOT_LIST",robots);
    }

    private void restoreState(Bundle inState) {
        robots = (ArrayList<NeatoRobot>) inState.getSerializable("ROBOT_LIST");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        neatoClient = NeatoClient.getInstance(getContext());

        if(savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_robots, container, false);
        //recycler view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.robot_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RobotsListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //swipe to refresh
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRobots();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimaryDark);
        //end swipe to refresh
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null) {
            loadRobots();
        }
        mAdapter.notifyDataSetChanged();
    }

    private void loadRobots() {
        neatoClient.loadRobots(new NeatoCallback<ArrayList<NeatoRobot>>(){
            @Override
            public void done(ArrayList<NeatoRobot> result) {
                super.done(result);
                robots.clear();
                robots.addAll(result);
                swipeContainer.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void fail(NeatoError error) {
                super.fail(error);
                swipeContainer.setRefreshing(false);
                if(error == NeatoError.INVALID_TOKEN) {
                    Toast.makeText(getContext(), "Your session is expired.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class RobotsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView robotName,robotModel;

            public ItemViewHolder(View v) {
                super(v);
                this.robotName = (TextView) v.findViewById(R.id.robotName);
                this.robotModel = (TextView) v.findViewById(R.id.robotModel);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Toast.makeText(getContext(),"Tap position "+position, Toast.LENGTH_SHORT).show();
            }
        }

        public RobotsListAdapter() {}

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.robot_list_item, parent, false);

            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((ItemViewHolder)holder).robotName.setText(robots.get(position).getName());
            ((ItemViewHolder)holder).robotModel.setText(robots.get(position).getModel());
        }


        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return robots.size();
        }
    }
}