package beldan.guidetofish.Weather;

import java.io.Serializable;
import java.util.List;

import beldan.restrung.annotations.JsonProperty;
import beldan.restrung.marshalling.response.AbstractJSONResponse;

/**
 * Created by danielhart on 15/03/15.
 */
public class HourlyResponse extends AbstractJSONResponse implements Serializable {

    @JsonProperty("time")
    private String time;

    /**
     * Temperature in degree Celsius
     */
    @JsonProperty("tempC")
    private int tempC;

    @JsonProperty("tempF")
    private int tempF;

    /**
     * Wind speed in kilometre per hour
     */
    @JsonProperty("windspeedKmph")
    private int windSpeedKmph;

    @JsonProperty("windspeedMiles")
    private int windSpeedMiles;

    /**
     * Wind direction in degree
     */
    @JsonProperty("winddirDegree")
    private int windDirDegree;

    /**
     * Wind direction in 16-point compass
     */
    @JsonProperty("winddir16Point")
    private String windDir16Point;

    /**
     * Weather condition code
     */
    private int weatherCode;

    /**
     * Weather description text
     */
    @JsonProperty("weatherDesc")
    private List<BasicValueResponse> weatherDesc;

    /**
     * Weather icon url
     */
    private List<BasicValueResponse> weatherIconUrl;

    /**
     * Precipitation in millimetre
     */
    @JsonProperty("precipMM")
    private double precipMm;

    /**
     * Humidity in percentage
     */
    @JsonProperty("humidity")
    private double humidity;

    /**
     * Visibility in kilometre (km)
     */
    private int visibility;

    /**
     * Atmospheric pressure in millibars
     */
    @JsonProperty("pressure")
    private double pressure;

    /**
     * Cloud cover in percentage
     */
    @JsonProperty("cloudcover")
    private double cloudCover;

    @JsonProperty("sigHeight_m")
    private double waveHeight;

    @JsonProperty("swellHeight_m")
    private double swellHeight;

    @JsonProperty("swell_Height_ft")
    private double swellHeightFT;

    @JsonProperty("swellDir")
    private double swellDirectionDegrees;

    @JsonProperty("swellDir16Point")
    private String swellDirection16Point;

    @JsonProperty("waterTemp_C")
    private double waterTemp;

    @JsonProperty("waterTemp_F")
    private double waterTempF;

    public String getTime() {
        return time; }
    public void setTime(String time) {
        this.time = time;
    }

    public int getTempC() {
        return tempC;
    }
    public void setTempC(int tempC) {
        this.tempC = tempC;
    }

    public int getTempF() {
        return tempF;
    }
    public void setTempF(int tempF) {
        this.tempF = tempF;
    }

    public int getWindSpeedKmph() {
        return windSpeedKmph;
    }
    public void setWindSpeedKmph(int windSpeedKmph) {
        this.windSpeedKmph = windSpeedKmph;
    }

    public int getWindSpeedMiles() {
        return windSpeedMiles;
    }
    public void setWindSpeedMiles(int windSpeedMiles) {
        this.windSpeedMiles = windSpeedMiles;
    }

    public int getWindDirDegree() {
        return windDirDegree;
    }
    public void setWindDirDegree(int windDirDegree) {
        this.windDirDegree = windDirDegree;
    }

    public String getWindDir16Point() {
        return windDir16Point;
    }
    public void setWindDir16Point(String windDir16Point) {
        this.windDir16Point = windDir16Point;
    }

    public int getWeatherCode() {
        return weatherCode;
    }
    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public List<BasicValueResponse> getWeatherDesc() {
        return weatherDesc;
    }
    public void setWeatherDesc(List<BasicValueResponse> weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public List<BasicValueResponse> getWeatherIconUrl() {
        return weatherIconUrl;
    }
    public void setWeatherIconUrl(List<BasicValueResponse> weatherIconUrl) {
        this.weatherIconUrl = weatherIconUrl;
    }

    public double getHumidity() {
        return humidity;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getCloudCover() {
        return cloudCover;
    }
    public void setCloudCover(double cloudCover) {
        this.cloudCover = cloudCover;
    }

    public int getVisibility() {
        return visibility;
    }
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public double getPrecipMm() {
        return precipMm;
    }
    public void setPrecipMm(double precipMm) {
        this.precipMm = precipMm;
    }

    public double getWaveHeight() {
        return waveHeight;
    }
    public void setWaveHeight(double waveHeight) {
        this.waveHeight = waveHeight;
    }

    public double getSwellHeight() {
        return swellHeight;
    }
    public void setSwellHeight(double swellHeight) {
        this.swellHeight = swellHeight;
    }

    public double getSwellHeightFT() {
        return swellHeightFT;
    }
    public void setSwellHeightFT(double swellHeightFT) {
        this.swellHeightFT = swellHeightFT;
    }

    public double getSwellDirectionDegrees() {
        return swellDirectionDegrees;
    }
    public void setSwellDirectionDegrees(double swellDirectionDegrees) {
        this.swellDirectionDegrees = swellDirectionDegrees;
    }

    public String getSwellDirection16Point() {
        return swellDirection16Point;
    }
    public void setSwellDirection16Point(String swellDirection16Point) {
        this.swellDirection16Point = swellDirection16Point;
    }

    public double getWaterTemp() {
        return waterTemp;
    }
    public void setWaterTemp(double waterTemp) {
        this.waterTemp = waterTemp;
    }

    public double getWaterTempF() {
        return waterTempF;
    }
    public void setWaterTempF(double waterTempF) {
        this.waterTempF = waterTempF;
    }
}
