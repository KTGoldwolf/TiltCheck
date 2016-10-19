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
    static StaticDataService staticDataService;
    static AnalysisService analysisService;

    public static void main(String[] args) {

        staticFileLocation("/public");

        String key = getHerokuSecret();

        //If running  locally and not via heroku, provide API key via args
        if (key == null) {
            key = args[0];
        }

        config = new ConfigurationService(key);
        summonerService = new SummonerService(config);
        matchService = new MatchService(config);
        staticDataService = new StaticDataService();
        analysisService = new AnalysisService(config);

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
            String rawName = cleanSearchInput(params);
            String searchName = rawName.trim().toLowerCase();
            try {
                if ( !summonerService.validateSummonerName(searchName) ){
                    setSearchError(data, rawName, "Please enter a valid summoner name.");
                    return new ModelAndView(data, "tilt.mustache");
                }
                    Summoner summoner = summonerService.findSummoner(searchName, "NA");
                    data.put("summonerName", searchName);
                    int check = analysisService.analyzeGames(summoner);
                    if (check >= 1){
                        data.put("tilted", false);
                        data.put("games", check);
                    } else {
                        data.put("tilted", true);
                        data.put("error", false);
                    }
            } catch (RateLimitException e) {
                setSearchError(data, rawName, "Rate limit has been temporarily exceeded. Please wait a bit and then try again.");
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

    private static void setSearchError(Map data, String searchName, String errorText){
        data.put("error", true);
        data.put("summonerName", searchName);
        data.put("errorText", errorText);
    }

    private static String cleanSearchInput(MultiMap<String> params){
        return params.get("requestedSummoner").toString().replace('[', ' ').replace(']', ' ');
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
            System.out.println("Reading secret from Heroku");
            return System.getenv().get("SECRET");
        }
        System.out.println("No Heroku secret found, running in local-only mode.");
        return null; //if running locally, don't get a Heroku secret
    }
}