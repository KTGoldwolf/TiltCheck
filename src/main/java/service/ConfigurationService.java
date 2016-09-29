package service;

/**
 * Created by KTGoldwolf on 9/28/2016.
 */
public class ConfigurationService {

    private String API_KEY;

    public ConfigurationService(String key){
        API_KEY = key;
    }

    public String getApiKey(){
        return API_KEY;
    }
}
