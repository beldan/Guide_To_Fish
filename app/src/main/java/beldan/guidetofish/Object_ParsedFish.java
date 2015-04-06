package beldan.guidetofish;

import java.io.Serializable;

/**
 * Created by danielhart on 7/03/15.
 */
public class Object_ParsedFish implements Serializable {
    public String water;
    public String type;
    public String weight;
    public String length;
    public String date;
    public String latitude;
    public String longitude;
    public String image;
    public String uploaded;
    public String waterTemp;
    public String pressure;
    public String pressureTrend;
    public String waveHeight;
    public String swellHeight;
    public String moonPhase;
    public String measurement;
    public String swellDirection;
    public String objectID;
    public String userName;
    public String humidity;
    public byte[] photo;
    public String bait;

    //empty constructor
    public Object_ParsedFish() {

    }

    public Object_ParsedFish(String water, String type, String weight, String length, String date, String latitude, String longitude, String image, String waterTemp, String pressure, String pressureTrend, String waveHeight, String swellHeight, String moonPhase, String measurement, String swellDirection, String objectID, String userName, String humidity, byte[] photo, String bait) {
        this.water = water;
        this.type = type;
        this.weight = weight;
        this.length = length;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.waterTemp = waterTemp;
        this.pressure = pressure;
        this.pressureTrend = pressureTrend;
        this.waveHeight = waveHeight;
        this.swellHeight = swellHeight;
        this.moonPhase = moonPhase;
        this.measurement = measurement;
        this.swellDirection = swellDirection;
        this.objectID = objectID;
        this.userName = userName;
        this.humidity = humidity;
        this.photo = photo;
        this.bait = bait;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public void setMoonPhase(String moonPhase) {
        this.moonPhase = moonPhase;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setPressureTrend(String pressureTrend) {
        this.pressureTrend = pressureTrend;
    }

    public void setSwellDirection(String swellDirection) {
        this.swellDirection = swellDirection;
    }

    public void setSwellHeight(String swellHeight) {
        this.swellHeight = swellHeight;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public void setWaterTemp(String waterTemp) {
        this.waterTemp = waterTemp;
    }

    public void setWaveHeight(String waveHeight) {
        this.waveHeight = waveHeight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setBait(String bait) {
        this.bait = bait;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public String getDate() {
        return date;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getImage() {
        return image;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLength() {
        return length;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getMoonPhase() {
        return moonPhase;
    }

    public String getObjectID() {
        return objectID;
    }

    public String getPressure() {
        return pressure;
    }

    public String getPressureTrend() {
        return pressureTrend;
    }

    public String getSwellDirection() {
        return swellDirection;
    }

    public String getSwellHeight() {
        return swellHeight;
    }

    public String getType() {
        return type;
    }

    public String getUploaded() {
        return uploaded;
    }

    public String getUserName() {
        return userName;
    }

    public String getWater() {
        return water;
    }

    public String getWaterTemp() {
        return waterTemp;
    }

    public String getWaveHeight() {
        return waveHeight;
    }

    public String getWeight() {
        return weight;
    }

    public String getBait() {
        return bait;
    }
}
