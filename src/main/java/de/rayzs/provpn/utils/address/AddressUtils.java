package de.rayzs.provpn.utils.address;

import de.rayzs.provpn.utils.builder.ConnectionBuilder;
import de.rayzs.provpn.plugin.logger.Logger;
import java.util.regex.*;
import java.util.*;

public class AddressUtils {

    private static final List<String> ignored_ips = Arrays.asList(
            "10.0.0.0/8",
            "172.16.0.0/12",
            "192.168.0.0/16",
            "192.168.56.1",
            "127.0.0.1",
            "0.0.0.0",
            "0",
            "localhost");

    public static boolean isVirtualProxy(String address) {
        address = address.contains("/") ? address.replace("/", "") : address;
        address = address.contains(":") ? address.split(":")[0] : address;

        if (ignored_ips.contains(address)) {
            Logger.debug("ip=" + address + " is a local address and cannot be analyzed!");
            return false;
        }

        if(!isValidAddress(address)) {
            Logger.debug("ip=" + address + " is not a valid address and cannot be analyzed!");
            return false;
        }

        ConnectionBuilder connection = new ConnectionBuilder()
                .setUrl("https://www.rayzs.de/provpn/api/proxy.php/?a=" + address)
                .setProperties("ProProtection", "free")
                .connect();

        return connection.hasResponse() && connection.getResponse().equals("true");
    }

    public static boolean isValidAddress(String address) {
        address = address.contains(":") ? address.split(":")[0] : address;
        String zeroTo255 = "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])",
                regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\."  + zeroTo255;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }
}
