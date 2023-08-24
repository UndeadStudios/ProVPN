package de.rayzs.provpn.api.actionbar;

import de.rayzs.provpn.utils.reflection.Reflection;
import de.rayzs.provpn.api.actionbar.impl.*;

public class Actionbar {

    private static final TextBar actionbar;

    static {
        if(Reflection.isBungeecordServer()) actionbar = new BungeeActionbar();
        else if(Reflection.getMinor() >= 17) actionbar = new BukkitAPIActionbar();
        else actionbar = new BukkitReflectionActionbar(Reflection.getVersionPackageName());
    }

    public static void execute(Object object, String text) { actionbar.execute(object, text);
    }
}