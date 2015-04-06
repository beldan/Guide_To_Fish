package beldan.guidetofish.Weather;

import java.io.Serializable;
import java.util.List;

import beldan.restrung.annotations.JsonProperty;
import beldan.restrung.marshalling.response.AbstractJSONResponse;

/**
 * Created by danielhart on 15/03/15.
 */
public class MarineWeather extends AbstractJSONResponse implements Serializable {

    /**
     * Time in UTC hhmm tt format. E.g.:- 06:45 AM or 11:34 PM
     */

    @JsonProperty("hourly")
    private List<HourlyResponse> hourly;
    public List<HourlyResponse> getHourly() {
        return hourly;
    }
    public void setHourly(List<HourlyResponse> hourly) {
        this.hourly = hourly;
    }

    @JsonProperty("maxtempC")
    private double maxTemp;
    public double getMaxTemp() {
        return maxTemp;
    }
    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    @JsonProperty("date")
    private String date;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("mintempC")
    private double minTempC;
    public double getMinTempC() {
        return minTempC;
    }
    public void setMinTempC(double minTempC) {
        this.minTempC = minTempC;
    }


}
