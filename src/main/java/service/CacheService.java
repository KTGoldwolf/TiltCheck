package service;

import dto.MatchList;
import dto.Summoner;

import java.util.HashMap;

/**
 * Created by KTGoldwolf on 10/18/2016.
 */
public class CacheService extends BaseService{
    private HashMap<String, Summoner> cachedSummoners;
    private HashMap<Long, MatchList> cachedMatchHistories;

    public CacheService(){
        cachedSummoners = new HashMap<>();
        cachedMatchHistories = new HashMap<>();
    }

    public void cacheSummoner(Summoner summoner){
        if(cachedSummoners.containsKey(summoner.getSummonerName().toLowerCase())){
            cachedSummoners.replace(summoner.getSummonerName().toLowerCase(), summoner);
        } else {
            cachedSummoners.put(summoner.getSummonerName().toLowerCase(), summoner);
        }
    }

    public Summoner getCachedSummoner(String summonerName){
        if(cachedSummoners.containsKey(summonerName.toLowerCase())){
            return cachedSummoners.get(summonerName.toLowerCase());
        } else {
            return null;
        }
    }

    public void cacheMatchHistory(Long summonerId, MatchList matchList){
        if(cachedMatchHistories.containsKey(summonerId)){
            cachedMatchHistories.replace(summonerId, matchList);
        } else {
            cachedMatchHistories.put(summonerId, matchList);
        }
    }

    public MatchList getCachedMatchHistory(Long summonerId){
        if(cachedMatchHistories.containsKey(summonerId)){
            return cachedMatchHistories.get(summonerId);
        } else {
            return null;
        }
    }


}
