package me.smartstore.project.menu;

import me.smartstore.project.exception.InputEmptyException;
import me.smartstore.project.exception.InputRangeException;
import me.smartstore.project.util.Message;

import static me.smartstore.project.util.MyScanner.scanner;

public class MainMenu extends Menu {
    private static MainMenu mainMenu;

    protected MainMenu() {
    }

    public static MainMenu getInstance() {
        if (mainMenu == null) {
            mainMenu = new MainMenu();
        }
        return mainMenu;
    }

    @Override
    public void process() {}

    /**
     * 유효한 메인 메뉴를 반환하는 함수
     */
    public Integer selectValidMainMenu() {
        while (true) {
            Integer result = getMainMenu();
            if (result != null) return result;
        }
    }

    /**
     * 메인 메뉴가 유효한지 검사한 후 반환해주는 함수
     */
    private Integer getMainMenu() {
        try {
            String menu = selectMainMenu();

            if ("".equals(menu)) {
                throw new InputEmptyException();
            }

            int result = Integer.parseInt(menu);

            if (!checkMenuValidity(menu, 1, 4)) {
                throw new InputRangeException();
            }

            return result;
        } catch (NumberFormatException e) {
            System.out.println(Message.ERR_MSG_INVALID_INPUT_TYPE);
        } catch (InputRangeException e) {
            System.out.println(e.getMessage());
        } catch (InputEmptyException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 메인 메뉴 값을 입력받고 반환하는 함수
     */
    private String selectMainMenu() {
        mainMenuTemplate();
        return scanner.nextLine().trim();
    }

    /**
     * 메인 메뉴를 출력하는 함수
     */
    private void mainMenuTemplate() {
        System.out.print("\n==============================\n" +
                " 1. Classification Parameter\n" +
                " 2. Customer Data\n" +
                " 3. Summary\n" +
                " 4. Quit\n" +
                "==============================\n" +
                "Choose One: ");
    }

}
