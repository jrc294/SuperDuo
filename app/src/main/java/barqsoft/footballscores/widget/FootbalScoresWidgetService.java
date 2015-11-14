package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by jonathan.cook on 11/8/2015.
 */
public class FootbalScoresWidgetService extends IntentService {

    private static String[] INFO = new String[] {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL};
    String hometeam;
    String awayTeam;
    String homeGoals;
    String awayGoals;
    String gameDetail;
    String matchTime;

    public FootbalScoresWidgetService() {
        super("FootbalScoresWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FootbalScoresWidgetProvider.class));

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_layout);

            Intent mainIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);


            Uri u = DatabaseContract.scores_table.buildScoreWithDate();
            Date today = new Date();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String sToday = fmt.format(today);

            //sToday = "2015-11-08";
            //fmt = new SimpleDateFormat("dd");
            //sToday = fmt.format(today);
            //int nToday = Integer.parseInt(sToday) - 7;
            //sToday = "2015-11-07";

            Cursor c = this.getContentResolver().query(u, INFO, null, new String[] {sToday}, null);
            if (c.moveToFirst()) {
                hometeam = c.getString(c.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
                awayTeam = c.getString(c.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
                homeGoals = c.getString(c.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
                awayGoals = c.getString(c.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
                matchTime = c.getString(c.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
                fmt = new SimpleDateFormat("EEE, MMM d");
                gameDetail = fmt.format(today);
                if (!homeGoals.equals("-1")) {
                    gameDetail += this.getResources().getString(R.string.full_time);
                    views.setContentDescription(R.id.home_side, getResources().getString(R.string.final_score) + " " + hometeam);
                    views.setContentDescription(R.id.match_time, getResources().getString(R.string.on) + " " + gameDetail);
                    views.setTextViewText(R.id.home_side_score, homeGoals);
                    views.setTextViewText(R.id.away_side_score, awayGoals);
                } else {
                    gameDetail += " " + this.getResources().getString(R.string.at) + " " + matchTime;
                    views.setContentDescription(R.id.home_side, hometeam + " " +
                        getResources().getString(R.string.verses));
                    views.setContentDescription(R.id.away_side, awayTeam + " " + getResources().getString(R.string.on));
                }

                views.setTextViewText(R.id.home_side, hometeam);
                views.setTextViewText(R.id.away_side, awayTeam);
                views.setTextViewText(R.id.match_time, gameDetail);

            } else {
                views.setTextViewText(R.id.home_side, this.getResources().getString(R.string.no_games));
            }
            c.close();



            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }
}
