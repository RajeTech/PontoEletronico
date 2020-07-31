package com.pm.ramilton.pontoeletronico.Adaptador;

import com.pm.ramilton.pontoeletronico.fragment_Historico;
import com.pm.ramilton.pontoeletronico.fragment_PontoEletronico;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position)   ;
    }

    @Override
    public int getCount() {
        return fragmentListTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitles.get(position);
    }

    public void AddFragment(fragment_PontoEletronico fragment, String Title){
        fragmentList.add(fragment);
        fragmentListTitles.add(Title);
    }
    public void AddFragment(fragment_Historico fragment, String Title){
        fragmentList.add(fragment);
        fragmentListTitles.add(Title);
    }

}
