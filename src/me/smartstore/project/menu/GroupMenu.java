package me.smartstore.project.menu;

import me.smartstore.project.customer.Customers;
import me.smartstore.project.exception.InputEmptyException;
import me.smartstore.project.exception.InputRangeException;
import me.smartstore.project.group.Group;
import me.smartstore.project.group.GroupType;
import me.smartstore.project.group.Groups;
import me.smartstore.project.group.Parameter;
import me.smartstore.project.util.Message;

import static me.smartstore.project.util.MyScanner.scanner;

public class GroupMenu extends Menu { // 싱글톤
    private static GroupMenu groupMenu;
    private final Groups allGroups = Groups.getInstance();

    private GroupMenu() {
    }

    public static GroupMenu getInstance() {
        if (groupMenu == null) {
            groupMenu = new GroupMenu();
        }
        return groupMenu;
    }

    @Override
    public void process() {
        processSubMenu();
    }

    private void processSubMenu() {
        while (true) {
            int subMenu = getValidGroupMainMenu();
            switch (subMenu) {
                case 1:
                    setParameter();
                    break;
                case 2:
                    viewParameter();
                    break;
                case 3:
                    updateParameter();
                    break;
                case 4:
                    return;
            }
        }
    }

    /**
     * 그룹의 파라미터를 수정하는 함수
     */
    private void updateParameter() {
        while (true) {
            String group = selectGroup();

            if ("END".equals(group)) {
                return;
            }

            GroupType groupType = GroupType.valueOf(group);
            Group findGroup = allGroups.findByGroupType(groupType);

            if (findGroup.getParameter() == null) {
                System.out.println("\nNo parameter. Set the parameter first.");
            } else {
                System.out.println("\nGroupType: " + findGroup.getGroupType().toString());
                System.out.println("Parameter: " + findGroup.getParameter().toString());

                Parameter param = findGroup.getParameter();
                while (true) {
                    boolean done = setGroupParameters(groupType, allGroups, param);
                    System.out.println("\nGroupType: " + findGroup.getGroupType().toString());
                    System.out.println("Parameter: " + findGroup.getParameter().toString());

                    if (done) {
                        break;
                    }
                }

            }

        }
    }

    /**
     * 그룹의 파라미터를 출력하는 함수
     */
    private void viewParameter() {
        while (true) {
            String group = selectGroup();

            if ("END".equals(group)) {
                return;
            }

            GroupType groupType = GroupType.valueOf(group);
            Groups allGroups = Groups.getInstance();
            Group findGroup = allGroups.findByGroupType(groupType);

            System.out.println("\nGroupType: " + group);
            try {
                System.out.println("Parameter: " + findGroup.getParameter());
            } catch (NullPointerException e) {
                System.out.println("Parameter: null");
            }
        }
    }

    /**
     * 그룹의 파라미터를 수정하는 함수
     */
    private void setParameter() {
        while (true) {

            String group = selectGroup();

            if ("END".equals(group)) {
                return;
            }

            GroupType groupType = GroupType.valueOf(group);
            Groups allGroups = Groups.getInstance();

            Group findGroup = allGroups.findByGroupType(groupType);


            if (findGroup.getParameter() != null) {
                System.out.println("\n" + group + " group already exists.");
                System.out.println("\n" + findGroup);
            } else {
                Parameter param = new Parameter();
                setGroupParameters(groupType, allGroups, param);
            }


        }
    }

    private boolean setGroupParameters(GroupType groupType, Groups allGroups, Parameter param) {
        while (true) {
            int setParameterMenu = getValidSetParameterMenu();
            switch (setParameterMenu) {
                case 1:
                    setMinSpentTime(param, groupType);
                    break;
                case 2:
                    setMinTotalPay(param, groupType);
                    break;
                case 3:
                    allGroups.add(new Group(groupType, param));
                    Customers.getInstance().reassignGroup();
                    return true;
            }
        }
    }

    /**
     * 그룹의 최소 총 결제액을 정하는 함수
     */
    private void setMinTotalPay(Parameter param, GroupType groupType) {
        while(true) {
            try {
                System.out.print("\nInput Minimum Total Pay: ");

                String input = scanner.nextLine();

                if ("".equals(input)) {
                    throw new InputEmptyException();
                }

                int minTotalPay = Integer.parseInt(input);

                if (minTotalPay < 0) {
                    throw new InputRangeException();
                }

                checkValidPayBetweenGroups(groupType, minTotalPay);

                param.setMinimumTotalPay(minTotalPay);
                return;

            } catch (InputEmptyException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_TYPE);
            } catch (InputRangeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 그룹 간의 최소 총 결제액이 유효한지 검사하는 함수
     */
    private void checkValidPayBetweenGroups(GroupType groupType, int minTotalPay) throws InputRangeException {
        if (GroupType.GENERAL.equals(groupType)) {
            Group vip = allGroups.findByGroupType(GroupType.VIP);
            Group vvip = allGroups.findByGroupType(GroupType.VVIP);

            if (vip.getParameter() != null) {
                if (minTotalPay > vip.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be below VIP and VVIP parameter.");
                }
            }

            if (vvip.getParameter() != null) {
                if (minTotalPay > vvip.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be below VIP and VVIP parameter.");
                }
            }
        }

        vipCompare(groupType, minTotalPay);

        if (GroupType.VVIP.equals(groupType)) {
            Group general = allGroups.findByGroupType(GroupType.GENERAL);
            Group vip = allGroups.findByGroupType(GroupType.VIP);

            if (general.getParameter() != null) {
                if (minTotalPay < general.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be greater than GENERAL and VIP parameters.");
                }
            }

            if (vip.getParameter() != null) {
                if (minTotalPay < vip.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be greater than GENERAL and VIP parameters.");
                }
            }
        }
    }

    private void vipCompare(GroupType groupType, int minTotalPay) throws InputRangeException {
        if (GroupType.VIP.equals(groupType)) {
            Group general = allGroups.findByGroupType(GroupType.GENERAL);
            Group vvip = allGroups.findByGroupType(GroupType.VVIP);

            if (general.getParameter() != null) {
                if (minTotalPay < general.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be between GENERAL and VVIP parameters.");
                }
            }

            if (vvip.getParameter() != null) {
                if (minTotalPay > vvip.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be between GENERAL and VVIP parameters.");
                }
            }
        }
    }

    /**
     *  그룹의 최소 총 시간 사용량을 정하는 함수
     */
    private void setMinSpentTime(Parameter param, GroupType groupType) {
        while(true) {
            try {
                System.out.print("\nInput Minimum Spent Time: ");

                String input = scanner.nextLine();

                if ("".equals(input)) {
                    throw new InputEmptyException();
                }

                int minSpentTime = Integer.parseInt(input);

                if (minSpentTime < 0) {
                    throw new InputRangeException();
                }

                checkValidTimeBetweenGroups(groupType, minSpentTime);

                param.setMinimumSpentTime(minSpentTime);
                return;
            } catch (InputEmptyException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_TYPE);
            } catch (InputRangeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 그룹 간의 최소 총 시간 사용량이 유효한지 검사하는 함수
     */
    private void checkValidTimeBetweenGroups(GroupType groupType, int minSpentTime) throws InputRangeException {
        if (GroupType.GENERAL.equals(groupType)) {
            Group vip = allGroups.findByGroupType(GroupType.VIP);
            Group vvip = allGroups.findByGroupType(GroupType.VVIP);

            if (vip.getParameter() != null) {
                if (minSpentTime > vip.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be less than VIP and VVIP parameter.");
                }
            }

            if (vvip.getParameter() != null) {
                if (minSpentTime > vvip.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be less than VIP and VVIP parameter.");
                }
            }
        }

        if (GroupType.VIP.equals(groupType)) {
            Group general = allGroups.findByGroupType(GroupType.GENERAL);
            Group vvip = allGroups.findByGroupType(GroupType.VVIP);

            if (general.getParameter() != null) {
                if (minSpentTime < general.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be between GENERAL and VVIP parameters.");
                }
            }

            if (vvip.getParameter() != null) {
                if (minSpentTime > vvip.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be between GENERAL and VVIP parameters.");
                }
            }
        }

        if (GroupType.VVIP.equals(groupType)) {
            Group general = allGroups.findByGroupType(GroupType.GENERAL);
            Group vip = allGroups.findByGroupType(GroupType.VIP);

            if (general.getParameter() != null) {
                if (minSpentTime < general.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be greater than GENERAL and VIP parameters.");
                }
            }

            if (vip.getParameter() != null) {
                if (minSpentTime < vip.getParameter().getMinimumSpentTime()) {
                    throw new InputRangeException("The input value should be greater than GENERAL and VIP parameters.");
                }
            }
        }
    }

    /**
     * 그룹이 유효한지 검사해서 반환하는 함수
     */
    private String selectGroup() {
        while (true) {
            try {
                System.out.println("\n** Press 'end', if you want to exit! **");
                System.out.print("Which group (GENERAL, VIP, VVIP)? ");
                String group = scanner.nextLine().trim().toUpperCase();

                if (group.equals("END")) {
                    return group;
                }

                if ("".equals(group)) {
                    throw new InputEmptyException();
                }

                if (!validGroupType(group)) {
                    throw new InputRangeException();
                }

                return group;

            } catch (NullPointerException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_NULL);
            } catch (InputEmptyException e) {
                System.out.println(e.getMessage());
            } catch (InputRangeException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_GENERAL);
            }
        }
    }

    /**
     * 유효한 그룹인지 검사하는 함수
     */
    private boolean validGroupType(String group) {
        for (GroupType groupType : GroupType.values()) {
            if (group.equals(groupType.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 유효한 그룹 메인 메뉴를 반환하는 함수
     */
    private int getValidGroupMainMenu() {
        while (true) {
            try {
                String setGroupMainMenu = "\n==============================\n" +
                        " 1. Set Parameter\n" +
                        " 2. View Parameter\n" +
                        " 3. Update Parameter\n" +
                        " 4. Back\n" +
                        "==============================\n" +
                        "Choose One: ";
                String menu = showAndSelectMenu(setGroupMainMenu);

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
        }
    }

    /**
     * 유효한 파라미터 메뉴를 반환하는 함수
     */
    private int getValidSetParameterMenu() {
        while (true) {
            try {
                String setParameterMenu = "\n==============================\n" +
                        " 1. Minimum Spent Time\n" +
                        " 2. Minimum Total Pay\n" +
                        " 3. Back\n" +
                        "==============================\n" +
                        "Choose One: ";
                String menu = showAndSelectMenu(setParameterMenu);

                if ("".equals(menu)) {
                    throw new InputEmptyException();
                }

                int result = Integer.parseInt(menu);

                if (!checkMenuValidity(menu, 1, 3)) {
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
     * menu를 입력받고 출력한 후에 유저에게 인풋을 받아 반환하는 함수
     */
    private String showAndSelectMenu(String menu) {
        System.out.print(menu);
        return scanner.nextLine().trim();
    }


}
