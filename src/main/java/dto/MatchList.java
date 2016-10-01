package dto;

import java.util.List;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class MatchList {
    List<Match> matches;
    int totalGames;
    int startIndex;
    int endIndex;

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getMatchCount(){
        return matches.size();
    }
}
