package service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Summoner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class SummonerService {
    ConfigurationService config;

    public SummonerService(ConfigurationService configuration){
        config = configuration;
    }

    /**
     * Return a summoner object is the summoner exists
     * Return null if they do not.
     * @param summonerName
     * @return
     */
    public Summoner findSummoner(String summonerName, String regionId){
        OkHttpClient client = new OkHttpClient();
        StringBuilder apirequest = new StringBuilder();
        apirequest.append("https://na.api.pvp.net/api/lol/")
                .append(regionId)
                .append("/v2.2")
                .append("/summoner/by-name/")
                .append(summonerName)
                .append(config.getApiKey());
        Request request = new Request.Builder().url(apirequest.toString()).build();
        Response response;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        Map<String, Object> mymap;
        Summoner summoner = new Summoner();
        try {
            response = client.newCall(request).execute();
            String responseJSON = response.body().string();
            mymap = mapper.readValue(responseJSON, HashMap.class);
            Map<String, Object> results = (Map) mymap.get(summonerName);
            summoner = mapSummoner(results);
            return summoner;
        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    private Summoner mapSummoner(Map results){
        Summoner newSummoner = new Summoner();
        newSummoner.setAccountId((Integer)results.get("accountId"));
        newSummoner.setSummonerId((Integer)results.get("summonerId"));
        newSummoner.setSummonerName((String)results.get("summonerName"));
        newSummoner.setPlatformId((String)results.get("platformId"));
        newSummoner.setSummonerLevel((Integer)results.get("summonerLevel"));
        return newSummoner;
    }

}
