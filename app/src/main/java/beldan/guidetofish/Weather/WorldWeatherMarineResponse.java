package beldan.guidetofish.Weather;

import java.io.Serializable;

import beldan.restrung.marshalling.response.AbstractJSONResponse;

/**
 * Created by danielhart on 15/03/15.
 */
public class WorldWeatherMarineResponse extends AbstractJSONResponse implements Serializable {
    private MarineDataResponse data;

    public MarineDataResponse getData() {
        return data;
    }

    public void setData(MarineDataResponse data) {
        this.data = data;
    }
}
