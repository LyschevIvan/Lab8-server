
package com.company.Application.Commands;

import com.company.Application.Controllers.Server.ServerController;
import com.company.Application.Data;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * clears collection
 */
class Clear extends AbstractCommand {
    public Clear(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data) {

        ArrayList<Data> response = new ArrayList<>();
        try {
            ArrayList<Integer> ids = controllersProvider.getDbController().clear(data.getLogin());
            for (Integer id : ids){
                System.out.println(id);
                controllersProvider.getTreeMapController().remove(id);
            }
            ServerController.collectionChainged();
//            response.add(new Data("clear", true));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Ошибка при очистке!");
            response.add(new Data("clear", false));
        }

//        super.response(socket,data);

        return response;
    }

    @Override
    public String getInfo() {
        return "clear : очищает коллекцию";
    }
}
