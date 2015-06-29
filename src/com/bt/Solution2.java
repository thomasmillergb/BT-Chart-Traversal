package com.bt;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thomas on 27/06/2015.
 */

    //I have kept solution 2 as close to as a POJO solution, to show i can do traditional Traversal through a POJO tree
    //Even though this solution works with no issues, it is not the most efficient solution due to needing to traverse
    //through the tree.

    //Future Optimizations
    /*
    A big waste of search time is searching for multiple names because every time the program needs to find a employee
    it has to traverse through the tree from the beginning again. To optimize, every time an employee is found, the path
    should be added to a collection. Once the traversal of the tree has completed the collection of path should be returned.

     */


public class Solution2 {

    //Not the best looking DS but it work very effectively
    private Map<Integer,HashMap<Integer,Employee>> employeeManagerIdMap;
    private Functions f = new Functions();
    //The head of the Tree
    private EmployeeTree ceo;


    public static void main(String[] args) {

        Load load = new Load();
        load.loadEmployees(args[0]);
        Solution2 main = new Solution2(load.getEmployeeManagerIdMapIdMap());
        System.out.println(load.getEmployeeManagerIdMapIdMap());
        main.CreateOrganisationTree();
        //main.findEmployee(15);
        for(String s:main.shortestPath(args[1],args[2]))
            System.out.println(s);
        // I would normally write this
        //main.shortestPath(args[1],args[2]).forEach(System.out::println);
    }
    public Solution2(Map<Integer, HashMap<Integer, Employee>> employeeManagerIdMapIdMap) {
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
    public LinkedList<String> shortestPath(String name1, String name2){
        HashSet<EmployeeTree> employee1PathSet = new HashSet<EmployeeTree>();
        //while(employee1PathSet.contains())
        name1=f.stripNoise(name1);
        name2=f.stripNoise(name2);

        HashSet employee1Paths = findEmployeeByName(name1);
        HashSet employee2Paths = findEmployeeByName(name2);

        LinkedList<String> listOfPaths = new LinkedList<String>();
        for (Object emp1 :  employee1Paths) {
            for (Object emp2 : employee2Paths) {
                Stack clone1 = new Stack();
                Stack clone2 = new Stack();
                clone1.addAll((Stack) emp1);
                clone2.addAll((Stack) emp2);
                listOfPaths.add(getPath(clone1, clone2));
            }
        }

        return listOfPaths;

    }

    public String getPath(Stack employee1Path, Stack employee2Path){
        String path = "";

        EmployeeTree tempEmp = (EmployeeTree) employee1Path.peek();
        while (!employee2Path.contains(tempEmp)) {
            path += tempEmp.getEmployee().toString() + " -> ";
            employee1Path.pop();
            tempEmp = (EmployeeTree) employee1Path.peek();
            // EmployeeTree tempEmp = (EmployeeTree) employee1Path.peek();
        }
        path += tempEmp.getEmployee().toString();
        tempEmp = (EmployeeTree) employee2Path.peek();
        String path2 = "";
        while (!employee1Path.contains(tempEmp)) {
            path2 = " <- " + tempEmp.getEmployee().toString() + path2;
            employee2Path.pop();
            tempEmp = (EmployeeTree) employee2Path.peek();
            // EmployeeTree tempEmp = (EmployeeTree) employee1Path.peek();
        }

        return path + path2;

    }
    /*
    public EmployeeTree lowestCommonAncestor(EmployeeTree root, EmployeeTree employee1,EmployeeTree employee2){


        }
    }
    */
    //Traverses through the tree till the Employee is found.
    public HashSet<Stack> findEmployeeByName(String name){
        HashSet<EmployeeTree> employee1PathSet = new HashSet<EmployeeTree>();
        HashSet<Stack> paths = new HashSet<Stack>();
        boolean end= true;
        while(!employee1PathSet.contains(ceo)&&end) {
            Stack path = new Stack();
            path =findEmployeeByName(name, ceo, path, employee1PathSet);
            if(path.size()==0){
                end =false;
            }
            else {
                EmployeeTree last = (EmployeeTree) path.peek();
                if (!last.getEmployee().getNormlisedName().equals(name))
                    end = false;
                else {
                    employee1PathSet.add(last);
                    paths.add(path);
                }
            }
        }
        return paths;
    }
    public Stack findEmployeeByName(String name, EmployeeTree e,Stack path,HashSet<EmployeeTree> employee1PathSet){
        if(e.getEmployee().getNormlisedName().equals(name)&&!employee1PathSet.contains(e)) {
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
            EmployeeTree result = (EmployeeTree) findEmployeeByName(name, eT,path,employee1PathSet).peek();

            if(!employee1PathSet.contains(result) &&result.getEmployee().getNormlisedName().equals(name)){
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