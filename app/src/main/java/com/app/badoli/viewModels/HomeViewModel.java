package com.app.badoli.viewModels;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.app.badoli.R;
import com.app.badoli.fragments.HomeFragment;

public class HomeViewModel extends ViewModel {
    protected HomeFragment homeFragment;
   // Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.rootLayout);

    public boolean onBottomNavigationClick(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
               /* if (!(currentFragment instanceof HomeFragment)) {
                    homeFragment = new HomeFragment();
                    pushFragment(homeFragment);
                }*/

                return true;
            case R.id.navigation_profile:

                return true;
            case R.id.navigation_transaction:

                return true;

            case R.id.navigation_help:

                return true;
        }
        return true;
    }
}
