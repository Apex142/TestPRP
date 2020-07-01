package fr.apex.main;

import fr.apex.event.SpellsEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginSetup extends JavaPlugin {

    private static PluginSetup plugin;

    public void onEnable() {
        plugin = this;
        System.out.println(References.NAME + " Plugin allumé.");
        getServer().getPluginManager().registerEvents(new SpellsEvent(), this);

    }

    public void onDisable() {
        System.out.println(References.NAME + " Plugin éteint.");
    }

    public static PluginSetup getPlugin() {
        return plugin;
    }
}
