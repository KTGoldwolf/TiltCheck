package service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class StaticDataService {

    private Map<Integer, String> champLookup;
    Boolean dataLoaded;
    String lastRefreshVersion;

    public StaticDataService(){
        if (!dataLoaded){
            loadChampionData();
        }
    }

    public String lookupChampionId(int id){
        if (!dataLoaded) {
            loadChampionData();
        }
        return champLookup.get(id);
    }

    private void loadChampionData(){
        ObjectMapper mapper = new ObjectMapper();
        try{
            Map<String, Object> map = mapper.readValue(new URL("http://ddragon.leagueoflegends.com/cdn/6.20.1/data/en_US/champion.json"), HashMap.class);
            Map<String, Object> rawChampData = (Map) map.get("data");
            champLookup = new HashMap<>();
            Map<String, Object> entry;
            for (Map.Entry<String, Object> champ : rawChampData.entrySet()){
                entry = (Map) champ.getValue();
                Integer id = Integer.parseInt(entry.get("key").toString());
                String name = (String) entry.get("name");
                champLookup.put(id, name);
            }
            dataLoaded = true;
            lastRefreshVersion = map.get("version").toString();
        } catch (IOException e) {
            dataLoaded = false;
            System.out.println("Could not load chanpion data from data dragon.");
        }
    }


}
