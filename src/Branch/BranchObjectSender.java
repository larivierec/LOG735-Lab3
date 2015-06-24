package Branch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by d.budka on 2015-06-23.
 */
public class BranchObjectSender implements Serializable {

    static final Long serialVersionUID = 34523469111111234L;

    Integer idAction=0;
    String[] objects;

    public Integer getIdAction() {
        return idAction;
    }

    public void setIdAction(Integer idAction) {
        this.idAction = idAction;
    }

    public String[] getObjects() {
        return objects;
    }

    public void setObjects(String[] objects) {
        this.objects = objects;
    }
}
