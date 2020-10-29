
package com.company.Application.Commands;

import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Data;
import com.company.Application.ProductClasses.Product;
import com.company.Application.ProductClasses.UnitOfMeasure;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * remove by UnitOfMeasure
 */
class RemoveByUOM extends AbstractCommand {
    public RemoveByUOM(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data)  {
        UnitOfMeasure unitOfMeasure;
        ArrayList<Data> responses = new ArrayList<>();
        switch (data.getValue().toLowerCase()){
            case "kg": unitOfMeasure = UnitOfMeasure.KILOGRAMS; break;
            case "pcs": unitOfMeasure = UnitOfMeasure.PCS; break;
            case "gr": unitOfMeasure = UnitOfMeasure.GRAMS; break;
            case "mg": unitOfMeasure = UnitOfMeasure.MILLIGRAMS; break;
            default: unitOfMeasure = null;
        }

        try{
            controllersProvider.getDbController().removeByOUM(unitOfMeasure, data.getLogin());
            Iterator<Product> valueIterator = controllersProvider.getTreeMapController().getValueIterator();
            while(valueIterator.hasNext()){
                Product k = valueIterator.next();
                if(k.getUnitOfMeasure().equals(unitOfMeasure)){
                    valueIterator.remove();
                }
            }
            ServerController.collectionChainged();
//            responses.add(new Data("removeByUOM",true));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при удалении!");
            responses.add(new Data("removeByUOM",false));
        }
        return responses;
    }
    @Override
    public String getInfo() {
        return "remove_all_by_unit_of_measure (KG|PCS|GR|MG|null) : удалить из коллекции все элементы значение поля unitOfmeasure которого эквивалентно заданному";
    }
}
