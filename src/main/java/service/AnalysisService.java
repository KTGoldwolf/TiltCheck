package service;

import dto.Match;
import dto.MatchList;
import dto.Summoner;

import java.io.IOException;

/**
 * Created by KTGoldwolf on 10/2/2016.
 */
public class AnalysisService extends BaseService{

    ConfigurationService config;
    MatchService matchService;
    public AnalysisService(ConfigurationService configuration){
        config = configuration;
        matchService = new MatchService(config);
    }


    //Return a "Status DTO" with tilted boolean and some reasons (reason linked list?)
    public int analyzeGames(Summoner summoner) throws NoResultsException, RateLimitException, ServiceUnavailableException, IOException {
        long summonerId = summoner.getSummonerId();
        MatchList matches = matchService.getMatchList(summonerId);
        int twitchCount = searchForTwitchGames(matches);
        return twitchCount;
    }

    private int searchForTwitchGames(MatchList matches) throws NoResultsException, RateLimitException,
            IOException, ServiceUnavailableException{
        int twitchCount = 0;
        if (matches.getMatchCount() == 0){
            return  twitchCount;
        }
        for (Match match : matches.getMatches()){
            long championId = match.getChampion();
            if (championId == 29){
                twitchCount++;
            }
        }
        return twitchCount;
    }

    private Boolean playedTwitchRecently(){
        return false;
    }

    private Boolean losingStreak(){
        return false;
    }

}
