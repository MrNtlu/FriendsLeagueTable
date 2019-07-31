package com.mrntlu.friendsleaguetable;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.mrntlu.friendsleaguetable.models.League;
import com.mrntlu.friendsleaguetable.models.LeaguePlayers;
import com.mrntlu.friendsleaguetable.models.Player;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LeagueDaoTest extends LeagueDatabaseTest {

    //https://github.com/MrNtlu/UnitTesting-Practice
    //https://stackoverflow.com/questions/53301274/room-insert-one-to-many-relation
    //https://github.com/relativizt/android-room-one-to-many-auto-pk/tree/master/app/src/main/java/com/arc/roomonetomany/datasource
    //https://stackoverflow.com/questions/44667160/android-room-insert-relation-entities-using-room

    private final static String TAG="TEST";

    public final static League TEST_LEAGUE=new League("Test League", League.LeagueType.SPORT, League.LeagueStatus.ONGOING,new Date());
    public final static League TEST_LEAGUE2=new League("League Num 2", League.LeagueType.FPS, League.LeagueStatus.FINISHED,new Date());

    public final static Player TEST_PLAYER1=new Player("Burak",0,0,0,0,0);
    public final static Player TEST_PLAYER2=new Player("Bugra",1,1,0,4,1);
    public final static Player TEST_PLAYER3=new Player("Harun",0,1,0,0,1);

    public static ArrayList<Player> setPlayers() {
        return (new ArrayList<Player>(){{
            add(TEST_PLAYER1);
            add(TEST_PLAYER2);
        }});
    }

    @Rule
    public InstantTaskExecutorRule rule=new InstantTaskExecutorRule();

    @Test
    public void insertRead(){

        getLeagueDao().insertNewLeague(TEST_LEAGUE,setPlayers());

        List<League> leagues=getLeagueDao().getLeagues();
        assertNotNull(leagues);

        LeaguePlayers insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());
        assertNotNull(insertedLeagues);

        assertEquals(TEST_LEAGUE.getName(),insertedLeagues.league.getName());
        assertEquals(TEST_LEAGUE.getId(),insertedLeagues.league.getId());
        assertEquals(TEST_PLAYER1.getId(),insertedLeagues.players.get(0).getId());
        assertEquals(TEST_PLAYER1.getName(),insertedLeagues.players.get(0).getName());
        assertEquals(TEST_PLAYER2.getName(),insertedLeagues.players.get(1).getName());
    }

    @Test
    public void insertRead_deleteRead() {
        getLeagueDao().insertNewLeague(TEST_LEAGUE,setPlayers());

        List<League> leagues=getLeagueDao().getLeagues();
        assertNotNull(leagues);

        LeaguePlayers insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());
        assertNotNull(insertedLeagues);
        assertEquals(1,leagues.size());

        getLeagueDao().deleteLeague(insertedLeagues.league);

        leagues=getLeagueDao().getLeagues();
        insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());

        assertNull(insertedLeagues);
        assertEquals(0,leagues.size());
    }

    @Test
    public void multipleInsertRead_deleteOneRead() {
        getLeagueDao().insertNewLeague(TEST_LEAGUE,setPlayers());
        getLeagueDao().insertNewLeague(TEST_LEAGUE2,new ArrayList<Player>(){{add(TEST_PLAYER3);}});

        List<League> leagues=getLeagueDao().getLeagues();
        List<Player> players=getLeagueDao().getPlayers();

        assertEquals(2,leagues.size());
        assertEquals(3,players.size());
        assertEquals(TEST_LEAGUE.getId(),leagues.get(0).getId());
        assertEquals(TEST_LEAGUE2.getId(),leagues.get(1).getId());

        getLeagueDao().deleteLeague(leagues.get(0));
        leagues=getLeagueDao().getLeagues();
        players=getLeagueDao().getPlayers();
        assertEquals(1,leagues.size());
        assertEquals(1,players.size());
    }

    @Test
    public void insertRead_updateRead() {
        getLeagueDao().insertNewLeague(TEST_LEAGUE,setPlayers());

        List<League> leagues=getLeagueDao().getLeagues();
        assertNotNull(leagues);

        LeaguePlayers insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());
        assertNotNull(insertedLeagues);
        assertEquals(1,leagues.size());

        TEST_LEAGUE.setName("Updated league");
        TEST_LEAGUE.setStatus(League.LeagueStatus.FINISHED);
        getLeagueDao().updateLeague(TEST_LEAGUE);

        leagues=getLeagueDao().getLeagues();
        assertEquals(TEST_LEAGUE.getName(),leagues.get(0).getName());
        assertEquals(TEST_LEAGUE.getStatus(),leagues.get(0).getStatus());
    }

    @Test
    public void insertRead_insertPlayerRead(){
        getLeagueDao().insertNewLeague(TEST_LEAGUE,setPlayers());
        getLeagueDao().insertNewLeague(TEST_LEAGUE2,new ArrayList<Player>(){{add(TEST_PLAYER3);}});

        LeaguePlayers insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());
        LeaguePlayers insertedLeagues2=getLeagueDao().getPlayersByLeague(TEST_LEAGUE2.getName());
        assertNotNull(insertedLeagues);
        assertNotNull(insertedLeagues2);

        getLeagueDao().insertNewPlayer(TEST_LEAGUE,new Player(TEST_PLAYER3));
        getLeagueDao().insertNewPlayer(TEST_LEAGUE2,new Player(TEST_PLAYER1));

        insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());
        insertedLeagues2=getLeagueDao().getPlayersByLeague(TEST_LEAGUE2.getName());
        assertEquals(3,insertedLeagues.players.size());
        assertEquals(2,insertedLeagues2.players.size());
    }

    @Test
    public void insertRead_updatePlayer_updateLeagueRead(){
        getLeagueDao().insertNewLeague(TEST_LEAGUE,setPlayers());
        LeaguePlayers insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());

        assertEquals(TEST_LEAGUE.getId(),insertedLeagues.players.get(0).getLeagueId());
        assertEquals(TEST_LEAGUE.getId(),insertedLeagues.league.getId());

        Player player=insertedLeagues.players.get(0);
        player.setName("Changed name");
        player.setWin(90);
        getLeagueDao().updatePlayer(player);

        insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());

        assertEquals(TEST_PLAYER1.getId(),insertedLeagues.players.get(0).getId());
        assertNotEquals(TEST_PLAYER1.getName(),insertedLeagues.players.get(0).getName());
        assertNotEquals(TEST_PLAYER1.getWin(),insertedLeagues.players.get(0).getWin());

        League league=insertedLeagues.league;
        league.setName("Changed League");
        league.setStatus(League.LeagueStatus.FINISHED);
        league.setType(League.LeagueType.MOBA);
        getLeagueDao().updateLeague(league);

        insertedLeagues=getLeagueDao().getPlayersByLeague(TEST_LEAGUE.getName());
        assertNull(insertedLeagues);

        insertedLeagues=getLeagueDao().getPlayersByLeague("Changed League");

        assertEquals(TEST_LEAGUE.getId(),insertedLeagues.league.getId());
        assertNotEquals(TEST_LEAGUE.getName(),insertedLeagues.league.getName());
        assertNotEquals(TEST_LEAGUE.getStatus(),insertedLeagues.league.getStatus());
        assertNotEquals(TEST_LEAGUE.getType(),insertedLeagues.league.getType());
    }
}
