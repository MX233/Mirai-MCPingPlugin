package tax.cute.mcpingplugin;

public class Menu {
    public static String mcPingMenu() {
        return " [ MCPing ] " +
                "\n�ɻ�ȡMC(JE)��������MOTD" +
                "\nʹ�÷���:/mcping <����/IP>" +
                "\n֧��Srv����������"
                ;
    }

    public static String ownerMenu() {
        return "/by" +
                "\n�󶨷�����������������Ȩ��" +
                "\n����:" +
                "\nowner list -- �鿴�����б�" +
                "\nowner add <qq����> -- �������" +
                "\nowner remove <qq����> -- �Ƴ�����";
    }

    public static String bindServerMenu() {
        return
                "�����ܿ�������Ļ�����" +
                        "\nһ��ȺMCPing��һ����ַ" +
                        "\n�ڰ󶨵�Ⱥ�����������õ�����������Զ�����Motd" +
                        "\n/bindServer" +
                        "\n����:" +
        "\nadd <Ⱥ��> <����> <Host> -- ��һ��������" +
                "\nremove <Ⱥ��> -- ��������" +
                        "\nremove all -- ������а�" +
                        "\nPS:\"this\"�ɴ�ָ��Ⱥ";
    }

    public static String menu() {
        return "[ MCPing ] " +
                "\n/MCPing -- �鿴����" +
                "\n/by -- �鿴����" +
                "\n/bindServer -- �鿴����" +
                "\n/enable set <����ֵ>--��������û�����" +
                "\n/cmd set <����> -- �޸�mcping����ָ��" +
                "\n/reload -- ��������";
    }
}
