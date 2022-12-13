package me.smartstore.project.group;

import java.util.Objects;

public class Group { // 그룹 1명 (NONE, GENERAL, VIP, VVIP)
    private GroupType groupType;
    private Parameter parameter;

    public Group() {
    }

    public Group(GroupType groupType, Parameter parameter) {
        this.groupType = groupType;
        this.parameter = parameter;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (groupType != group.groupType) return false;
        return Objects.equals(parameter, group.parameter);
    }

    @Override
    public int hashCode() {
        int result = groupType != null ? groupType.hashCode() : 0;
        result = 31 * result + (parameter != null ? parameter.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupType=" + groupType +
                ", parameter=" + parameter +
                '}';
    }
}

