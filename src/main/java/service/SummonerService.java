package service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Summoner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class SummonerService extends BaseService{
    ConfigurationService config;
    CacheService cache;

    public SummonerService(ConfigurationService configuration){
        config = configuration;
        cache = new CacheService();
    }

    /**
     * Ensure that a summoner name is of a valid format before accepting a search.
     * @param input
     * @return
     */
    public Boolean validateSummonerName(String input) {
        if ( (!input.matches("^[0-9\\p{L} _\\.]+$")) || (input == null) ){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Return a summoner if they exist.
     * @param summonerName
     * @param regionId
     * @return summoner
     * @throws RateLimitException
     * @throws ServiceUnavailableException
     * @throws NoResultsException
     * @throws IOException
     */
    public Summoner findSummoner(String summonerName, String regionId) throws RateLimitException,
            ServiceUnavailableException, NoResultsException, IOException{
        Summoner summoner = cache.getCachedSummoner(summonerName);
        if (summoner != null){
            return summoner;
        }
        StringBuilder apirequest = new StringBuilder();
        apirequest.append("https://na.api.pvp.net/api/lol/")
                .append(regionId)
                .append("/v1.4")
                .append("/summoner/by-name/")
                .append(summonerName)
                .append(config.getApiKey());
        String response = getResponse(apirequest.toString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        Map<String, Object> mymap;
        mymap = mapper.readValue(response, HashMap.class);
        Map<String, Object> results = (Map) mymap.get(summonerName);
        summoner = mapSummoner(results);
        cache.cacheSummoner(summoner);
        return summoner;
    }

    private Summoner mapSummoner(Map results){
        Summoner newSummoner = new Summoner();
        newSummoner.setSummonerId((Integer)results.get("id"));
        newSummoner.setSummonerName((String)results.get("name"));
        newSummoner.setSummonerLevel((Integer)results.get("summonerLevel"));
        return newSummoner;
    }

}
