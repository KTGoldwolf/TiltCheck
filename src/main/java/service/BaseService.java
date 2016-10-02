package service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KTGoldwolf on 10/1/2016.
 */
public class BaseService {

    /**
     * Execute given String as API call
     * @param call fully formed URL for API call
     * @return
     * @throws RateLimitException
     * @throws ServiceUnavailableException
     * @throws IOException
     * @throws NoResultsException
     */
    public String getResponse(String call) throws RateLimitException, ServiceUnavailableException, IOException, NoResultsException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(call).build();
        Response response;
        response = client.newCall(request).execute();
        if (!response.isSuccessful()){
            response.body().close();
            switch (response.code()) {
                case 404:
                    throw new NoResultsException();
                case 429:
                    throw new RateLimitException();
                case 503:
                    throw new ServiceUnavailableException();
                default:
                    throw new NoResultsException();
            }
        }
        String responseJSON = response.body().string();
        response.body().close();
        return responseJSON;
    }


}
