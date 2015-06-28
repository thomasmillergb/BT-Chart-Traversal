package com.bt;


import java.util.*;

public class Solution1 {
    private Map<Integer,Employee> employeeIdMap = new HashMap<Integer,Employee>();
    private Map<String,Employee> employeeNameMap = new HashMap<String,Employee>();
    public static void main(String[] args) {

        Load load = new Load();
        load.loadEmployees(args[0]);
        Solution1 main = new Solution1(load.getEmployeeIdMap(),load.getEmployeeNameMap());
        System.out.println(main.findManager("Batman", "Catwoman"));
        System.out.println(main.findManager("Batman", "Hit Girl"));

    }
    public Solution1(Map<Integer, Employee> employeeIdMap, Map<String, Employee> employeeNameMap){
        this.employeeNameMap = employeeNameMap;
        this.employeeIdMap =employeeIdMap;
    }
    public String findManager(String name1,String name2){
        Employee employee1 = employeeNameMap.get(name1);
        Employee employee2 = employeeNameMap.get(name2);

        HashSet<Integer> managers= new HashSet<Integer>();
        topOfTree(employee1, managers);
        Employee head = compareManagers(employee2, managers);

        return shortestPath(employee1,employee2,head.getId());
    }
    private String shortestPath(Employee employee1, Employee employee2, int headManager){

        Employee currentEmployee = employee2;
        String shortestPath = "";//employee2.getName()+" ("+employee2.getId()+")";

        while(currentEmployee.getId() != headManager){
            shortestPath = " <- "+ currentEmployee.getName()+" ("+currentEmployee.getId()+")"+shortestPath;
            currentEmployee = employeeIdMap.get(currentEmployee.getManager());
        }

        String shortestPath2= "";
        currentEmployee = employee1;
        while(currentEmployee.getId() != headManager){
            shortestPath2 += currentEmployee.getName()+" ("+currentEmployee.getId()+")"+" -> ";
            currentEmployee = employeeIdMap.get(currentEmployee.getManager());
        }
        shortestPath2 += currentEmployee.getName()+" ("+currentEmployee.getId()+")";
        return shortestPath2 +shortestPath;



    }
    private Employee compareManagers(Employee employee,HashSet<Integer> managers1){
        int managerID = employee.getManager();
        Employee head = employee;
        if(managerID!=0 && !managers1.contains(employee.getId())){
            head = compareManagers(employeeIdMap.get(managerID), managers1);

        }
        return head;
    }
    private HashSet<Integer> topOfTree(Employee employee, HashSet<Integer> managers){
        int managerID = employee.getManager();
        if(managerID!=0){
            //managers.add(employeeIdMap.get(managerID));
            topOfTree(employeeIdMap.get(managerID), managers);
            managers.add(managerID);
        }
        return managers;
    }



}




