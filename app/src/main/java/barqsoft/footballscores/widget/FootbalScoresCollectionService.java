package barqsoft.footballscores.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;

/**
 * Created by jonathan.cook on 11/9/2015.
 */
public class FootbalScoresCollectionService extends RemoteViewsService {

    public final String LOG_TAG = FootbalScoresCollectionService.class.getSimpleName();

    private static String[] INFO = new String[] {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL};
    String hometeam = "";
    String awayTeam = "";
    String homeGoals = "";
    String awayGoals = "";
    String gameDetail = "";
    String matchTime = "";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.d(LOG_TAG, "onGetViewFactory");
        FootbalScoresCollectionDataProvider dataProvider = new FootbalScoresCollectionDataProvider(
                getApplicationContext());
        return dataProvider;
    }
}
