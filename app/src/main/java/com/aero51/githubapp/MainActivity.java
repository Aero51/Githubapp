package com.aero51.githubapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aero51.githubapp.utils.Constants;

import dagger.hilt.android.AndroidEntryPoint;

import static com.aero51.githubapp.utils.Constants.FORKS_SORT_FLAG;
import static com.aero51.githubapp.utils.Constants.STARS_SORT_FLAG;
import static com.aero51.githubapp.utils.Constants.UPDATED_SORT_FLAG;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
      /*  if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new NewMainFragment())
                    .commitNow();
        }
*/
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(Constants.LOG, " onQueryTextSubmit: " + query);
                sharedViewModel.sendSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Constants.LOG, " onQueryTextChange: " + newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
*/



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        View menuItemView = findViewById(item.getItemId());
        showPopupMenu(menuItemView);
        return super.onOptionsItemSelected(item);
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_order_by, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_stars:
                        sharedViewModel.sendSortFlag(STARS_SORT_FLAG);
                        return true;
                    case R.id.action_forks:
                        sharedViewModel.sendSortFlag(FORKS_SORT_FLAG);
                        return true;
                    case R.id.action_updated:
                        sharedViewModel.sendSortFlag(UPDATED_SORT_FLAG);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
}
