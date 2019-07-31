package com.mrntlu.friendsleaguetable;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mrntlu.friendsleaguetable.persistence.LeagueDao;
import com.mrntlu.friendsleaguetable.persistence.LeagueDatabase;

import org.junit.After;
import org.junit.Before;

public abstract class LeagueDatabaseTest {

    private LeagueDatabase leagueDatabase;

    public LeagueDao getLeagueDao(){
        return leagueDatabase.getLeagueDao();
    }

    @Before
    public void init(){
        leagueDatabase= Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                LeagueDatabase.class
        ).build();

        InstrumentationRegistry.getInstrumentation().getContext().deleteDatabase(LeagueDatabase.DATABASE_NAME);
    }

    @After
    public void finish(){
        leagueDatabase.close();
    }
}
