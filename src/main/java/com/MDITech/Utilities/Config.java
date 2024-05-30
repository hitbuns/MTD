package com.MDITech.Utilities;


import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;


public class Config {

    public File f;
    public YamlConfiguration fileConfiguration;

    public Config(File path, String s) {
        setup(path,s);

        System.out.println("FILE>> "+f);
        System.out.println("CF>> "+fileConfiguration);

    }

    public Config(File path, String s, String def) {
        setup(path,s,def);
    }

    public void loaddefaults(String def) {
        load();
        InputStream is = ClassLoader.getSystemResourceAsStream((def.endsWith(".yml"))?def:def+".yml");
        if (is != null) this.fileConfiguration = YamlConfiguration.loadConfiguration( new InputStreamReader(is));
        save();
    }

    public boolean setup(File path,String s) {
        path.mkdirs();
        this.f = new File(path,(s.endsWith(".yml"))?s:s+".yml");
        if (!this.f.exists()) {
            try {
                this.f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            load();
            save();
            return true;
        }
        load();
        save();
        return false;
    }

    public void setup(File path,String s,String def) {
        if (setup(path,s)) loaddefaults(def);
    }

    public void load() {
        System.out.println("INT>> "+this.fileConfiguration);
        this.fileConfiguration = YamlConfiguration.loadConfiguration(f);
        System.out.println("INT-2>> "+this.fileConfiguration);
    }

    public void update(String key,Object value) {
        load();
        this.fileConfiguration.set(key,value);
        save();
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ConfigurationSection getorAddConfigurationSection(String path) {
        return getorAddConfigurationSection(this.fileConfiguration,path);
    }

    public boolean pathExists(String... paths) {
        return pathExists(this.fileConfiguration,paths);
    }

    public boolean pathExists(ConfigurationSection configurationSection,String... paths) {
        return configurationSection != null &&
                Arrays.stream(paths).filter(Objects::nonNull).allMatch(configurationSection::contains);
    }

    public ConfigurationSection getorAddConfigurationSection(ConfigurationSection parent,String path) {
        ConfigurationSection configurationSection = parent.getConfigurationSection(path);
        if (configurationSection == null) configurationSection = parent.createSection(path);
        return configurationSection;
    }

}
