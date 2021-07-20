package tax.cute.mcpingplugin;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import tax.cute.minecraftserverping.MCPing;
import tax.cute.minecraftserverping.Punycode;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Base64;

public class Friend extends SimpleListenerHost {
    Plugin plugin;

    public Friend(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private ListeningStatus onMonitor(FriendMessageEvent event) {
        long qqNum = event.getSender().getId();
        String msg = event.getMessage().contentToString();

        if (plugin.config.isEnable()) {
            try {
                if (msg.toLowerCase().startsWith(plugin.config.getMcpingCMD() + " ")) {
                    //MCPing main function
                    String host = Util.combineArgs(msg, 1);
                    String ip;
                    int port;
                    if (host.contains(":")) {
                        ip = host.split(":")[0];
                        port = Integer.parseInt(host.split(":")[1]);
                    } else {
                        ip = host;
                        port = 25565;
                    }

                    //Chinese domain encode
                    ip = Punycode.encodeURL(ip);

                    //Check if there is a Srv record
                    Srv srv = Srv.getSrv(ip, Util.MC_SRV);
                    if (srv.isExist()) {
                        event.getSubject().sendMessage("��⵽����Srv��¼,���Զ���ת��\n>>\n" + srv.getSrvHost() + ":" + srv.getSrvPort());
                        ip = srv.getSrvHost();
                        port = srv.getSrvPort();
                    }

                    Typeset typeset = Typeset.getTypeset(ip, port, plugin.typesetText);

                    if (typeset.isSendFavicon()) {
                        Image image = event.getSubject().uploadImage(ExternalResource.create(typeset.getFavicon_bytes()));
                        event.getSubject().sendMessage(image.plus(typeset.getMotdText()));
                    } else {
                        event.getSubject().sendMessage(typeset.getMotdText());
                    }
                }

                if (msg.equalsIgnoreCase("/getowner")) {
                    if (plugin.config.getOwner().size() < 1) {
                        plugin.config.addOwner(qqNum);
                        event.getSubject().sendMessage(plugin.name + "���ѳ�Ϊ����");
                    }
                }
            } catch (SocketTimeoutException e) {
                event.getSubject().sendMessage(plugin.name + "����ʧ��:���ӳ�ʱ");
            } catch (ConnectException e) {
                event.getSubject().sendMessage(plugin.name + "����ʧ��:�޷�����/δ����TCP�˿�");
            } catch (EOFException e) {
                event.getSubject().sendMessage(plugin.name + "����ʧ��:���Ӷ�ʧ");
            } catch (SocketException e) {
                event.getSubject().sendMessage(plugin.name + "����ʧ��:Զ�̷������Ͽ�������\n" + e);
            } catch (UnknownHostException e) {
                event.getSubject().sendMessage(plugin.name + "����ʧ��:" + "��Ч�ĵ�ַ");
            } catch (IOException e) {
                event.getSubject().sendMessage(plugin.name + "�����쳣:\n" + e);
            }

            if (msg.equalsIgnoreCase("/mcping")) event.getSubject().sendMessage(Menu.mcPingMenu());
        }

        return ListeningStatus.LISTENING;
    }
}
