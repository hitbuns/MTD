package com.MDITech.functions.impl;

import com.MDITech.MDI;
import com.MDITech.functions.ResponseCode;
import com.MDITech.functions.iFunction;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;

import java.util.regex.Pattern;

public class AddFunction implements iFunction
{
    @Override
    public void esc() {

    }

    @Override
    public int receiveArg(String... args) {

        String v = String.join(" ",args);
        int a = v.lastIndexOf("\""), b = v.indexOf("\"");

        if (a == -1 || a == b) {
            System.out.println("Usage: '"+getUsage()+"'");
            return ResponseCode.DENY;
        }

        String definition = v.substring(a+1),medTerm = v.substring(b+1,a);
        ConfigurationSection configurationSection = MDI.getConfig().getorAddConfigurationSection("med-term-saves");
        configurationSection.set(medTerm,definition);
        MDI.getConfig().save();

        return ResponseCode.ALLOW;
    }

    @Override
    public String getKey() {
        return "!add";
    }

    @Override
    public String getUsage() {
        return "!add \"Med Term\" Definition of the term";
    }
}
