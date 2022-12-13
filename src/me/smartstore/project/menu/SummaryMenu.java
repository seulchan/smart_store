package me.smartstore.project.menu;

import me.smartstore.project.customer.ClassifiedCustomers;
import me.smartstore.project.customer.ClassifiedCustomersGroup;
import me.smartstore.project.exception.InputEmptyException;
import me.smartstore.project.exception.InputFormatException;
import me.smartstore.project.exception.InputRangeException;
import me.smartstore.project.group.Group;
import me.smartstore.project.group.GroupType;
import me.smartstore.project.group.Groups;
import me.smartstore.project.util.Message;

import static me.smartstore.project.util.MyScanner.scanner;

public class SummaryMenu extends Menu { // 싱글톤
    private static SummaryMenu summaryMenu;

    private final static ClassifiedCustomersGroup classifiedCustomersGroup = ClassifiedCustomersGroup.getInstance();

    private SummaryMenu() {
    }

    public static SummaryMenu getInstance() {
        if (summaryMenu == null) {
            summaryMenu = new SummaryMenu();
        }
        return summaryMenu;
    }


    @Override
    public void process() {
        processSubMenu();
    }

    public void processSubMenu() {
        while (true) {
            int subMenu = getValidSummaryMainMenu();
            switch (subMenu) {
                case 1:
                    showSummary(SortMethod.NONE);
                    break;
                case 2:
                    showSummary(SortMethod.NAME);
                    break;
                case 3:
                    showSummary(SortMethod.TIME);
                    break;
                case 4:
                    showSummary(SortMethod.PAY);
                    break;
                case 5:
                    return;
            }
        }
    }

    /**
     * SortMethod 값을 받아 그 값에 맞게 고객들을 정렬한 다음에 출력하는 함수
     */
    public void showSummary(SortMethod sortMethod) {

        if (!sortMethod.equals(SortMethod.NONE)) {
            String input = selectSortOrder();
            if (input.equals("END")) {
                return;
            }
            OrderType sortOrder = OrderType.valueOf(input);
            classifiedCustomersGroup.classifyCustomers(sortOrder, sortMethod);
        } else {
            classifiedCustomersGroup.classifyCustomers();
        }
        printSummary();
    }

    /**
     * 각 그룹별로 고객을 출력해주는 함수
     */
    private void printSummary() {
        for (ClassifiedCustomers customers : classifiedCustomersGroup.getClassifiedCustomers()) {
            System.out.println("\n==============================");
            if (customers.getGroupType().equals(GroupType.NONE)) {
                System.out.println("Others : " + customers.getSize() + " customer(s)");
            } else {
                System.out.println(customers.getGroupType() + " Group : " + customers.getSize() + " customer(s)");
                Group group = Groups.getInstance().findByGroupType(customers.getGroupType());
                if (group.getParameter() == null) {
                    System.out.println("[Parameter] " + group.getParameter());
                } else {
                    System.out.println("[Parameter] " + group.getParameter());
                }
            }

            System.out.println("------------------------------");
            if (!customers.isEmpty()) {
                for (int i = 0; i < customers.getSize(); i++) {
                    System.out.println("No. " + (i + 1) + " => " + customers.get(i));
                }
            } else {
                System.out.println("No customer.");
            }
        }
    }

    /**
     * Summary 메인 메뉴의 값이 유효한지 확인하고 반환하는 함수
     */
    private Integer getValidSummaryMainMenu() {
        while (true) {
            try {
                String summaryMainMenu = "\n==============================\n" +
                        " 1. Summary\n" +
                        " 2. Summary (Sorted By Name)\n" +
                        " 3. Summary (Sorted By Spent Time)\n" +
                        " 4. Summary (Sorted By Total Payment)\n" +
                        " 5. Back\n" +
                        "==============================\n" +
                        "Choose One: ";
                String menu = showAndSelectMenu(summaryMainMenu);

                if ("".equals(menu)) {
                    throw new InputEmptyException();
                }

                int result = Integer.parseInt(menu);

                if (!checkMenuValidity(menu, 1, 5)) {
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
        }
    }

    /**
     * 유효한 sortOrder를 반환해주는 함수
     */
    public String selectSortOrder() {
        while (true) {
            String sortOrder = getSortOrder();
            if (sortOrder != null) return sortOrder;
        }
    }

    /**
     * selectSortOrder 함수 안에서 sortOrder를 입력받고 유효한지 체크한 후
     * 반환하는 함수
     */
    private String getSortOrder() {
        try {
            System.out.println("\n** Press 'end', if you want to exit! **");
            System.out.print("Which order (ASCENDING, DESCENDING)? ");
            String sortOrder = scanner.nextLine().trim().toUpperCase();

            if ("END".equals(sortOrder)) {
                return sortOrder;
            }

            if ("".equals(sortOrder)) {
                throw new InputEmptyException();
            }

            if (!sortOrder.matches("^[a-zA-Z]*$")) {
                throw new InputFormatException();
            }

            if (!validSortOrderType(sortOrder)) {
                throw new InputRangeException();
            }

            return sortOrder;

        } catch (NullPointerException e) {
            System.out.println(Message.ERR_MSG_INVALID_INPUT_NULL);
        } catch (InputEmptyException e) {
            System.out.println(e.getMessage());
        } catch (InputRangeException e) {
            System.out.println(e.getMessage());
        } catch (InputFormatException e) {
            System.out.println(Message.ERR_MSG_INVALID_INPUT_TYPE);
        }
        return null;
    }

    /**
     * 입력 받은 sortOrder가 유효한 값인지 확인하는 함수
     */
    private Boolean validSortOrderType(String sortOrder) {

        for (OrderType orderType : OrderType.values()) {
            if (sortOrder.equals(orderType.toString())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 메뉴를 보여주고 메뉴를 입력받아서 리턴하는 함수
     */
    private String showAndSelectMenu(String menu) {

        System.out.print(menu);
        return scanner.nextLine().trim();
    }

}
