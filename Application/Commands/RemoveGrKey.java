
package com.company.Application.Commands;

import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Data;
import com.company.Application.ProductClasses.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * remove if key is greater
 */
class RemoveGrKey extends AbstractCommand {
    public RemoveGrKey(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data)  {
        ArrayList<Data> responses = new ArrayList<>();
        Integer key = data.getProductId();
        try{
            controllersProvider.getDbController().removeGrKey(key, data.getLogin());
            TreeMap<Integer, Product> products = controllersProvider.getTreeMapController().getProducts();
            products.keySet().removeIf(k -> (k > key) && (products.get(k).getOwner().getName().equals(data.getLogin())));
            ServerController.collectionChainged();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при удалении!");
            responses.add(new Data("removeGrKey", false));
        }
        return responses;
    }

    @Override
    public String getInfo() {
        return "remove_greater_key key : удалить из коллекции все элементы, ключ которых превышает заданный";
    }
}
