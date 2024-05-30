package com.MDITech.functions.impl;

import com.MDITech.MDI;
import com.MDITech.functions.ResponseCode;
import com.MDITech.functions.iFunction;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;

import java.util.Set;

public class ListFunction implements iFunction {
    @Override
    public void esc() {

    }

    @Override
    public int receiveArg(String... args) {

        System.out.println("=========================");
        System.out.println("  List of Med-Terms");
        ConfigurationSection configurationSection = MDI.getConfig().getorAddConfigurationSection("med-term-saves");
        String[] v  = configurationSection.getKeys(false).stream().sorted(String.CASE_INSENSITIVE_ORDER::compare).toArray(String[]::new);
        for (int i = 0; i < v.length; i++) {
            System.out.println(" "+(i+1)+". "+v[i]);
        }

        System.out.println(" ");


        return ResponseCode.ALLOW;
    }

    @Override
    public String getKey() {
        return "list";
    }

    @Override
    public String getUsage() {
        return "list";
    }
}
