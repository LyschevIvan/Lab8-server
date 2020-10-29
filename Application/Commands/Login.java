package com.company.Application.Commands;

import com.company.Application.Data;

import java.util.ArrayList;

public class Login extends AbstractCommand {
    public Login(ControllersProvider controllersProvider) {
        super(controllersProvider);
    }

    @Override
    ArrayList<Data> execute(Data data) {
        ArrayList<Data> responses = new ArrayList<>();
        if (controllersProvider.getDbController().checkPass(data.getLogin(), data.getPassword())) {
            responses.add(new Data("login", true));
        } else {
            responses.add(new Data("login", false));
        }
        return responses;
    }

    @Override
    String getInfo() {
        return "";
    }
}
