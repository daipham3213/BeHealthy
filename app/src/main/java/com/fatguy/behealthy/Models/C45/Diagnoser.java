package com.fatguy.behealthy.Models.C45;

import java.util.ArrayList;
import java.util.List;

public class Diagnoser {


    public Diagnoser() {
    }

    public void diagnose(String attr, Attribute[] attrs) {
        List<String> disease = new ArrayList<>();
        List<String> symtoms = new ArrayList<>();
        symtoms.add(attr);
        disease = findDisease(attr, attrs);
        while (disease.size() > 1) {
            for (int i = 0; i < attrs.length; i++) {
                int pos=0;
                if (attrs[i].values.get(0).valueName.equals("1"))
                    pos = 0;
                else pos = 1;
                List<String> temp = filterDisease(disease, attrs[i].values.get(pos).classes);
                if (!temp.isEmpty()) {
//                    System.out.println("Do you have: " + attrs[i].name);
//                    Scanner scan = new Scanner(System.in);
//                    String ans = scan.nextLine();
//                    if (ans.equals("1")) {
//                        disease = temp;
//                        symtoms = systoms.add(attrs[i].name);
//                    }
                }
                if (disease.size() == 1) break;
            }
        }
        System.out.println("You may caught: " + disease.get(0));
    }

    public List<String> findDisease(String systoms, Attribute[] attrs) {
        int pos = 0;
        List<String> disease = new ArrayList<>();
        for (Attribute item : attrs) {
            if (item.name == null ? systoms == null : item.name.equals(systoms)) {
                if (item.values.get(0).valueName.equals("1"))
                    pos = 0;
                else pos = 1;

                disease = item.values.get(pos).classes;
                break;
            }
        }
        return disease;
    }

    public List<String> filterDisease(List<String> list1, List<String> list2) {
        List<String> rs = new ArrayList<>();
        for (String name : list1) {
            if (list2.contains(name)) {
                rs.add(name);
            }
        }
        return rs;
    }
    public String  getDigaois(String a){
        return a;
    }
}
