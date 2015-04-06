package beldan.guidetofish.Weather;

import java.util.HashMap;

/**
 * Created by danielhart on 14/03/15.
 */
public class WWO {
    static final String API_KEY = "t9x9h3j4s86k5r378ezchsu3";
    static final String NUMBER_OF_DAYS = "5";

    public void getMarineWeather(String lat, String lng, boolean premium) {
        String parameters = String.format("&q=%s%%2C%s", lat, lng);
        String url = "http://api.worldweatheronline.com/free/v1/marine.ashx";
        HashMap<String, String> result = callApi(url, parameters);
        String responseData = result.get("data");

        requestSuccess(responseData);
    }

    private void requestSuccess(String responseData) {

    }

    private HashMap<String, String> callApi(String url, String parameters) {
        String requestURL = String.format("&q=%@&num_of_results=%@&popular=%@", url, API_KEY, parameters);
        return null;
    }
}
