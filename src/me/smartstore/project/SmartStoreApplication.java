package me.smartstore.project;


import me.smartstore.project.customer.Customer;
import me.smartstore.project.customer.Customers;
import me.smartstore.project.group.Group;
import me.smartstore.project.group.GroupType;
import me.smartstore.project.group.Groups;
import me.smartstore.project.group.Parameter;
import me.smartstore.project.menu.CustomerMenu;
import me.smartstore.project.menu.GroupMenu;
import me.smartstore.project.menu.MainMenu;
import me.smartstore.project.menu.SummaryMenu;

import static me.smartstore.project.util.MyScanner.scanner;

public class SmartStoreApplication { // 싱글톤
    private static SmartStoreApplication smartStoreApplication;

    private SmartStoreApplication() {
    }

    public static SmartStoreApplication getInstance() {
        if (smartStoreApplication == null) {
            smartStoreApplication = new SmartStoreApplication();
        }
        return smartStoreApplication;
    }

    /**
     * 애플리케이션와 관련된 정보 출력
     */
    public void details() {
        System.out.println("===========================================");
        System.out.println(" Title : SmartStore Customer Classification");
        System.out.println(" Release Date : 22.11.30");
        System.out.println(" Author : Seulchan Hwang");
        System.out.println("===========================================\n");
    }


    /**
     * 테스트를 위해서 그룹과 유저 생성
     */
    public SmartStoreApplication test() {
        Groups allGroups = Groups.getInstance();
        Customers allCustomers = Customers.getInstance();
        allGroups.add(new Group(GroupType.GENERAL, new Parameter(50, 50)));
        allGroups.add(new Group(GroupType.VIP, new Parameter(100, 100)));
        allGroups.add(new Group(GroupType.VVIP, new Parameter(150, 150)));

        for (int i = 0; i < 20; i++) {
            Customer customer = new Customer(
                    Character.toString((char) ('a' + i)), (char) ('a' + i) + "12345",
                    i * 10, i * 10);
            customer.setGroup(allGroups.findGroupForCustomer(customer));
            allCustomers.add(customer);
        }
        return this;
    }

    /**
     * 스마트 스토어를 시작하기 위한 함수
     */
    public void run() {
        // 메뉴 초기화
        MainMenu mainMenu = MainMenu.getInstance();
        CustomerMenu customerMenu = CustomerMenu.getInstance();
        GroupMenu groupMenu = GroupMenu.getInstance();
        SummaryMenu summaryMenu = SummaryMenu.getInstance();

        // 메인 메뉴
        while (true) {
            int mainPick = mainMenu.selectValidMainMenu();
            switch (mainPick) {
                case 1:
                    groupMenu.process();
                    break;
                case 2:
                    customerMenu.process();
                    break;
                case 3:
                    summaryMenu.process();
                    break;
                case 4:
                    scanner.close();
                    return;
            }
        }
    }

}
