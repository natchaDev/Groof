package th.co.octagoninteractive.groof.model;

public class Bill {

    private String billID;
    private String month;
    private Float expense;
    private Float saving;
    private Float power;


    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


    public Float getExpense() {
        return expense;
    }

    public void setExpense(Float expense) {
        this.expense = expense;
    }


    public Float getSaving() {
        return saving;
    }

    public void setSaving(Float saving) {
        this.saving = saving;
    }


    public Float getPower() {
        return power;
    }

    public void setPower(Float power) {
        this.power = power;
    }
}
