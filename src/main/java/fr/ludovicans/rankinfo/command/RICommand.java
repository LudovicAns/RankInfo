package fr.ludovicans.rankinfo.command;

import fr.ludovicans.lanslib.acf.BaseCommand;
import fr.ludovicans.lanslib.acf.annotation.*;
import fr.ludovicans.lanslib.configuration.ConfigurationManager;
import fr.ludovicans.lanslib.utils.ColoredText;
import fr.ludovicans.lanslib.utils.DependencyError;
import fr.ludovicans.lanslib.utils.LuckPermsUtility;
import fr.ludovicans.rankinfo.RankInfo;
import fr.ludovicans.rankinfo.configuration.Config;
import fr.ludovicans.rankinfo.configuration.Messages;
import net.luckperms.api.node.Node;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("unused")
@CommandAlias("rankinfo|ri")
public class RICommand extends BaseCommand {

    private final static ConfigurationManager CM = RankInfo.getINSTANCE().getConfigurationManager();
    private static final @NotNull String BASE_PERMISSION = "rankinfo.command";

    @Default
    @CommandPermission(BASE_PERMISSION)
    private static void onCommand(Player player) {
        try {
            // On récupère les grades expirables du joueur.
            final @NotNull List<Node> expirableGroup = LuckPermsUtility.getUser(player).getNodes()
                    .stream()
                    .filter(node -> node.getKey().contains("group"))
                    .filter(Node::hasExpiry)
                    .toList();

            // On récupère le fichier de configuration où se trouve les messages.
            final @Nullable FileConfiguration messagesFile = CM.getConfigurationFile("messages.yml");
            if (messagesFile == null) {
                CM.initNewFile(".", "messages.yml", Messages.CONTENT);
            }
            assert messagesFile != null;

            // Si le joueur n'a pas de grade expirable, on lui envoie le message associé.
            if (expirableGroup.isEmpty()) {
                player.sendMessage(new ColoredText(
                        messagesFile.getString("no-expirable-rank", "&cYou do not have any expirable ranks.")
                ).buildComponent());
                return;
            }

            // On récupère le fichier de configuration du plugin.
            final @Nullable FileConfiguration configurationFile = CM.getConfigurationFile("config.yml");
            if (configurationFile == null) {
                CM.initNewFile(".", "config.yml", Config.CONTENT);
            }
            assert configurationFile != null;

            @NotNull String message = messagesFile.getString(
                            "expirable-rank.header-message",
                            "You have &e{count} &rexpirable ranks:"
                    ).replaceAll("\\{count}", String.valueOf(expirableGroup.size()));

            player.sendMessage(new ColoredText(message).buildComponent());

            for (Node node : expirableGroup) {
                final @NotNull String rankName = node.getKey().replace("group.", "");
                final @NotNull String rankNameCap = rankName.substring(0, 1).toUpperCase() + rankName.substring(1);

                final @NotNull LocalDate expirationDate = Objects.requireNonNull(node.getExpiry()).atZone(ZoneId.of(
                        configurationFile.getString("time-zone", "Europe/Paris")
                )).toLocalDate();

                final @NotNull String pattern = configurationFile.getString("date-format", "EEE d MMMM yyyy");

                message = messagesFile.getString(
                                "expirable-rank.rank-expiration-info",
                                "- &a{rank} &rexpire at &c{time-left}&r")
                        .replaceAll("\\{rank}", rankNameCap)
                        .replaceAll(
                                "\\{time-left}",
                                expirationDate.format(DateTimeFormatter.ofPattern(pattern, Locale.FRANCE)));

                player.sendMessage(new ColoredText(message).buildComponent());
            }

        } catch (DependencyError e) {
            e.printStackTrace();
        }
    }

    @Subcommand("reload")
    @Syntax("[fileName]")
    @CommandCompletion("@rireload")
    @CommandPermission(BASE_PERMISSION + ".reload")
    public static void onReload(CommandSender sender, @Optional String fileName){
        FileConfiguration messages = CM.getConfigurationFile("messages.yml");
        if (messages == null) {
            CM.initNewFile(".", "messages.yml", Messages.CONTENT);
        }
        assert messages != null;

        if (fileName != null && !fileName.isEmpty()) {
            if (CM.getConfigurationFile(fileName) != null) {
                CM.reloadFile(fileName);
                sender.sendMessage(new ColoredText(
                        messages.getString("reload-file", "Configuration file " + fileName + " reloaded.")
                                .replaceAll("\\{filename}", fileName))
                        .buildComponent());
            } else {
                sender.sendMessage(new ColoredText(
                        messages.getString("unknown-file", "Unknown configuration file.")
                                .replaceAll("\\{filename}", fileName))
                        .buildComponent());
            }
        } else {
            CM.reloadFiles();
            sender.sendMessage(new ColoredText(
                    messages.getString("reload", "Configuration files reloaded.")).buildComponent()
            );
        }
    }

}
