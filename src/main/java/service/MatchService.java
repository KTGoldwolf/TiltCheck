package service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.MatchList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class MatchService {

    ConfigurationService config;

    public MatchService (ConfigurationService configuration){
        config = configuration;
    }

    public MatchList getMatchList(long summonerId){
        MatchList matchList = new MatchList();

        StringBuilder apirequest = new StringBuilder()
                .append("https://na.api.pvp.net/api/lol/na/v2.2/matchlist/by-summoner/")
                .append(summonerId)
                .append(config.getApiKey());

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(apirequest.toString()).build();
        Response response;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        Map<String, Object> mymap;
        try {
            response = client.newCall(request).execute();
            String responseJSON = response.body().string();
            matchList = mapper.readValue(responseJSON, MatchList.class);
        } catch (Exception e){
            System.out.println(e);
        }
        return matchList;
    }
}
