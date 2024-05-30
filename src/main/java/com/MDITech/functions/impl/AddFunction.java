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

        String[] ab = definition.substring(1).split(" ");

        StringBuilder stringBuilder = new StringBuilder();

        int count = 0;
        for (String s : ab) {
           stringBuilder.append(s).append(" ");
           count += s.length();
           if (count >= 45) stringBuilder.append("\n");
        }

        configurationSection.set(medTerm.toLowerCase(),stringBuilder.toString());
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
