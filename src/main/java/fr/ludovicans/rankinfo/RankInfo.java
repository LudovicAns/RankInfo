package fr.ludovicans.rankinfo;

import fr.ludovicans.lanslib.acf.PaperCommandManager;
import fr.ludovicans.lanslib.configuration.ConfigurationManager;
import fr.ludovicans.lanslib.utils.DependencyError;
import fr.ludovicans.lanslib.utils.LuckPermsUtility;
import fr.ludovicans.rankinfo.command.RICommand;
import fr.ludovicans.rankinfo.configuration.Config;
import fr.ludovicans.rankinfo.configuration.Messages;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@SuppressWarnings("unused")
public final class RankInfo extends JavaPlugin {

    private static RankInfo INSTANCE;

    private LuckPerms luckPermsAPI;
    private ConfigurationManager configurationManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        // On initialise les fichiers de configurations.
        this.configurationManager = new ConfigurationManager(this, Level.INFO);
        this.configurationManager.initNewFile(".", "config.yml", Config.CONTENT);
        this.configurationManager.initNewFile(".", "messages.yml", Messages.CONTENT);

        // On enregistre les commandes.
        final @NotNull PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new RICommand());

        // On vérifie que LuckPerms est installé. On désactive le plugin si ce n'est pas le cas.
        try {
            this.luckPermsAPI = LuckPermsUtility.getLuckPermsAPI();
        } catch (DependencyError e) {
            e.printStackTrace();
            this.getLogger().log(Level.SEVERE, "LuckPerms is missing. Disabling RankInfo.");
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        this.configurationManager.saveFiles();
    }

    public @NotNull ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public static @NotNull RankInfo getINSTANCE() {
        return INSTANCE;
    }

    public @NotNull LuckPerms getLuckPermsAPI() {
        return luckPermsAPI;
    }
}
