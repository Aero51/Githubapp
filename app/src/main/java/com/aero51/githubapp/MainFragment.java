package com.aero51.githubapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aero51.githubapp.db.model.Repo;

import static com.aero51.githubapp.utils.Constants.DEFAULT_QUERY;
import static com.aero51.githubapp.utils.Constants.LAST_SEARCH_QUERY;
import static com.aero51.githubapp.utils.Constants.LOG;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private SharedViewModel sharedViewModel;

    private TextView emptyListTextView;
    private RecyclerView recyclerView;
    private MainAdapter adapter;


    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the query to search
         String query = DEFAULT_QUERY;
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(LAST_SEARCH_QUERY, DEFAULT_QUERY);
        }
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(MainViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        //Post the query to be searched
        mViewModel.searchRepo(query);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        emptyListTextView = view.findViewById(R.id.emptyList);
        emptyListTextView.setText(getContext().getResources().getString(R.string.no_results, "\uD83D\uDE13"));
        //Initialize RecyclerView

        recyclerView = view.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        initAdapter();

        observeSearchQuery();
        observeSortFlags();
        String query = DEFAULT_QUERY;


        return view;
    }




    /**
     * Initializes the Adapter of RecyclerView which is {@link MainAdapter}
     */
    private void initAdapter() {
        adapter = new MainAdapter(getContext());
        recyclerView.setAdapter(adapter);

        //Subscribing to receive the new PagedList Repos
        mViewModel.getRepos().observe(getViewLifecycleOwner(), new Observer<PagedList<Repo>>() {
            @Override
            public void onChanged(PagedList<Repo> repos) {
                if (repos != null) {
                    Log.d(LOG, "initAdapter: Repo List size: " + repos.size());
                    MainFragment.this.showEmptyList(repos.size() == 0);
                    adapter.submitList(repos);
                }
            }
        });
    }

    private void observeSearchQuery(){
        sharedViewModel.getSearchQuery().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String query) {
                Log.d(LOG, "MainFragment get search query: " + query);
                updateRepoListFromInput(query);
            }
        });
    }
    private void observeSortFlags(){
        sharedViewModel.getSortFlag().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(LOG, "MainFragment get sort flag: " + integer);
            }
        });

    }

    /**
     * Shows the Empty view when the list is empty
     *
     * @param show Displays the empty view and hides the list when the boolean is <b>True</b>
     */
    private void showEmptyList(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            emptyListTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListTextView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_SEARCH_QUERY, mViewModel.lastQueryValue());
    }


    private void updateRepoListFromInput(String query) {
        if (!TextUtils.isEmpty(query)) {
            recyclerView.scrollToPosition(0);
            //Posts the query to be searched
            mViewModel.searchRepo(query);
            //Resets the old list
            adapter.submitList(null);
        }
    }
}