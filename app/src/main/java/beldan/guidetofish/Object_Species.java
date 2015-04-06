package beldan.guidetofish;

import java.io.Serializable;

/**
 * Created by danielhart on 24/03/15.
 */
public class Object_Species implements Serializable {
    public String name;

    //empty constructor
    public Object_Species() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object_Species(String name) {
        this.name = name;
    }

}
