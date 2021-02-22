package Data;

import java.io.Serializable;

public abstract class Data implements Serializable {
    private static final long serialVersionUID = 1L;
    private int receiverPORT;
    private int senderPORT;

    public Data() {

    }

    public Data(int receiverPORT, int senderPORT) {
        this.receiverPORT = receiverPORT;
        this.senderPORT = senderPORT;
    }

    public int getReceiverPORT() {
        return receiverPORT;
    }

    public void setReceiverPORT(int receiverPORT) {
        this.receiverPORT = receiverPORT;
    }

    public int getSenderPORT() {
        return senderPORT;
    }

    public void setSenderPORT(int senderPORT) {
        this.senderPORT = senderPORT;
    }
}
