package com.bt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thomas on 27/06/2015.
 */
public class Load {
    private Map<Integer,Employee> employeeIdMap = new HashMap<Integer,Employee>();
    //Unused due to a name not being unique
    //private Map<String,Employee> employeeNameMap = new HashMap<String,Employee>();
    private Map<Integer,HashMap<Integer,Employee>> employeeManagerIdMap = new HashMap<Integer, HashMap<Integer, Employee>>();


    public Map<Integer,Employee> getEmployeeIdMap(){return employeeIdMap;}
    //Unused due to a name not being unique
    //public Map<String,Employee> getEmployeeNameMap(){return employeeNameMap;}
    public Map<Integer,HashMap<Integer,Employee>> getEmployeeManagerIdMapIdMap(){return employeeManagerIdMap;}
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
   //I choose not to used a fixed prams incas the names were biggger then expected.
    //It could be more dynamic but more then good enough for this example
    private void addEmployee(String employee){

        //String nameRegex = "\\w+\\s*";

        String nameRegex = "[a-zA-Z]+[a-zA-Z0-9](\\s*[a-zA-Z0-9])+";
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
            if(employeeManagerIdMap.containsKey(manager)) {
                employeeManagerIdMap.get(manager).put(id,e);
            }
            else{

                HashMap<Integer,Employee> children = new HashMap<Integer,Employee>();
                children.put(id,e);
                employeeManagerIdMap.put(manager,children);
            }

        } else {
            System.out.println("NO MATCH");
        }


    }
}
