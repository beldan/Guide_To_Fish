package beldan.guidetofish;

import java.io.Serializable;

/**
 * Created by danielhart on 24/03/15.
 */
public class Object_Bait implements Serializable {
    public String name;

    //empty constructor
    public Object_Bait() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object_Bait(String name) {
        this.name = name;
    }


}
