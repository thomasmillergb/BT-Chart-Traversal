package com.bt;


import java.util.*;
//The Solution I would go straight to if i was not required to show tree Traversal.
//Faster and less complex.
//The solution contains some Java8 features. Which is not allowed according to one of the PDF i was sent.
public class Solution1 {
    private Map<Integer,Employee> employeeIdMap = new HashMap<Integer,Employee>();
    public static void main(String[] args) {
        Load load = new Load();
        load.loadEmployees(args[0]);
        Solution1 main = new Solution1(load.getEmployeeIdMap());
        main.findManager(args[1],args[2]).stream().forEach(System.out::println);
    }
    public Solution1(Map<Integer, Employee> employeeIdMap){
        this.employeeIdMap =employeeIdMap;
    }
    public LinkedList<String> findManager(String name1, String name2){
        Functions f = new Functions();
        final String normalizedName1 = f.stripNoise(name1);
        final String normalizedName2 = f.stripNoise(name2);
        HashSet<Employee> employees1Set = new HashSet<Employee>();
        HashSet<Employee> employees2Set = new HashSet<Employee>();

        //Thought i would some lambda for fun
        employeeIdMap.entrySet().stream()
                .filter(e -> e.getValue().getNormlisedName().equals(normalizedName1))
                .forEach(e -> {
                    employees1Set.add(e.getValue());
                });

        employeeIdMap.entrySet().stream()
                .filter(e -> e.getValue().getNormlisedName().equals(normalizedName2))
                .forEach(e -> employees2Set.add(e.getValue()));


        LinkedList<String> listOfPaths = new LinkedList<String>();
        employees1Set.stream().forEach(e->{
            employees2Set.stream()
                    .filter(e3-> !e3.equals(e))
                    .forEach(e2 -> {
                        HashSet<Integer> managers = new HashSet<Integer>();
                        topOfTree(e, managers);
                        Employee head = compareManagers(e2, managers);
                        listOfPaths.add(shortestPath(e, e2, head.getId()));

                    });
        });
        return listOfPaths;
    }

    private String shortestPath(Employee employee1, Employee employee2, int headManager){

        Employee currentEmployee = employee2;
        String shortestPath = "";//employee2.getName()+" ("+employee2.getId()+")";

        while(currentEmployee.getId() != headManager){
            shortestPath = " <- "+ currentEmployee.toString()+shortestPath;
            currentEmployee = employeeIdMap.get(currentEmployee.getManager());
        }

        String shortestPath2= "";
        currentEmployee = employee1;
        while(currentEmployee.getId() != headManager){
            shortestPath2 += currentEmployee.toString()+" -> ";
            currentEmployee = employeeIdMap.get(currentEmployee.getManager());
        }
        shortestPath2 += currentEmployee.toString();
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




