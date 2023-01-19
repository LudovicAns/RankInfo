package fr.ludovicans.rankinfo.configuration;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class Messages {

    public static final @NotNull String CONTENT = """
            reload: '&aLes fichiers de configurations ont bien été reload.'
            reload-file: '&aLe fichier de configuration &e{filename} &aa bien été reload.'
            unknown-file: '&cLe fichier de configuration &6{filename} &cn''existe pas.'
            no-expirable-rank: "Prefix > &cTu n'as pas de grade expirable."
            expirable-rank:
              header-message: "Prefix > Tu as {count} grade(s) expirable(s):\n"
              rank-expiration-info: "- {rank} expire le {time-left}\n"
            """;

}
