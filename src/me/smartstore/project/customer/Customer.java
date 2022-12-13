package me.smartstore.project.customer;

import me.smartstore.project.group.Group;

public class Customer {
    private String serialNO; /* auto-generated */
    private String name = "";
    private String userID = "";
    private Group group;
    private int spentTime;
    private int totalPay;

    private static int AUTO_GENERATOR = 0;

    public Customer() {
        AUTO_GENERATOR++;
        serialNO = String.format("%04d", AUTO_GENERATOR);
    }

    public Customer(String name, String userID, int spentTime, int totalPay) {
        AUTO_GENERATOR++;
        serialNO = String.format("%04d", AUTO_GENERATOR);
        this.name = name;
        this.userID = userID;
        this.spentTime = spentTime;
        this.totalPay = totalPay;
    }

    public String getSerialNO() {
        return serialNO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(int spentTime) {
        this.spentTime = spentTime;
    }

    public int getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (getSpentTime() != customer.getSpentTime()) return false;
        if (getTotalPay() != customer.getTotalPay()) return false;
        if (getSerialNO() != null ? !getSerialNO().equals(customer.getSerialNO()) : customer.getSerialNO() != null)
            return false;
        if (getName() != null ? !getName().equals(customer.getName()) : customer.getName() != null) return false;
        if (getUserID() != null ? !getUserID().equals(customer.getUserID()) : customer.getUserID() != null)
            return false;
        return getGroup() != null ? getGroup().equals(customer.getGroup()) : customer.getGroup() == null;
    }

    @Override
    public int hashCode() {
        int result = getSerialNO() != null ? getSerialNO().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getUserID() != null ? getUserID().hashCode() : 0);
        result = 31 * result + (getGroup() != null ? getGroup().hashCode() : 0);
        result = 31 * result + getSpentTime();
        result = 31 * result + getTotalPay();
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "serialNO='" + serialNO + '\'' +
                ", name='" + name + '\'' +
                ", userID='" + userID + '\'' +
                ", spentTime=" + spentTime +
                ", totalPay=" + totalPay +
                '}';
    }
}
