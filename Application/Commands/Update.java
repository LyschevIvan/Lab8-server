
package com.company.Application.Commands;

import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Data;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * uses to change value by key in collection
 */
class Update extends AbstractCommand {
    public Update(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data)  {
        int key = data.getProductId();
        ArrayList<Data> responses = new ArrayList<>();
        try {

                if (controllersProvider.getDbController().updateProduct(key, data.getProduct(), data.getLogin())){
                    controllersProvider.getTreeMapController().getProducts().replace(key, data.getProduct());
                    ServerController.collectionChainged();
                }
                else{
                    responses.add(new Data("update", false));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("Ошибка при изменении");
                responses.add(new Data("update",false));
            }
        return responses;
    }

    @Override
    public String getInfo() {
        return "update key : предлагает изменить данные о продукте с ключем key";
    }
}
