package me.smartstore.project.menu;

import me.smartstore.project.customer.Customer;
import me.smartstore.project.customer.Customers;
import me.smartstore.project.exception.InputEmptyException;
import me.smartstore.project.exception.InputFormatException;
import me.smartstore.project.exception.InputRangeException;
import me.smartstore.project.group.Groups;
import me.smartstore.project.util.Message;
import java.util.regex.Pattern;

import static me.smartstore.project.util.MyScanner.scanner;

public class CustomerMenu extends Menu { // 싱글톤
    private static CustomerMenu customerMenu;
    private final Customers allCustomers = Customers.getInstance();
    private final Groups allGroups = Groups.getInstance();

    private CustomerMenu() {
    }

    public static CustomerMenu getInstance() {
        if (customerMenu == null) {
            customerMenu = new CustomerMenu();
        }
        return customerMenu;
    }

    @Override
    public void process() {
        processSubMenu();
    }

    private void processSubMenu() {
        while (true) {
            int subMenu = getValidCustomerMainMenu();
            switch (subMenu) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    viewCustomer();
                    break;
                case 3:
                    updateCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
                case 5:
                    return;
            }
        }
    }

    /**
     * 고객을 삭제하는 함수
     */
    private void deleteCustomer() {
        int allCustomerNumber = allCustomers.getSize();
        if (allCustomerNumber == 0) {
            System.out.println("No customers. Add customers first.");
            return;
        }

        showAllCustomers();
        int customerNumber = selectCustomerNumber(allCustomerNumber);
        allCustomers.pop(customerNumber - 1);
        System.out.println("\nCustomer Data Deleted Successfully !");
        showAllCustomers();

    }

    /**
     * 고객을 수정하는 함수
     */
    private void updateCustomer() {
        int allCustomerNumber = allCustomers.getSize();

        if (allCustomerNumber == 0) {
            System.out.println("No customers. Add customers first.");
            return;
        }

        showAllCustomers();
        int customerNumber = selectCustomerNumber(allCustomerNumber);

        if (customerNumber >= 1 && customerNumber <= allCustomerNumber) {
            Customer customer = allCustomers.get(customerNumber - 1);
            while (true) {
                boolean done = setCustomerParameters(customer, false);
                if (done) {
                    break;
                }
            }
        }
    }

    /**
     * 고객 번호를 입력받고 유효한지 검사 후 반환하는 함수
     */
    private int selectCustomerNumber(int allCustomerNumber) {
        while (true) {
            try {
                System.out.print("\nWhich customer ( 1 ~ " + allCustomerNumber + " )? ");
                String input = scanner.nextLine();

                if (input == null) {
                    throw new NullPointerException();
                }

                if ("".equals(input)) {
                    throw new InputEmptyException();
                }

                int customerNumber = Integer.parseInt(input);

                if (customerNumber < 1 || customerNumber > allCustomerNumber) {
                    throw new InputRangeException();
                }

                return customerNumber;
            } catch (NullPointerException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_NULL);
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
     * 모든 고객을 보여주는 함수
     */
    private void showAllCustomers() {
        System.out.println("\n======= Customer Info. =======");

        for (int i = 0; i < allCustomers.getSize(); i++) {
            Customer customer = allCustomers.get(i);
            System.out.println("No. " + (i + 1) + " => " + customer);
        }
    }

    /**
     * 고객들을 출력하는 함수
     */
    private void viewCustomer() {
        System.out.println("\n======= Customer Info. =======");
        if (allCustomers.getSize() == 0) {
            System.out.println("No customers. Add customers first.");
            return;
        }

        for (int i = 0; i < allCustomers.getSize(); i++) {
            Customer customer = allCustomers.get(i);
            if (customer != null) {
                System.out.println("No. " + (i + 1) + " => " + customer);
            } else {
                System.out.println("null");
            }
        }
    }

    /**
     * 고객을 생성하는 함수
     */
    private void addCustomer() {
        int customerNumber = getCustomerNumber();
        if (customerNumber == -1) {
            return;
        }

        createCustomers(customerNumber);
    }

    /**
     * 등록할 고객 수를 입력받고 유효한지 검사후 반환하는 함수
     */
    private int getCustomerNumber() {
        while (true) {
            try {
                System.out.println("\n** Press -1, if you want to exit! **");
                System.out.print("How many customers to input? ");
                int customerNumber = Integer.parseInt(scanner.nextLine().trim());

                if (customerNumber == -1) {
                    return customerNumber;
                }

                if (customerNumber == 0 || customerNumber < -1) {
                    throw new InputRangeException();
                }
                return customerNumber;
            } catch (NumberFormatException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_TYPE);
            } catch (InputRangeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 입력받은 고객 수만큼의 고객을 생성하는 함수
     */
    private void createCustomers(int customerNumber) {
        for (int i = 0; i < customerNumber; i++) {
            Customer customer = new Customer();
            System.out.println("\n====== Customer " + (i + 1) + " Info. ======");
            while (true) {
                boolean done = setCustomerParameters(customer, true);
                if (done) {
                    break;
                }
            }
        }
    }

    /**
     * 고객의 파리미터를 설정하는 함수
     * create는 이 함수가 고객 생성할 때 쓰였는지 고객을 수정할 때 쓰였는지 구분하기 위해서 사용
     */
    private boolean setCustomerParameters(Customer customer, Boolean create) {
        while (true) {
            int menu = getValidCustomerAddMenu();
            switch (menu) {
                case 1:
                    setCustomerName(customer);
                    break;
                case 2:
                    setCustomerId(customer);
                    break;
                case 3:
                    setCustomerSpentTime(customer);
                    break;
                case 4:
                    setCustomerTotalPay(customer);
                    break;
                case 5:
                    customer.setGroup(allGroups.findGroupForCustomer(customer));
                    if (create) {
                        allCustomers.add(customer);
                    }
                    return true;
            }
        }
    }

    /**
     * 고객을 입력 받아서 고객의 총 결제액을 설정하는 함수
     */
    private void setCustomerTotalPay(Customer customer) {
        while (true) {
            try {
                System.out.print("\nInput Customer's Total Payment at Your Store: ");
                String input = scanner.nextLine();

                if (input == null) {
                    throw new NullPointerException();
                }

                if ("".equals(input)) {
                    throw new InputEmptyException();
                }

                int totalPay = Integer.parseInt(input);

                if (totalPay < 0) {
                    throw new InputRangeException();
                }

                customer.setTotalPay(totalPay);
                return;
            } catch (NullPointerException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_NULL);
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
     * 고객을 입력 받아서 고객의 총 시간 사용량을 설정하는 함수
     */
    private void setCustomerSpentTime(Customer customer) {
        while (true) {
            try {
                System.out.print("\nInput Customer's Spent Time at Your Store: ");
                String input = scanner.nextLine();

                if (input == null) {
                    throw new NullPointerException();
                }

                if ("".equals(input)) {
                    throw new InputEmptyException();
                }

                int spentTime = Integer.parseInt(input);

                if (spentTime < 0) {
                    throw new InputRangeException();
                }

                customer.setSpentTime(spentTime);
                return;
            } catch (NullPointerException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_NULL);
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
     * 고객을 입력 받아서 고객의 ID를 설정하는 함수
     */
    private void setCustomerId(Customer customer) {
        while (true) {
            try {
                System.out.print("\nInput Customer's UserID: ");
                String regex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
                String id = scanner.nextLine();

                if (id == null) {
                    throw new NullPointerException();
                }

                if ("".equals(id)) {
                    throw new InputEmptyException();
                }

                if (Pattern.matches(regex, id)) {
                    customer.setUserID(id);
                    return;
                } else {
                    throw new InputFormatException();
                }

            } catch (NullPointerException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_NULL);
            } catch (InputEmptyException e) {
                System.out.println(e.getMessage());
            } catch (InputFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 고객을 입력 받아서 고객의 이름을 설정하는 함수
     */
    private void setCustomerName(Customer customer) {
        while (true) {
            try {
                System.out.print("\nInput Customer's Name: ");
                String regex = "^[a-zA-Z]{3,}$";
                String name = scanner.nextLine();

                if (name == null) {
                    throw new NullPointerException();
                }

                if ("".equals(name)) {
                    throw new InputEmptyException();
                }

                if (Pattern.matches(regex, name)) {
                    customer.setName(name);
                    return;
                } else {
                    throw new InputFormatException();
                }

            } catch (NullPointerException e) {
                System.out.println(Message.ERR_MSG_INVALID_INPUT_NULL);
            } catch (InputEmptyException e) {
                System.out.println(e.getMessage());
            } catch (InputFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 유효한 고객 추가 메뉴를 반환하는 함수
     */
    private int getValidCustomerAddMenu() {
        while (true) {
            try {
                String customerAddMenu = "\n==============================\n" +
                        " 1. Customer Name\n" +
                        " 2. Customer ID\n" +
                        " 3. Customer Spent Time\n" +
                        " 4. Customer Total Pay\n" +
                        " 5. Back\n" +
                        "==============================\n" +
                        "Choose One: ";
                String menu = showAndSelectMenu(customerAddMenu);

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
     * 유효한 고객 메인메뉴를 반환하는 함수
     */
    private int getValidCustomerMainMenu() {
        while (true) {
            try {
                String customerMainMenu = "\n==============================\n" +
                        " 1. Add Customer Data\n" +
                        " 2. View Customer Data\n" +
                        " 3. Update Customer Data\n" +
                        " 4. Delete Customer Data\n" +
                        " 5. Back\n" +
                        "==============================\n" +
                        "Choose One: ";
                String menu = showAndSelectMenu(customerMainMenu);

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
     * 메뉴를 입력받아서 출력하고 유저 인풋을 받환하는 함수
     */
    private String showAndSelectMenu(String menu) {
        System.out.print(menu);
        return scanner.nextLine().trim();
    }

}
