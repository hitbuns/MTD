package com.MDITech.functions.impl;

import com.MDITech.functions.ResponseCode;
import com.MDITech.functions.iFunction;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;

import java.util.Arrays;

public class SearchAllFunction implements iFunction {
    @Override
    public void esc() {

    }

    @Override
    public int receiveArg(String... args) {

        if (args.length == 1) {
            System.out.println("Usage: "+getUsage());
            return ResponseCode.DENY;
        }

        String[] copy = Arrays.copyOfRange(args,1,args.length);

        ConfigurationSection configurationSection = SearchFunction.Instance.config.getorAddConfigurationSection("med-term-saves");

        configurationSection.getKeys(false)
                .stream()
                .filter(s -> Arrays.stream(copy).anyMatch(s1 -> s.toLowerCase().contains(s1.toLowerCase()
                        .replace(".","_")))).sorted(String.CASE_INSENSITIVE_ORDER::compare)
                        .forEachOrdered(s -> SearchFunction.Instance.display(s));

        return ResponseCode.ALLOW;
    }

    @Override
    public String getKey() {
        return "searchall";
    }

    @Override
    public String getUsage() {
        return "searchall <input>";
    }
}
