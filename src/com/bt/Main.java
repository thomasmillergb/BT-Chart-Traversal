package com.bt;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private Map<Integer,Employee> employeeIdMap = new HashMap<Integer,Employee>();
    private Map<String,Employee> employeeNameMap = new HashMap<String,Employee>();
    public static void main(String[] args) {
	// write your code here
        Main main = new Main();
        main.loadEmployees(args[0]);
        System.out.println(main.findManager("Batman", "Catwoman"));
        System.out.println(main.findManager("Batman", "Hit Girl"));

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
        if(managerID!=0 && !managers1.contains(employee)){
            //managers.add(employeeIdMap.get(managerID));
            head = compareManagers(employeeIdMap.get(managerID), managers1);
            //head = employee;
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


    public void loadEmployees(String fileName) {


        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            try {
                br.readLine();
                for (String line; (line = br.readLine()) != null; ) {
                    //System.out.println(line);
                    addEmployee(line);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // line is not visible here.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //Please tell me there is a easier way to convert Strings into objects,
    //because i am really am not happy with this function.
    private void addEmployee(String employee){

        //String nameRegex = "\\w+\\s*";

        String nameRegex = "[a-zA-Z]+(\\s*[a-zA-Z])+";
        String idsRegex = "\\|\\s*[0-9]*\\s*\\|";
        String idRegex= "[0-9]+";
        Pattern r = Pattern.compile(nameRegex);
        Matcher m = r.matcher(employee);

        String name;
        int id =0;
        int manager=0;
        if (m.find()) {
            name= m.group();
            r = Pattern.compile(idsRegex);
            m = r.matcher(employee);

            m.find();
            r = Pattern.compile(idRegex);
            Matcher idR = r.matcher(m.group());
            if (idR.find()) {
                id = Integer.parseInt(idR.group());
            }
            m.find();
            idR = r.matcher(m.group());
            if (idR.find()) {
                manager = Integer.parseInt(idR.group());
            }
            Employee e = new Employee(id,name, manager);
            employeeIdMap.put(id,e);
            employeeNameMap.put(name, e);

        } else {
            System.out.println("NO MATCH1");
        }


    }
}




class Employee{
    private final int id;
    private final int manager;
    private final String name;

    Employee(int id, String name, int manager) {
        this.id = id;
        this.manager = manager;
        this.name = name;
    }
    public int getManager(){return manager;}

    public int getId() {return id;}

    public String toString(){
        return "id: "+id+"   manager: "+ manager+ "   name: "+name;
    }

    public String getName() {
        return name;
    }
}
