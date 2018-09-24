package com.mis.community.rms.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mis.community.rms.fragments.MenuFragment;
import com.mis.community.rms.model.Menu;

import java.util.List;

public class MenuPagerAdapter extends FragmentStatePagerAdapter {

    private List<Menu> menuArray;

    public MenuPagerAdapter(FragmentManager fm, List<Menu> menuList) {
        super(fm);
        this.menuArray = menuList;
    }

    @Override
    public int getCount() {
        return menuArray.size();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment menuFragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putParcelable("MENU", menuArray.get(position));
        menuFragment.setArguments(args);
        return menuFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return menuArray.get(position).getTitle();
    }


}
