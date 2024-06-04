package com.MDITech.functions.impl;

import com.MDITech.MDI;
import com.MDITech.Utilities.Config;
import com.MDITech.Utilities.KeyedRunnable;
import com.MDITech.functions.ResponseCode;
import com.MDITech.functions.iFunction;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;

import java.util.Arrays;

public class SearchFunction implements iFunction {


    public void display(String key) {
        System.out.println("===================================");
        System.out.println(" ");
        System.out.println("Med Term: "+key);
        System.out.println("Definition: ");
        System.out.println(config.fileConfiguration.getString("med-term-saves."+key));
        System.out.println(" ");
        System.out.println("===================================");
    }

    Config config = MDI.getConfig();

    @Override
    public void esc() {

    }

    @Override
    public int receiveArg(String... args) {

        if (args.length <= 1) {
            System.out.println("Usage: '"+getUsage()+"'");
            return ResponseCode.DENY;
        }

        String[] copy = Arrays.copyOfRange(args,1,args.length);

        ConfigurationSection configurationSection = config.getorAddConfigurationSection("med-term-saves");
        ChoiceFunction choiceFunction = MDI.getInstance().choiceFunction;
        KeyedRunnable[] keyedRunnables = configurationSection.getKeys(false)
                .stream()
                .filter(s -> Arrays.stream(copy).anyMatch(s1 -> s.toLowerCase().contains(s1.toLowerCase()
                        .replace(".","_")))).sorted(String.CASE_INSENSITIVE_ORDER::compare).map(s -> KeyedRunnable.of(s,()-> this.display(s)))
                .toArray(KeyedRunnable[]::new);

        if (keyedRunnables.length == 0) {
            System.out.println("No search results found for Med-Term Search '"+String.join(" ",copy)+"'");
            return ResponseCode.DENY;
        }

        choiceFunction.assignRunnables(keyedRunnables);
        choiceFunction.setState(true);
        choiceFunction.displayChoices();

        return ResponseCode.ALLOW;
    }

    @Override
    public String getKey() {
        return "search";
    }

    @Override
    public String getUsage() {
        return "search <medical term>";
    }
}
