package com.nbcb.mytomcat.test.load;


import java.util.*;
import java.io.*;


public class Employee {
    private String name;
    private int age;
    private String department;
    private List<String> tasks;

    public Employee() {
        name = "DefaultName";
        age = 10;
        department = "DefaultDep";
    }

    public Employee(String name, int age, String department) {
        this.name = name;
        this.age = age;
        this.department = department;
    }

    /**
     * 计算雇员的工资
     * 计算逻辑很简单，就是年龄的100倍
     * @return
     */
    public int calulateSalary(){
        return age * 100;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
