import dto.Match;
import dto.MatchList;
import dto.Summoner;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import service.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    static ConfigurationService config;
    static SummonerService summonerService;
    static MatchService matchService;

    /**
     *
     * @param args API Key
     */
    public static void main(String[] args) {
        staticFileLocation("/public");

        String key = getHerokuSecret();

        config = new ConfigurationService(key);
        summonerService = new SummonerService(config);
        matchService = new MatchService(config);

        Map map = new HashMap();
        int twitchCount = 0;
        map.put("tilted", true);
        map.put("twitchGames", twitchCount);

        port(getHerokuAssignedPort());

        get("/", (req, res) -> {
            return new ModelAndView(map, "tilt.mustache");
        }, new MustacheTemplateEngine());

        get("/hello", (req, res) -> "Hello World");

        post("/view", (req, res) -> {
            Map<String, Object> data = new HashMap<>();
            MultiMap<String> params = new MultiMap<>();
            UrlEncoded.decodeTo(req.body(), params, "UTF-8");
            String rawName = params.get("requestedSummoner").toString().replace('[', ' ').replace(']', ' ');
            String searchName = rawName.trim().toLowerCase();
            try {
                if ( !validateSummonerName(searchName)){
                    setSearchError(data, rawName, "Please enter a valid summoner name.");
                    return new ModelAndView(data, "tilt.mustache");
                }
                    Summoner summoner = summonerService.findSummoner(searchName, "NA");
                    data.put("summonerName", searchName);
                    int check = searchForTwitchGames(summoner);
                    if (check >= 1){
                        data.put("tilted", false);
                        data.put("games", check);
                    } else {
                        data.put("tilted", true);
                        data.put("error", false);
                    }
            } catch (RateLimitException e) {
                setSearchError(data, rawName, "Rate limit has been temporariily exceeded. Please wait a bit and then try again.");
                return new ModelAndView(data, "tilt.mustache");
            } catch (NoResultsException e) {
                setSearchError(data, rawName, "No summoner was found with name" + rawName);
                return new ModelAndView(data, "tilt.mustache");
            } catch (ServiceUnavailableException e) {
                setSearchError(data, rawName, "Unable to complete the search. Perhaps League is down for maintenance?");
                return new ModelAndView(data, "tilt.mustache");
            } catch (Exception e) {
                setSearchError(data, rawName, "Some unknown error happened. ...Sorry.");
                System.out.print("Unknown error " +e);
                return new ModelAndView(data, "tilt.mustache");
            }
            return new ModelAndView(data, "view.mustache");
        }, new MustacheTemplateEngine());

    }

    private static Boolean validateSummonerName(String input) {
        if ( (!input.matches("^[0-9\\p{L} _\\.]+$")) || (input == null) ){
            return false;
        } else {
            return true;
        }
    }

    private static void setSearchError(Map data, String searchName, String errorText){
        data.put("error", true);
        data.put("summonerName", searchName);
        data.put("errorText", errorText);
    }

    private static int searchForTwitchGames(Summoner summoner) throws NoResultsException, RateLimitException,
            IOException, ServiceUnavailableException{
        int twitchCount = 0;
        MatchList matches = matchService.getMatchList(summoner.getSummonerId());
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

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    static String getHerokuSecret() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return System.getenv().get("SECRET");
        }
        return null; //ir running locally, don't get a Heroku secret
    }
}