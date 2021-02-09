package Data;

import java.io.Serializable;

public abstract class Data implements Serializable {
    private static final long serialVersionUID = 1L;
    private int targetPORT;

    public Data() {

    }

    public Data(int targetPORT) {
        this.targetPORT = targetPORT;
    }

    public void setPORT(int targetPORT) {
        this.targetPORT = targetPORT;
    }

    public int getTargetPORT() {
        return targetPORT;
    }
}
