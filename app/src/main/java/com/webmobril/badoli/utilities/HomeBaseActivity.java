package com.webmobril.badoli.utilities;

import android.graphics.Color;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.webmobril.badoli.fragments.HelpFragment;
import com.webmobril.badoli.fragments.HomeFragment;
import com.webmobril.badoli.fragments.ProfileFragment;
import com.webmobril.badoli.fragments.TransactionFragment;
import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityHomePageBinding;

public abstract class HomeBaseActivity extends AppCompatActivity {

    protected HomeFragment homeFragment;
    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.rootLayout);

    protected void setBottomNavigationView(ActivityHomePageBinding homePageBinding, int icon_id) {

        switch (icon_id) {

            case R.id.navigation_home:

                homePageBinding.commonHeader.hamburger.setBackgroundDrawable(getResources().getDrawable(R.drawable.hamburger));
                homePageBinding.commonHeader.mainLayout.setBackgroundResource(R.mipmap.home_header_bgg);
                homePageBinding.commonHeader.header.setBackgroundColor(Color.TRANSPARENT);
                homePageBinding.commonHeader.hamburger.setVisibility(View.VISIBLE);
                homePageBinding.commonHeader.badoliPhoneText.setVisibility(View.GONE);
                homePageBinding.commonHeader.balanceLayout.setVisibility(View.GONE);
                break;

            case R.id.navigation_profile:

                homePageBinding.commonHeader.mainLayout.setBackground(null);
                homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
                homePageBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.text_orange));
                homePageBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
                homePageBinding.commonHeader.badoliPhoneText.setText("User profile");
                break;

            case R.id.navigation_transaction:

                homePageBinding.commonHeader.mainLayout.setBackground(null);
                homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
                homePageBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.text_orange));
                homePageBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
                homePageBinding.commonHeader.badoliPhoneText.setText("Transactions");
                break;

            case R.id.navigation_help:

                homePageBinding.commonHeader.mainLayout.setBackground(null);
                homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
                homePageBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.text_orange));
                homePageBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
                homePageBinding.commonHeader.badoliPhoneText.setText("Help");
                break;
        }

    }

    protected void loadFragment(int viewID) {

        switch (viewID) {
            case R.id.home_view:

                if (!(currentFragment instanceof HomeFragment)) {
                    homeFragment = new HomeFragment();
                    pushFragment(homeFragment);
                }
                break;
            case R.id.profile_view:

                if (!(currentFragment instanceof ProfileFragment)) {
                    ProfileFragment profileFragment = new ProfileFragment();
                    pushFragment(profileFragment);
                }
                break;
            case R.id.transaction_view:

                if (!(currentFragment instanceof TransactionFragment)) {
                    TransactionFragment transactionFragment = new TransactionFragment();
                    pushFragment(transactionFragment);
                }

                break;
            case R.id.help_view:
                if (!(currentFragment instanceof HelpFragment)) {
                    HelpFragment helpFragment = new HelpFragment();
                    pushFragment(helpFragment);
                }
                break;
        }
    }
    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ft != null) {
            ft.replace(R.id.rootLayout, fragment);
            ft.commit();
        }
    }

}
