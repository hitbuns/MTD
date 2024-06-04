package com.MDITech.functions.impl;

import com.MDITech.MDI;
import com.MDITech.Utilities.Config;
import com.MDITech.functions.ResponseCode;
import com.MDITech.functions.iFunction;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;

import java.io.File;
import java.util.Arrays;

public class MergeFunction implements iFunction {


    File parent;
    Config main;

    public MergeFunction() {
        main = MDI.getConfig();
        parent = new File(MDI.getInstance().MDIDirectory.getName()+File.separator+"Insert Merge Files Here");
        parent.mkdirs();
    }

    @Override
    public void esc() {

    }

    @Override
    public int receiveArg(String... args) {

        if (args.length < 2) {
            System.out.println("Usage: "+getUsage());
            return ResponseCode.DENY;
        }

        if (args.length == 2 && args[1].equalsIgnoreCase("all")) {


            for (String s : parent.list()) {
                if (mergeFile(s)) System.out.println("Successfully merged "+s+" contents to main save file!");
                else System.out.println("Failed to merge "+s+" contents to main save file!");
            }

            return ResponseCode.ALLOW;
        }

        String s = String.join(" ", Arrays.copyOfRange(args,1,args.length));
        mergeFile(s);

        return ResponseCode.ALLOW;
    }

    public boolean mergeFile(String file) {

        try {

            if (!file.endsWith(".yml")) {
                return false;
            }

            File file1 = new File(parent,file);

            if (!file1.exists()) {
                return false;
            }

            Config config = new Config(parent,file);
            ConfigurationSection configurationSection =
                    config.getorAddConfigurationSection("med-term-saves"),
            saveTo = main.getorAddConfigurationSection("med-term-saves");

            configurationSection.getKeys(false).forEach(s -> saveTo.set(s,configurationSection.getString(s)));

            main.save();

            return true;
        } catch (Exception exception) {
            return false;
        }

    }

    @Override
    public String getKey() {
        return "!merge";
    }

    @Override
    public String getUsage() {
        return "!merge <all/%file_name%>";
    }
}
