package tax.cute.mcpingplugin;

import com.alibaba.fastjson.JSONArray;
import net.mamoe.mirai.console.command.java.JSimpleCommand;

import java.io.IOException;

public class Console extends JSimpleCommand {
    Plugin plugin;
    public Console(Plugin plugin) {
        super(plugin,"lp",new String[]{}, plugin.getParentPermission());
        this.plugin = plugin;
    }

    @Handler
    public void command(String operation,long args) {
        try {
            switch (operation.toLowerCase()) {
                case "add":
                    plugin.config.addOwner(args);
                    plugin.getLogger().info("�ѽ� " + args + "���Ϊ����");
                    break;

                case "remove":
                    plugin.config.removeOwner(args);
                    plugin.getLogger().info(args + "������������");
                    break;

                default:
                    plugin.getLogger().info("Usage:lp [add/remove/list] [qqNum]");
            }
        } catch (IOException e) {
            plugin.getLogger().info("��д����ʱ�����쳣\n" + e);
        }
    }

    @Handler
    public void command() {
        plugin.getLogger().info("Usage:lp [add/remove/list] [qqNum]");
    }

    @Handler
    public void command(String operation) {
        if (!operation.equalsIgnoreCase("list")) {
            plugin.getLogger().info("Usage:lp [add/remove/list] [qqNum]");
            return;
        }

        JSONArray jsonArray = plugin.config.getOwner();
        plugin.getLogger().info("����:");
        for (Object obj : jsonArray) {
            plugin.getLogger().info(String.valueOf(obj));
        }

    }
}