import java.net.InetSocketAddress;

public class ServerWrapper {
    public InetSocketAddress ip;
    public String name;

    public ServerWrapper(String newIP, int port, String name) {
        this.ip = new InetSocketAddress(newIP, port);
        this.name = name;
    }
}
