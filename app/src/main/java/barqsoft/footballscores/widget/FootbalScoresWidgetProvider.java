package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by jonathan.cook on 11/7/2015.
 */
public class FootbalScoresWidgetProvider extends AppWidgetProvider {

    public final String LOG_TAG = FootbalScoresWidgetProvider.class.getSimpleName();

    private static String[] INFO = new String[] {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL};
    String hometeam;
    String awayTeam;
    String homeGoals;
    String awayGoals;
    String gameDetail;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive");
        if ("android.appwidget.action.APPWIDGET_UPDATE".equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetList);
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // This will get called on the UI thread. We need a service call.
        Intent i = new Intent(context, FootbalScoresWidgetService.class);
        context.startService(i);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
}
