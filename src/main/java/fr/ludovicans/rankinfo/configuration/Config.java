package fr.ludovicans.rankinfo.configuration;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class Config {

    public static final @NotNull String CONTENT = """
            # Plus d'informations sur comment changer le format ici:
            # > https://help.gooddata.com/cloudconnect/manual/date-and-time-format.html
            date-format: "EEE d MMMM yyyy"
            
            # Plus d'informations sur les time-zone valide ici:
            # > https://docs.oracle.com/middleware/12211/wcs/tag-ref/MISC/TimeZones.html
            time-zone: "Europe/Paris"
            """;

}
