package me.smartstore.project.menu;

public enum OrderType {
    ASCENDING("오름차순"), DESCENDING("내림차순"), NONE("없음");

    String sortType;

    OrderType(String sortType) {
        this.sortType = sortType;
    }
}
