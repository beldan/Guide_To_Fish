package beldan.guidetofish.Weather;

import java.io.Serializable;
import java.util.List;

import beldan.restrung.annotations.JsonProperty;
import beldan.restrung.marshalling.response.AbstractJSONResponse;

/**
 * Created by danielhart on 15/03/15.
 */
public class MarineDataResponse extends AbstractJSONResponse implements Serializable {
    /**
     * Contains the marine weather condition forecast related information.
     */
    @JsonProperty("weather")

    private List<MarineWeather> marineConditionList;
    public List<MarineWeather> getMarineConditionList() {
        return marineConditionList;
    }
    public void setMarineConditionList(List<MarineWeather> marineConditionList) {
        this.marineConditionList = marineConditionList;
    }

}
