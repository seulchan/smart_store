package me.smartstore.project.customer;

import me.smartstore.project.group.Group;
import me.smartstore.project.group.GroupType;
import me.smartstore.project.menu.OrderType;
import me.smartstore.project.menu.SortMethod;

import java.util.Arrays;

public class ClassifiedCustomers extends Customers { // ClassifiedCustomers is a Customers
    private final GroupType groupType;

    public ClassifiedCustomers(GroupType groupType) {
        this.groupType = groupType;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    /**
     * sortMethod가 none일 때 불러와 그룹에 맞는 고객들을 배열에 저장해서 반환
     */
    public void findCustomers() {
        Customers allCustomer = Customers.getInstance();

        int groupSize = getGroupSize();
        this.size = groupSize;
        if (this.capacity < this.size) {
            this.capacity = this.size;
        }

        Customer[] savedCustomers = new Customer[groupSize];

        int index = 0;
        for (int i = 0; i < allCustomer.getSize(); ++i) {
            Customer customer = allCustomer.get(i);
            Group customerGroup = allCustomer.get(i).getGroup();
            if (customerGroup == null && this.groupType.equals(GroupType.NONE)) {
                savedCustomers[index] = customer;
                index++;
            } else if (customerGroup != null && customerGroup.getGroupType().equals(this.groupType)) {
                savedCustomers[index] = customer;
                index++;
            }
        }

        this.customers = savedCustomers;
    }

    /**
     * sortMethod가 none이 아닐 때 불러와 그룹에 맞는 고객들을 배열에 저장하고 sortOrder와 sortMethod에 맞게 정렬해서 반환
     */
    public void findCustomers(OrderType sortOrder, SortMethod sortMethod) {
        Customers allCustomer = Customers.getInstance();
        int groupSize = getGroupSize();
        this.size = groupSize;
        if (this.capacity < this.size) {
            this.capacity = this.size;
        }

        Customer[] savedCustomers = new Customer[groupSize];

        int index = 0;
        for (int i = 0; i < allCustomer.getSize(); ++i) {
            Customer customer = allCustomer.get(i);
            Group customerGroup = allCustomer.get(i).getGroup();
            if (customerGroup == null && this.groupType.equals(GroupType.NONE)) {
                savedCustomers[index] = customer;
                index++;
            } else if (customerGroup != null && customerGroup.getGroupType().equals(this.groupType)) {
                savedCustomers[index] = customer;
                index++;
            }
        }

        if (sortMethod.equals(SortMethod.NAME)) {
            sortByName(sortOrder, savedCustomers);
        } else if (sortMethod.equals(SortMethod.TIME)) {
            sortByTime(sortOrder, savedCustomers);
        } else if (sortMethod.equals(SortMethod.PAY)) {
            sortByPay(sortOrder, savedCustomers);
        }

        this.customers = savedCustomers;
    }

    /**
     * 배열에 저장된 고객을 sortOrder에 맞게 이름을 기준으로 정렬
     * 이름이 같다면 ID를 이용해서 정렬
     */
    private void sortByName(OrderType sortOrder, Customer[] savedCustomers) {
        if (sortOrder == OrderType.ASCENDING) {
            Arrays.sort(savedCustomers, (o1, o2) -> {
                int result = o1.getName().compareTo(o2.getName());
                if (result == 0) {
                    return o1.getUserID() != null && o2.getUserID() != null ? o1.getUserID().compareTo(o2.getUserID()) : 0;
                } else {
                    return result;
                }
            });
        } else {
            Arrays.sort(savedCustomers, (o1, o2) -> {
                int result = o1.getName().compareTo(o2.getName()) * -1;
                if (result == 0) {
                    return o1.getUserID() != null && o2.getUserID() != null ? o1.getUserID().compareTo(o2.getUserID()) : 0;
                } else {
                    return result;
                }
            });
        }
    }

    /**
     * 배열에 저장된 고객을 sortOrder에 맞게 총 결제액을 기준으로 정렬
     * 결제액이 같다면 이름을 이용해서 정렬
     */
    private void sortByPay(OrderType sortOrder, Customer[] savedCustomers) {
        if (sortOrder == OrderType.ASCENDING) {
            Arrays.sort(savedCustomers, (o1, o2) -> {
                if (o1.getTotalPay() < o2.getTotalPay()) {
                    return -1;
                } else if (o1.getTotalPay() == o2.getTotalPay()) {
                    return o1.getName() != null && o2.getName() != null ? o1.getName().compareTo(o2.getName()) : 0;
                } else {
                    return 1;
                }
            });
        } else {
            Arrays.sort(savedCustomers, (o1, o2) -> {
                if (o1.getTotalPay() < o2.getTotalPay()) {
                    return 1;
                } else if (o1.getTotalPay() == o2.getTotalPay()) {
                    return o1.getName() != null && o2.getName() != null ? o1.getName().compareTo(o2.getName()) : 0;
                } else {
                    return -1;
                }
            });
        }
    }

    /**
     * 배열에 저장된 고객을 sortOrder에 맞게 총 시간 이용량을 기준으로 정렬
     * 총 시간 이용량이 같다면 이름을 이용해서 정렬
     */
    private void sortByTime(OrderType sortOrder, Customer[] savedCustomers) {
        if (sortOrder == OrderType.ASCENDING) {
            Arrays.sort(savedCustomers, (o1, o2) -> {
                if (o1.getSpentTime() < o2.getSpentTime()) {
                    return -1;
                } else if (o1.getSpentTime() == o2.getSpentTime()) {
                    return o1.getName() != null && o2.getName() != null ? o1.getName().compareTo(o2.getName()) : 0;
                } else {
                    return 1;
                }
            });
        } else {
            Arrays.sort(savedCustomers, (o1, o2) -> {
                if (o1.getSpentTime() < o2.getSpentTime()) {
                    return 1;
                } else if (o1.getSpentTime() == o2.getSpentTime()) {
                    return o1.getName() != null && o2.getName() != null ? o1.getName().compareTo(o2.getName()) : 0;
                } else {
                    return -1;
                }
            });
        }
    }

    /**
     * 그룹 사이즈를 반환해주는 함수
     */
    public int getGroupSize() {
        Customers allCustomer = Customers.getInstance();
        int count = 0;
        for (int i = 0; i < allCustomer.getSize(); ++i) {
            Group customerGroup = allCustomer.get(i).getGroup();
            if (customerGroup == null && this.groupType.equals(GroupType.NONE)) {
                count++;
            } else if (customerGroup != null && customerGroup.getGroupType().equals(this.groupType)) {
                count++;
            }
        }
        return count;
    }
}
