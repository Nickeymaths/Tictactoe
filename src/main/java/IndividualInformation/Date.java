package IndividualInformation;

import java.io.Serializable;

public class Date implements Serializable {
    private static final long serialVersionUID = 1L;
    private int dd, mm, yy;

    public Date(int dd, int mm, int yy) {
        this.dd = dd;
        this.mm = mm;
        this.yy = yy;
    }

    public Date(String s) {
        if (!s.isEmpty()) {
            String[] part = s.split("/");
            dd = Integer.parseInt(part[0]);
            dd = Integer.parseInt(part[1]);
            dd = Integer.parseInt(part[2]);
        }
    }

    public int getDay() {
        return dd;
    }

    public int getMonth() {
        return mm;
    }

    public int getYear() {
        return yy;
    }

    public void setDay(int dd) {
        this.dd = dd;
    }

    public void setMonth(int mm) {
        this.mm = mm;
    }

    public void setYear(int yy) {
        this.yy = yy;
    }

    @Override
    public String toString() {
        return "Date: " + dd + "/" + mm + "/" + "yy" + yy;
    }
}
