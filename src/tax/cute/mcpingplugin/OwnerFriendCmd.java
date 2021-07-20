package tax.cute.mcpingplugin;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;

public class OwnerFriendCmd extends SimpleListenerHost {
    Plugin plugin;

    public OwnerFriendCmd(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private ListeningStatus onMonitor(FriendMessageEvent event) {
        long qqNum = event.getSender().getId();
        String msg;
        if (plugin.config.isOwner(qqNum)) {
            msg = event.getMessage().contentToString();
        } else {
            msg = "";
        }

        try {
            if (msg.toLowerCase().startsWith("/enable set ")) {
                String[] args = msg.split(" ");
                String bArgs = args[2];
                if (Util.isBoolean(bArgs)) {
                    plugin.config.setEnable(Boolean.parseBoolean(bArgs));
                    event.getSubject().sendMessage(plugin.name + "enable���޸�Ϊ" + bArgs);
                } else {
                    event.getSubject().sendMessage(plugin.name + "��������(��Ҫboolean)");
                }
            }

            if (plugin.config.isEnable()) {
                if (msg.startsWith("/")) {
                    //Get bindServer list function
                    if (msg.equalsIgnoreCase("/bindServer list")) {
                        if (plugin.config.getBindServerList().size() > 0) {
                            ForwardMessageBuilder builder = new ForwardMessageBuilder(event.getFriend());
                            for (int i = 0; i < plugin.config.getBindServerList().size(); i++) {
                                JSONObject server = plugin.config.getBindServerList().getJSONObject(i);
                                StringBuilder sb = new StringBuilder();
                                sb
                                        .append(server.getString("GroupNum"))
                                        .append(":")
                                        .append(server.getString("CMD"))
                                        .append(":")
                                        .append(server.getString("Host"));
                                builder.add(event.getBot().getId(), String.valueOf(i), new PlainText(sb));
                            }
                            event.getSubject().sendMessage(builder.build());
                        } else {
                            event.getSubject().sendMessage(plugin.name + "�󶨷������б�Ϊ��");
                        }
                    }

                    if (msg.toLowerCase().startsWith("/bindserver add ")) {
                        String[] args = msg.split(" ");
                        long num = Long.parseLong(args[2]);

                        //1.group 2.cmd 3.host
                        if (plugin.config.addBindServer(num, args[3], args[4])) {
                            event.getSubject().sendMessage(plugin.name + "�ѽ� " + args[4] + " �󶨵� " + num);
                        } else {
                            event.getSubject().sendMessage(plugin.name + num + "�Ѱ�,�����ظ���");
                        }
                    }

                    if (msg.equalsIgnoreCase("/bindserver remove all")) {
                        int count = plugin.config.removeAllBindServer();
                        event.getSubject().sendMessage(plugin.name + "��������а󶨷�����(" + count + "��)");
                    }else if (msg.toLowerCase().startsWith("/bindserver remove ")) {
                        String[] args = msg.split(" ");
                        long num = Long.parseLong(args[2]);

                        if (plugin.config.removeBindServer(num)) {
                            event.getSubject().sendMessage(plugin.name + "�ѽ��� " + num + " �ķ������Ƴ�");
                        } else {
                            event.getSubject().sendMessage(plugin.name + num + "δ�󶨷�����");
                        }
                    }

                    if (msg.toLowerCase().startsWith("/by owner add ")) {
                        String[] args = msg.split(" ");
                        long num = Long.parseLong(args[3]);
                        if (plugin.config.addOwner(num)) event.getSubject().sendMessage(plugin.name + "�ѽ�" + num + "���Ϊ����");
                        else event.getSubject().sendMessage(plugin.name + "���ʧ��:" + num + "��������,�����ظ����");
                    }

                    if (msg.toLowerCase().startsWith("/by owner remove ")) {
                        String[] args = msg.split(" ");
                        long num = Long.parseLong(args[3]);
                        if (plugin.config.removeOwner(num)) event.getSubject().sendMessage(plugin.name + "�ѽ�" + num + "�Ƴ�����");
                        else event.getSubject().sendMessage(plugin.name + "�Ƴ�ʧ��:" + num + "��������");
                    }

                    if (msg.equalsIgnoreCase("/by owner")) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < plugin.config.getOwner().size(); i++) {
                            sb.append(plugin.config.getOwner().getString(i)).append("\n");
                        }
                        event.getSubject().sendMessage(plugin.name + "����:\n" + sb);
                    }

                    if (msg.toLowerCase().startsWith("/cmd set ")) {
                        String[] args = msg.split(" ");
                        plugin.config.setMcpingCMD(args[2]);
                        event.getSubject().sendMessage(plugin.name + "mcping����ָ���Ѹ���Ϊ:" + args[2]);
                    }

                    if (msg.toLowerCase().startsWith("/reload")) {
                        plugin.config = Config.getConfig(plugin.configFilePath);
                        plugin.typesetText = Util.readText(plugin.typesetFilePath,"GBK");
                        event.getSubject().sendMessage("���������");
                    }

                    if (msg.equalsIgnoreCase("/menu")) event.getSubject().sendMessage(Menu.menu());

                    if(msg.equalsIgnoreCase("/by")) event.getSubject().sendMessage(Menu.ownerMenu());

                    if(msg.equalsIgnoreCase("/bindServer")) event.getSubject().sendMessage(Menu.bindServerMenu());

                }
            }
        } catch (IOException e) {
            event.getSubject().sendMessage(plugin.name + "��д�����ļ�ʱ�����˴���" + e);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            event.getSubject().sendMessage(plugin.name + "�����������");
        }

        return ListeningStatus.LISTENING;
    }
}