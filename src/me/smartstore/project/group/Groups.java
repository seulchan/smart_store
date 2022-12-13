package me.smartstore.project.group;

import me.smartstore.project.customer.Customer;

public class Groups { // 싱글톤
    private static Groups allGroups;
    private int size;
    private final int totalGroupNumber = GroupType.values().length;
    private final Group[] groups;

    private Groups() {
        this.groups = new Group[totalGroupNumber];
    }

    public static Groups getInstance() {
        if (allGroups == null) {
            allGroups = new Groups();
            for (GroupType groupType : GroupType.values()) {
                allGroups.add(new Group(groupType, null));
            }
        }
        return allGroups;
    }

    /**
     * GroupType을 입력받아서 groups 배열에서 일치하는 groupType을 가진 group을 반환
     */
    public Group findByGroupType(GroupType groupType) {
        for (int i = 0; i < size; i++) {
            if (groups[i].getGroupType() == groupType) {
                return groups[i];
            }
        }
        return null;
    }

    /**
     * 입력받은 고객에게 맞는 그룹을 반환하는 함수
     */
    public Group findGroupForCustomer(Customer customer) {
        if (customer != null) {
            for (int i = totalGroupNumber - 1; i > 0; i--) {
                Parameter param = groups[i].getParameter();
                if (param != null && customer.getSpentTime() >= param.getMinimumSpentTime()
                        && customer.getTotalPay() >= param.getMinimumTotalPay()) {
                    return groups[i];
                }
            }
        }
        return null;
    }

    /**
     * 그룹을 groups 배열에 추가해주는 함수
     * 이미 존재한다면 그 그룹의 파라미터를 수정
     */
    public void add(Group group) {
        Group findGroup = findByGroupType(group.getGroupType());

        if (findGroup != null) {
            findGroup.setParameter(group.getParameter());
        } else {
            groups[this.size] = group;
            size++;
        }
    }
}
