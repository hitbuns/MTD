package com.MDITech.functions;

public interface iFunction extends iBack {

    int receiveArg(String... args);

    String getKey();

    String getUsage();

}
