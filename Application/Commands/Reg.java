package com.company.Application.Commands;

import com.company.Application.Data;

import java.sql.SQLException;
import java.util.ArrayList;

public class Reg extends AbstractCommand{
    public Reg(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    public ArrayList<Data> execute(Data data)  {
        ArrayList<Data> responses = new ArrayList<>();

        try {
            controllersProvider.getDbController().addUser(data.getLogin(), data.getPassword());
            responses.add(new Data("reg",true));
        }
        catch (SQLException e){
            responses.add(new Data("reg", false));
        }

        return responses;
    }

    @Override
    String getInfo() {
        return "reg login password: для регистрации новых пользователей";
    }
}
