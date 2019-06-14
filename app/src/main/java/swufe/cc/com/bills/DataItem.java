package swufe.cc.com.bills;

public class DataItem {
    private String id;
    private String inOrOut;
    private String type;
    private String time;
    private String fee;
    private String remarks;

    public DataItem() {
        super();
        this.inOrOut = "";
        this.type = "";
        this.time = "";
        this.fee = "";
        this.remarks = "";

    }
    public DataItem(String inOrOut, String type, String time, String fee, String remarks) {
        this.inOrOut = inOrOut;
        this.type = type;
        this.time = time;
        this.fee = fee;
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(String inOrOut) {
        this.inOrOut = inOrOut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


}
