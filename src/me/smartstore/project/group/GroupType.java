package me.smartstore.project.group;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum GroupType {
	NONE("해당없음"), GENERAL("일반고객"), VIP("우수고객"), VVIP("최우수고객");
	String groupType = "";

	GroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
}
