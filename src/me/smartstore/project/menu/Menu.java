package me.smartstore.project.menu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    public abstract void process();

    /**
     * 입력받은 메뉴의 범위가 유효한지 체크하는 함수
     */
    Boolean checkMenuValidity(String str, int from, int to) {

        String regex = "^(["+from+"-"+to+"])";
        Pattern p = Pattern.compile(regex);

        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
