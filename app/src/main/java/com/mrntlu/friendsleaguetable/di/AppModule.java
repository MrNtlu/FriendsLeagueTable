package com.mrntlu.friendsleaguetable.di;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.adapters.leaguesactivity.leaguecontroller.LeaguePlayersRecyclerAdapter;
import com.mrntlu.friendsleaguetable.persistence.LeagueDao;
import com.mrntlu.friendsleaguetable.persistence.LeagueDatabase;
import com.mrntlu.friendsleaguetable.repository.LeagueRepository;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import static com.mrntlu.friendsleaguetable.persistence.LeagueDatabase.*;

@Module
public class AppModule {

    @Singleton
    @Provides
    static LeagueDatabase provideLeagueDatabase(Application application){
        return Room.databaseBuilder(
                application,
                LeagueDatabase.class,
                DATABASE_NAME
        ).build();
    }

    @Singleton
    @Provides
    static LeagueDao provideLeagueDao(LeagueDatabase leagueDatabase){
        return leagueDatabase.getLeagueDao();
    }

    @Singleton
    @Provides
    static LeagueRepository provideLeagueRepository(LeagueDao leagueDao){
        return new LeagueRepository(leagueDao);
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions(){
        return RequestOptions
                .placeholderOf(R.drawable.ic_error_outline_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Provides
    static LeaguePlayersRecyclerAdapter provideLeaguePlayersRecyclerAdapter(){
        return new LeaguePlayersRecyclerAdapter();
    }
}
