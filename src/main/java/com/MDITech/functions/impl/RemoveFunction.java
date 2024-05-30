package com.MDITech.functions.impl;

import com.MDITech.MDI;
import com.MDITech.Utilities.KeyedRunnable;
import com.MDITech.functions.ResponseCode;
import com.MDITech.functions.iFunction;

import java.util.Arrays;

public class RemoveFunction implements iFunction {


    @Override
    public void esc() {

    }

    @Override
    public int receiveArg(String... args) {

        if (args.length <= 1) {
            System.out.println("Usage: '"+getUsage()+"'");
            return ResponseCode.DENY;
        }

        String v = String.join(" ",Arrays.copyOfRange(args,1,args.length));

        if (!MDI.getConfig().fileConfiguration.contains("med-term-saves."+v)) {
            System.out.println("Med-Term does not exist in this dictionary!");
            return ResponseCode.DENY;
        }

        ChoiceFunction choiceFunction = MDI.getInstance().choiceFunction;
        System.out.println("Are you sure you would like to delete this med-term? (\""+v+"\")");
        choiceFunction.assignRunnables(KeyedRunnable.of("Yes",()-> {
            MDI.getConfig().update("med-term-saves."+v.replace(".","_"),null);
            choiceFunction.setState(false);
                }),
                KeyedRunnable.of("No",()-> choiceFunction.setState(false)));

        return ResponseCode.ALLOW;
    }

    @Override
    public String getKey() {
        return "!remove";
    }

    @Override
    public String getUsage() {
        return "!remove <med-term>";
    }
}
