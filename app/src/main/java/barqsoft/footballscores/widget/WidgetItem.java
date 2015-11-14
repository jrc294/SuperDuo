package barqsoft.footballscores.widget;

/**
 * Created by jonathan.cook on 11/9/2015.
 */
public class WidgetItem {

    private String homeSide;
    private String awaySide;
    private String homeScore;
    private String awayScore;
    private String gameDetail;

    public WidgetItem(String homeSide, String awaySide, String homeScore, String awayScore, String gameDetail) {
        this.homeSide = homeSide;
        this.awaySide = awaySide;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.gameDetail = gameDetail;
    }

    public String getAwayScore() {
        return awayScore;
    }

    public String getAwaySide() {
        return awaySide;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public String getHomeSide() {
        return homeSide;
    }

    public String getGameDetail() {
        return gameDetail;
    }
}
