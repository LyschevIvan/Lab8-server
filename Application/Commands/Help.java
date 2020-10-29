
package com.company.Application.Commands;

import com.company.Application.Data;

import java.util.ArrayList;

/**
 * shows information about commands
 */
class Help extends AbstractCommand {
    CommandInvoker commandInvoker;
    public Help(ControllersProvider controllersProvider, CommandInvoker commandInvoker) {
        super(controllersProvider);
        this.commandInvoker = commandInvoker;
    }

    @Override
    public ArrayList<Data> execute(Data data) {
        ArrayList<Data> responses = new ArrayList<>();
        responses.add(new Data("help",commandInvoker.commandsInfo()));
        return responses;
    }


    @Override
    public String getInfo() {
        return "help : выводит информацию о командах";
    }
}
