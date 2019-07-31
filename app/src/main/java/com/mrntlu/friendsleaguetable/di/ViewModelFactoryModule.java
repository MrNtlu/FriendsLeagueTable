package com.mrntlu.friendsleaguetable.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mrntlu.friendsleaguetable.viewmodels.LeaguesViewModel;
import com.mrntlu.friendsleaguetable.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelFactoryModule {
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

    @Binds
    @IntoMap
    @ViewModelKey(LeaguesViewModel.class)
    public abstract ViewModel bindLeaguesViewModel(LeaguesViewModel leaguesViewModel);

}
