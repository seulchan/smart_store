package me.smartstore.project.customer;

import me.smartstore.project.group.GroupType;
import me.smartstore.project.menu.OrderType;
import me.smartstore.project.menu.SortMethod;

public class ClassifiedCustomersGroup {
    private static ClassifiedCustomersGroup classifiedCustomersGroup;
    private final ClassifiedCustomers[] classifiedCustomers;

    private ClassifiedCustomersGroup() {
        int totalGroupNumber = GroupType.values().length;
        classifiedCustomers = new ClassifiedCustomers[totalGroupNumber];
        initialize();
    }

    public static ClassifiedCustomersGroup getInstance() {
        if (classifiedCustomersGroup == null) {
            classifiedCustomersGroup = new ClassifiedCustomersGroup();
        }
        return classifiedCustomersGroup;
    }

    public ClassifiedCustomers[] getClassifiedCustomers() {
        return classifiedCustomers;
    }

    /**
     * ClassifiedCustomersGroup이 생성될 때, classifiedCustomers 배열에 각 그룹별 ClassifiedCustomers 객체를 넣어주는 함수
     */
    private void initialize() {
        int count = 0;
        for (GroupType groupType : GroupType.values()) {
            classifiedCustomers[count] = new ClassifiedCustomers(groupType);
            count++;
        }
    }

    /**
     * sortMethod가 none일 때 불러와 고객들을 분류하는 함수
     */
    public void classifyCustomers() {
        for (ClassifiedCustomers classifiedCustomer : classifiedCustomers) {
            classifiedCustomer.findCustomers();
        }
    }

    /**
     * sortMethod가 none이 아닐 때 불러와 고객들을 sortOrder와 sortMethod에 맞게 분류하는 함수
     */
    public void classifyCustomers(OrderType sortOrder, SortMethod sortMethod) {
        for (ClassifiedCustomers classifiedCustomer : classifiedCustomers) {
            classifiedCustomer.findCustomers(sortOrder, sortMethod);
        }
    }

}
