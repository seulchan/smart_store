package me.smartstore.project.customer;

import me.smartstore.project.group.Groups;

import java.util.Arrays;

public class Customers {
    private static Customers allCustomers;
    protected static final int DEFAULT_SIZE = 10;
    protected Customer[] customers;
    protected int size; // 실제 객체 수
    protected int capacity; // 배열의 크기

    protected Customers() {
        customers = new Customer[DEFAULT_SIZE];
        capacity = DEFAULT_SIZE;
    }

    public static Customers getInstance() {
        if (allCustomers == null) {
            allCustomers = new Customers();
        }
        return allCustomers;
    }

    /**
     * 저장된 고객수를 반환하는 함수
     */
    public int getSize() {
        return size;
    }

    /**
     * 저장된 고객이 있는지 확인하는 함수
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * i 번째 원소를 반환하는 함수
     */
    public Customer get(int i) {
        if (!(i >= 0 && i < size)) return null;

        return customers[i];
    }

    /**
     * 마지막 원소의 다음에 원소를 추가하는 함수
     */
    public void add(Customer customer) {
        if (customer == null) return;

        if (size < capacity) {
            customers[size] = customer;
            size++;
        } else {
            grow(); // doubling
            add(customer);
        }
    }

    /**
     * i 번째 원소를 추가하는 함수
     */
    public void add(int i, Customer customer) {
        if (!(i >= 0 && i < size)) return;
        if (customer == null) return;

        if (size < capacity) {
            for (int j = size - 1; j >= i; j--) {
                customers[j + 1] = customers[j];
            }
            customers[i] = customer;
            size++;
        } else {
            grow();
            add(i, customer);
        }
    }

    /**
     * i 번째 원소를 삭제하는 함수
     */
    public void pop(int i) {
        if (!(i >= 0 && i < size)) return;

        customers[i] = null; // 명시적으로 원소 삭제되었다고 표시하기 위함 (어차피 i + 1에 의해 덮어씌워짐)

        for (int j = i + 1; j < size; j++) {
            customers[j - 1] = customers[j];
        }
        customers[size - 1] = null;
        size--;
    }

    /**
     * 배열의 크기를 더블링하는 함수
     */
    public void grow() {
        Customer[] temp = new Customer[size];
        System.arraycopy(customers, 0, temp, 0, size);

        capacity *= 2;

        Customer[] newCustomers = new Customer[capacity];
        System.arraycopy(temp, 0, newCustomers, 0, Math.min(size, capacity));

        customers = newCustomers;
    }

    /**
     * 고객들을 그룹의 기준에 맞게 할당하는 함수
     */
    public void reassignGroup() {
        Groups allGroups = Groups.getInstance();
        Arrays.stream(this.customers, 0, this.size)
                .forEach(customer -> customer.setGroup(allGroups.findGroupForCustomer(customer)));
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < size; i++) {
            str.append(customers[i]).append("\n");
        }
        return str.toString();
    }
}
