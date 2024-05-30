package com.MDITech;

import com.MDITech.Utilities.Config;
import com.MDITech.functions.iFunction;
import com.MDITech.functions.iHidden;
import com.MDITech.functions.impl.ChoiceFunction;
import com.google.common.collect.ImmutableMap;
import org.reflections.Reflections;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MDI {


    private static MDI Instance;

    public static MDI getInstance() {
        return Instance;
    }

    public static void main(String[] args) {
        new MDI();
    }

    Scanner scanner = new Scanner(System.in);
    public final ChoiceFunction choiceFunction = new ChoiceFunction();
    File MDIDirectory = new File(System.getProperty("user.dir")+File.separator+"MDITech");
    private static Config config;

    public static Config getConfig() {
        return config;
    }

    public MDI() {

        Instance = this;

        MDIDirectory.mkdirs();
        config = new Config(MDIDirectory,"save.yml");
        System.out.println("  __  __        _ _         _   ___  _    _   _                        \n" +
                " |  \\/  |___ __| (_)__ __ _| | |   \\(_)__| |_(_)___ _ _  __ _ _ _ _  _ \n" +
                " | |\\/| / -_) _` | / _/ _` | | | |) | / _|  _| / _ \\ ' \\/ _` | '_| || |\n" +
                " |_|  |_\\___\\__,_|_\\__\\__,_|_| |___/|_\\__|\\__|_\\___/_||_\\__,_|_|  \\_, |\n" +
                "                                                                  |__/");

        registerFunctions();


        keepAlive();

    }

    private Map<String, iFunction> functions;

    public ImmutableMap<String, iFunction> getFunctions() {
        return ImmutableMap.copyOf(functions);
    }

    void registerFunctions() {
        Reflections reflections = new Reflections("com.MDITech.functions.impl");
        functions = reflections.getSubTypesOf(iFunction.class)
                .stream().map(aClass -> {

                    try {
                        return aClass.getDeclaredConstructor().newInstance();
                    } catch (Exception exception) {
                       exception.printStackTrace();
                       System.out.println("Failed to load function into map!");
                       return null;
                    }

                }).filter(iFunction -> iFunction != null && !(iFunction instanceof iHidden))
                .collect(Collectors.toMap(iFunction -> iFunction.getKey().toLowerCase(), iFunction ->
                        iFunction));
    }


    public void kill() {
        scanner.close();
    }

    public void help() {

        System.out.println("=====[Functions List]=====");
        String[] v  =functions.keySet().stream().sorted(String.CASE_INSENSITIVE_ORDER::compare).toArray(String[]::new);
        for (int i = 0; i < v.length; i++) {

            System.out.println(" "+(i+1)+". "+v[i]);

        }
        System.out.println(" ");
    }

    public void keepAlive() {

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] split = s.split(" ");
            String functionKey = split.length > 0 && split[0] != null ? split[0].toLowerCase() :
                    null;

            if (s.equalsIgnoreCase("!quit")) {
                scanner.close();
                System.exit(1);
                return;
            }

            if (s.equalsIgnoreCase("!help")) {
                help();
                continue;
            }


            boolean back = "!back".equalsIgnoreCase(s);

            if (choiceFunction.isOpen()) {

                if (back) {
                    choiceFunction.esc();
                    continue;
                }


                choiceFunction.receiveArg(split);
                continue;
            }

            iFunction function = functions.get(functionKey);
            if (function == null) {
                System.out.println("There is no existing function like that! Please use the '!help' command to list all functions");
                continue;
            }

            function.receiveArg(split);



        }

    }



}
