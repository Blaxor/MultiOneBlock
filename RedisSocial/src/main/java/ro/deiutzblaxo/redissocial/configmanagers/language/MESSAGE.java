package ro.deiutzblaxo.redissocial.configmanagers.language;

public enum MESSAGE {

    ERROR_INVALID_ARGUMENTS_PM("error.invalidarg.pm", "&cPlease use &l/pm <player> <message>"),
    ERROR_INVALID_ARGUMENTS_REPLAY("error.invalidarg.replay", "&cPlease use &l/r <message>"),
    ERROR_SELF("error.self", "&cYou can't send yourself message!"),
    PREFIX_MESSAGE_SEND("prefix.send", "&eSend to {target} -> &f"),
    PREFIX_MESSAGE_RECEIVE("prefix.receive", "&eFrom {sender} -> &f"),
    PREFIX_SOCIAL_SPY("socialspy.prefix", "&7[SocialSpy] &eFrom {sender} to {target} -> &f"),
    ERROR_PERMISSION("error.noPermission","&4You don`t have the permission!"),
    SOCIALSPY_DISABLE("socialspy.disable","&eSocialSpy disabled!"),
    SOCIALSPY_ENABLED("socialspy.enable","&eSocialSpy enabled!"),
    ERROR_PLAYER_OFFLINE("error.playerOffline","&cJucatorul este offline!"),
    ERROR_PLAYER_NO_EXIST("error.playerNoExist","&cJucatorul nu exista!");


    protected String path;
    protected String original;


    MESSAGE(String path, String original) {
        this.path = path;
        this.original = original;
    }
}
