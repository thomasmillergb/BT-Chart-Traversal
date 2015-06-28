package com.bt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Thomas on 27/06/2015.
 */

//I have kept solution 2 as close to as a POJO solution, to show i can do traditional Traversal through a POJO tree

public class Solution2 {
    private Map<Integer,Employee> employeeIdMap = new HashMap<Integer,Employee>();
    private Map<String,Employee> employeeNameMap = new HashMap<String,Employee>();
    private Map<Integer,HashMap<Integer,Employee>> employeeManagerIdMap = new HashMap<Integer, HashMap<Integer, Employee>>();


    private EmployeeTree ceo;
    public static void main(String[] args) {

        Load load = new Load();
        load.loadEmployees(args[0]);
        Solution2 main = new Solution2(load.getEmployeeIdMap(),load.getEmployeeNameMap(),load.getEmployeeManagerIdMapIdMap());
        System.out.println(load.getEmployeeManagerIdMapIdMap());
        main.CreateOrganisationTree();
        //main.findEmployee(15);
        System.out.println(main.shortestPath("Super Ted", "Catwoman"));

        //System.out.println(main.findManager("Batman", "Catwoman"));
        //System.out.println(main.findManager("Batman", "Hit Girl"));

    }
    public Solution2(Map<Integer, Employee> employeeIdMap, Map<String, Employee> employeeNameMap, Map<Integer, HashMap<Integer, Employee>> employeeManagerIdMapIdMap) {
        this.employeeNameMap = employeeNameMap;
        this.employeeIdMap = employeeIdMap;
        this.employeeManagerIdMap = employeeManagerIdMapIdMap;
    }


    //A bit of pointless code to prove i can do POJO. The employeeManagerIdMap is already in a good DS to allow Traversal.
    public void CreateOrganisationTree(){
        Employee children = employeeManagerIdMap.get(0).values().iterator().next();
        HashMap<Integer, Employee> tempEmployeeHashMap = employeeManagerIdMap.get(children.getId());
        ceo = new EmployeeTree(children);
        findChildren(ceo);
    }
    public void findChildren(EmployeeTree children){

        if(employeeManagerIdMap.containsKey(children.getEmployee().getId())) {
            HashMap<Integer, Employee> tempEmployeeHashMap = employeeManagerIdMap.get(children.getEmployee().getId());
            Iterator it = tempEmployeeHashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Employee e = (Employee) pair.getValue();
                EmployeeTree tempTree = new EmployeeTree(e);
                tempTree.setManager(children);
                children.getChildren().put(e.getId(), tempTree);
                findChildren(tempTree);

            }
        }
    }
    public String shortestPath(String name1, String name2){
        Stack employee1Path = findEmployeeByName(name1);
        Stack employee2Path = findEmployeeByName(name2);
        String path = "";

        EmployeeTree tempEmp = (EmployeeTree) employee1Path.peek();
        while(!employee2Path.contains(tempEmp)){
            path += tempEmp.getEmployee().toString()+ " -> ";
            employee1Path.pop();
            tempEmp = (EmployeeTree) employee1Path.peek();
           // EmployeeTree tempEmp = (EmployeeTree) employee1Path.peek();
        }
        path += tempEmp.getEmployee().toString();
        tempEmp = (EmployeeTree) employee2Path.peek();
        String path2 ="";
        while(!employee1Path.contains(tempEmp)){
            path2 = " <- "+tempEmp.getEmployee().toString()+ path2;
            employee2Path.pop();
            tempEmp = (EmployeeTree) employee2Path.peek();
            // EmployeeTree tempEmp = (EmployeeTree) employee1Path.peek();
        }

        return path +path2;

    }
    /*
    public EmployeeTree lowestCommonAncestor(EmployeeTree root, EmployeeTree employee1,EmployeeTree employee2){


        }
    }
    */
    //Traverses through the tree till the Employee is found.
    public EmployeeTree findEmployee(int id){
        EmployeeTree e=  findEmployee(id,ceo);
     //   System.out.println(e);
        return e;

    }
    public EmployeeTree findEmployee(int id, EmployeeTree e){
        if(e.getEmployee().getId()==id)
            return e;

        Map<Integer, EmployeeTree> children = e.getChildren();
        if(children.size()==0)
            return e;
        Iterator it= children.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            EmployeeTree eT = (EmployeeTree) pair.getValue();
            EmployeeTree result = findEmployee(id, eT);
            if(result.getEmployee().getId()== id)
               return result;

        }
        return e;

    }
    public Stack findEmployeeByName(String name){
        Stack path = new Stack();
        return findEmployeeByName(name, ceo, path);

    }
    public Stack findEmployeeByName(String name, EmployeeTree e,Stack path){
        if(e.getEmployee().getName().equals(name)) {
            path.push(e);
            return path;
        }
        Map<Integer, EmployeeTree> children = e.getChildren();
        if(children.size()==0){
            //path.push(e);
            return path;
        }


        path.push(e);
        Iterator it= children.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            EmployeeTree eT = (EmployeeTree) pair.getValue();
            EmployeeTree result = (EmployeeTree) findEmployeeByName(name, eT,path).peek();
            if(result.getEmployee().getName().equals(name)){
               // path.push(result);
                return path;
            }


        }
        path.pop();
        return path;

    }
}

class EmployeeTree{
    private Map<Integer,EmployeeTree> children;// = new HashMap<Integer,EmployeeTree>();
    private Employee employee;
    private EmployeeTree manager;

    EmployeeTree(Employee employee) {
        this.employee = employee;
        this.children = new HashMap<Integer,EmployeeTree>();
    //    this.employeeIdMap=employeeIdMap;
    }
    public Map<Integer,EmployeeTree> getChildren(){return children;}
    public Employee getEmployee(){return employee;}
    public EmployeeTree getManager(){return manager;}


    public void setManager(EmployeeTree manager){this.manager = manager;}
    @Override
    public String toString(){return employee.toString();}
    //public void setEmployeeIdMap(Map<Integer,EmployeeTree> employeeIdMap){this.employeeIdMap=employeeIdMap;}
}