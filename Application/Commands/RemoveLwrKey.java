
package com.company.Application.Commands;

import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Data;
import com.company.Application.ProductClasses.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * removes if key is lower
 */
class RemoveLwrKey extends AbstractCommand {
    public RemoveLwrKey(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data) {
        Integer key = data.getProductId();
        ArrayList<Data> responses = new ArrayList<>();
        try {
            controllersProvider.getDbController().removeGrKey(key, data.getLogin());
            TreeMap<Integer, Product> products = controllersProvider.getTreeMapController().getProducts();
            products.keySet().removeIf(k -> (k < key) && (products.get(k).getOwner().getName().equals(data.getLogin())));
            ServerController.collectionChainged();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при удалении!");
            responses.add(new Data("removeLwrKey", false));
        }
        return responses;


    }

    @Override
    public String getInfo() {
        return "remove_lower_key key : удалить из коллекции все элементы, ключ которых меньше чем заданный";
    }
}
