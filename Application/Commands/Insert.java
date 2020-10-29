
package com.company.Application.Commands;


import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Data;
import com.company.Application.ProductClasses.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * insert with key
 */
class Insert extends AbstractCommand {
    public Insert(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data) {
        Product product = data.getProduct();
        Integer key = data.getProductId();
        product.setCreationDate(new Date(new java.sql.Date(new Date().getTime()).getTime()));
        ArrayList<Data> responses = new ArrayList<>();
        try {
            if (!controllersProvider.getTreeMapController().getProducts().containsKey(key)) {
                Long id =  controllersProvider.getDbController().addProduct(key, product);
                if (id!=null){
                    product.setId(id);
                    controllersProvider.getTreeMapController().put(key, product);
                    ServerController.collectionChainged();
                    responses.add(new Data("insert", true));
                }else {
                    responses.add(new Data("insert", false));
                }
            } else {
                responses.add(new Data("insert", false));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при добавлени!");
            responses.add(new Data("insert", false));


        }
        return responses;
    }

    @Override
    public String getInfo() {
        return "insert int: команда служит для добавления элемента в коллекцию";
    }
}
