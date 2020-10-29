
package com.company.Application.Commands;

import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Data;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * remove by key
 */
class Remove extends AbstractCommand {
    public Remove(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data)  {
        int k = data.getProductId();
        ArrayList<Data> responses = new ArrayList<>();
        try {
            controllersProvider.getDbController().removeProduct(k, data.getLogin());
            controllersProvider.getTreeMapController().remove(k);
            ServerController.collectionChainged();
//            responses.add(new Data("remove",true));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошикба при удалении!");
            responses.add(new Data("remove", false));
        }

        return responses;
    }



    @Override
    public String getInfo() {
        return "remove_key k : удаляет элемент с ключем k";
    }
}
