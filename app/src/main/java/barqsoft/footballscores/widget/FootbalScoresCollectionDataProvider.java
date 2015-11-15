package barqsoft.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by jonathan.cook on 11/9/2015.
 */
public class FootbalScoresCollectionDataProvider implements RemoteViewsService.RemoteViewsFactory{


    public final String LOG_TAG = FootbalScoresCollectionDataProvider.class.getSimpleName();

    ArrayList<WidgetItem> mTeams;
    Context mContext;

    public FootbalScoresCollectionDataProvider(Context c) {
        mContext = c;
        //mTeams = teams;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        Log.d(LOG_TAG, "onDataSetChanged");

        mTeams = new ArrayList<>();
        String[] INFO = new String[] {
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

        Uri u = DatabaseContract.scores_table.buildScoreWithDate();
        Date today = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String sToday = fmt.format(today);
        final long identityToken = Binder.clearCallingIdentity();

        //fmt = new SimpleDateFormat("dd");
        //sToday = fmt.format(today);
        //int nToday = Integer.parseInt(sToday) - 7;
        //sToday = "2015-11-07";

        Cursor c = mContext.getContentResolver().query(u, INFO, null, new String[] {sToday}, null);
        if (c.moveToFirst()) {
            do {
                hometeam = c.getString(c.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
                awayTeam = c.getString(c.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
                homeGoals = c.getString(c.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
                awayGoals = c.getString(c.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
                matchTime = c.getString(c.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
                fmt = new SimpleDateFormat("EEE, MMM d");
                gameDetail = fmt.format(today);
                if (!homeGoals.equals("-1")) {
                    gameDetail += mContext.getResources().getString(R.string.full_time);
                } else {
                    homeGoals = "";
                    awayGoals = "";
                    gameDetail += " " + mContext.getResources().getString(R.string.at) + " " + matchTime;
                }
                mTeams.add(new WidgetItem(hometeam, awayTeam, homeGoals, awayGoals, gameDetail));
            } while (c.moveToNext());
        } else {
            mTeams.add(new WidgetItem(mContext.getApplicationContext().getResources().getString(R.string.no_games), "", "", "", ""));
        }
        c.close();
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mTeams.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);
        mView.setTextViewText(R.id.home_side, mTeams.get(i).getHomeSide());
        mView.setTextViewText(R.id.away_side, mTeams.get(i).getAwaySide());
        mView.setTextViewText(R.id.home_side_score, mTeams.get(i).getHomeScore());
        mView.setTextViewText(R.id.away_side_score, mTeams.get(i).getAwayScore());
        mView.setTextViewText(R.id.match_time, mTeams.get(i).getGameDetail());

        final Intent fillInIntent = new Intent();
        mView.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return mView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
