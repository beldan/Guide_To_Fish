package beldan.guidetofish;

import com.google.android.gms.maps.model.LatLng;

import io.nlopez.clusterer.Clusterable;

/**
 * Created by danielhart on 9/03/15.
 */


public class Object_Map_Marker implements Clusterable {
    private LatLng locationLatLng;
    private String name;
    private String description;
    private String type;


    public LatLng getLocationLatLng() {
        return locationLatLng;
    }

    public void setLocationLatLng(LatLng locationLatLng) {
        this.locationLatLng = locationLatLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public LatLng getPosition() {
        return getLocationLatLng();
    }
}
