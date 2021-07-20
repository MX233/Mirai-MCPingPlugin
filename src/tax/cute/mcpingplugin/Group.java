package tax.cute.mcpingplugin;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import tax.cute.minecraftserverping.*;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Base64;

public class Group extends SimpleListenerHost {
    String name = "[MCPing]";
    Plugin plugin;

    public Group(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private ListeningStatus onMonitor(GroupMessageEvent event) {
        long groupNum = event.getGroup().getId();
        if (plugin.config.isEnable()) {
            String msg = event.getMessage().contentToString();
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

                    Typeset typeset = Typeset.getTypeset(ip,port,plugin.typesetText);

                    if (typeset.isSendFavicon()) {
                        Image image = event.getSubject().uploadImage(ExternalResource.create(typeset.getFavicon_bytes()));
                        event.getSubject().sendMessage(image.plus(typeset.getMotdText()));
                    } else {
                        event.getSubject().sendMessage(typeset.getMotdText());
                    }
                }

                //bindServer function
                if (plugin.config.isGroup(groupNum)) {
                    JSONObject server = plugin.config.getServer(groupNum);
                    String cmd = server.getString("CMD");
                    if (cmd.equalsIgnoreCase(msg)) {
                        String host = server.getString("Host");

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

                        Typeset typeset = Typeset.getTypeset(ip,port,plugin.typesetText);
                        if (typeset.isSendFavicon()) {
                            Image image = event.getSubject().uploadImage(ExternalResource.create(typeset.getFavicon_bytes()));
                            event.getSubject().sendMessage(image.plus(typeset.getMotdText()));
                        } else {
                            event.getSubject().sendMessage(typeset.getMotdText());
                        }

                    }
                }

                if (msg.equalsIgnoreCase("/mcping")) event.getSubject().sendMessage(Menu.mcPingMenu());
            } catch (SocketTimeoutException e) {
                event.getSubject().sendMessage(name + "����ʧ��:���ӳ�ʱ");
            } catch (ConnectException e) {
                event.getSubject().sendMessage(name + "����ʧ��:�޷�����/δ����TCP�˿�");
            } catch (EOFException e) {
                event.getSubject().sendMessage(name + "����ʧ��:���Ӷ�ʧ");
            } catch (SocketException e) {
                event.getSubject().sendMessage(name + "����ʧ��:Զ�̷������Ͽ�������\n" + e);
            } catch (UnknownHostException e) {
                event.getSubject().sendMessage(name + "����ʧ��:" + "��Ч�ĵ�ַ");
            } catch (IOException e) {
                event.getSubject().sendMessage(name + "����ʧ��:\n" + e);
            }
        }

        return ListeningStatus.LISTENING;
    }

}