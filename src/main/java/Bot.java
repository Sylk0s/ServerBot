import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.List;

public class Bot extends ListenerAdapter {

    public JDA jda;
    public static Bot bot;
    public String token;
    public static ServerWrapper[] ips = {
            ip("ip as a string goes here",0 /* port (int), set to 25565 for default */, "Server Name"), //do ips like this
    };

    public static void main(String args[]) {
        bot = new Bot();
        bot.token = ""; //bot token goes here
        try {
            bot.jda = JDABuilder.createDefault(bot.token).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        bot.jda.addEventListener(bot);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().equalsIgnoreCase(".serverlist")) {
            EmbedBuilder serverlist = new EmbedBuilder();
            serverlist.setTitle("Server List");
            for(ServerWrapper ip : ips) {
                ServerListPing server = new ServerListPing();
                server.setAddress(ip.ip);
                try {
                    ServerListPing.StatusResponse response = server.fetchData();

                    String players = "";

                    List<ServerListPing.Player> pl = response.getPlayers().getSample();

                    for(int i=0; i < pl.size(); i++ ) {
                        if(players.equals(""))
                            players = players + pl.get(i).getName();
                        else
                            players = players + ", " + pl.get(i).getName();
                    }

                    System.out.println(players);

                    serverlist.addField(ip.name + ": " + server.getAddress(), response.getPlayers().getOnline() + "/" + response.getPlayers().getMax() + " players online: \n" + players, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverlist.setColor(0xFFF758);
            event.getChannel().sendMessage(serverlist.build()).queue();
        }
    }

    public static ServerWrapper ip(String address, int port, String name) {
        return new ServerWrapper(address, port, name);
    }
}
