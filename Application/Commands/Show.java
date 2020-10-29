
package com.company.Application.Commands;

import com.company.Application.Data;
import com.company.Application.ProductClasses.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * print collection content to System.out
 */
class Show extends AbstractCommand {
    public Show(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data)  {
        ArrayList<Data> responses = new ArrayList<>();
        TreeMap<Integer,Product> products = controllersProvider.getTreeMapController().getProducts();
        List<Map.Entry<Integer, Product>> sorted = products.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        responses.add(new Data("show",true));
        if (!sorted.isEmpty()){
            for (Map.Entry<Integer, Product> entry : sorted){
                responses.add(new Data("show",entry.getKey(),entry.getValue()));
//                System.out.println(new Data(entry.getKey(),entry.getValue()).toString());
            }
        }
        responses.add(new Data("show","end"));



        return responses;
    }



    @Override
    public String  getInfo() {
       return "show : выводит элементы коллекции";
    }
}
