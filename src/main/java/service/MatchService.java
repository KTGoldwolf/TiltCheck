package service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.MatchList;

import java.io.IOException;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class MatchService extends BaseService {

    ConfigurationService config;
    CacheService cache;

    public MatchService (ConfigurationService configuration){
        config = configuration;
        cache = new CacheService();
    }

    public MatchList getMatchList(long summonerId) throws RateLimitException, ServiceUnavailableException,
            NoResultsException, IOException{
        MatchList matchList = cache.getCachedMatchHistory(summonerId);
        if (matchList != null){
            return matchList;
        }
        StringBuilder apirequest = new StringBuilder()
                .append("https://na.api.pvp.net/api/lol/na/v2.2/matchlist/by-summoner/")
                .append(summonerId)
                .append(config.getApiKey());
        String response = getResponse(apirequest.toString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        matchList = mapper.readValue(response, MatchList.class);
        cache.cacheMatchHistory(summonerId, matchList);
        return matchList;
    }
}
