
package com.company.Application.Commands;

import com.company.Application.Data;
import com.company.Application.ProductClasses.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * shows if PartNumber contains substring
 */
class FilterPartNumber extends AbstractCommand {
    public FilterPartNumber(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data) {
        ArrayList<Data> responses = new ArrayList<>();
        String subStr = data.getValue();
        Pattern pattern = Pattern.compile(subStr);
        TreeMap<Integer, Product> products = controllersProvider.getTreeMapController().getProducts();
        List<Map.Entry<Integer,Product>> filtered = products.entrySet().stream().filter(e -> pattern.matcher(e.getValue().getPartNumber()).find()).collect(Collectors.toList());
        if (!filtered.isEmpty())
        {
            for(Map.Entry<Integer,Product> entry : filtered){
                responses.add(new Data("filterPartNumber",entry.getKey(), entry.getValue()));
            }
        }
        else {
            responses.add(new Data("filterPartNumber","Совпадений нет"));
        }
        return responses;

   }


    @Override
    public String getInfo(){
        return "filter_contains_part_number string : вывести элементы, значение поля partNumber которых содержит заданную подстроку";
    }
}
