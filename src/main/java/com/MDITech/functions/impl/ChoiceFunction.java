package com.MDITech.functions.impl;

import com.MDITech.Utilities.KeyedRunnable;
import com.MDITech.functions.ResponseCode;
import com.MDITech.functions.iFunction;
import com.MDITech.functions.iHidden;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChoiceFunction implements iFunction, iHidden {


    LinkedList<KeyedRunnable> list = new LinkedList<>();
    private boolean open;

    public boolean isOpen() {
        return open;
    }


    public void addRunnable(KeyedRunnable keyedRunnable) {
        if (keyedRunnable != null) this.list.addLast(keyedRunnable);
    }

    public void assignRunnables(KeyedRunnable... runnables) {
        this.list.clear();
        if (runnables != null) {
            for (KeyedRunnable runnable : runnables) {
                list.addLast(runnable);
            }
        }
        if (this.list.size() == 0) setState(false);
    }

    public void setState(boolean isOpen) {
        this.open = isOpen;
    }

    public void reset() {
        assignRunnables(null);
    }

    public ChoiceFunction() {
        this((KeyedRunnable[]) null);
    }

    public ChoiceFunction(KeyedRunnable... runnables) {
        assignRunnables(runnables);
    }

    @Override
    public int receiveArg(String... args) {
        try {

            int code = execute(Integer.parseInt(args[0])) ? ResponseCode.ALLOW :
                    ResponseCode.DENY;

            if (code == ResponseCode.ALLOW) {
                assignRunnables(null);
                return code;
            }

            System.out.println("Please choose a value between 1 and "+list.size()+". To cancel this process type, '!back'");

            return code;
        } catch (Exception exception ) {
            return ResponseCode.DENY;
        }
    }

    public void displayChoices() {
        System.out.println("=============================");
        System.out.println(" ");
        for (int i = 0; i < list.size(); i++) {
            KeyedRunnable keyedRunnable = list.get(i);
            System.out.println(" "+(i+1)+"] "+keyedRunnable.toDisplay());

        }
        System.out.println(" ");
        System.out.println("=============================");
    }

    @Override
    public String getKey() {
        return "choice";
    }

    @Override
    public String getUsage() {
        return "! <this is an admin command> !";
    }

    public boolean execute(int v) {
        v--;
        if (list != null && v < list.size()) {
            list.get(v).run();
            return true;
        }
        return false;
    }

    @Override
    public void esc() {
        reset();
    }

}
