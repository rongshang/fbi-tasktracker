package task.common.enums;

import java.util.Hashtable;

/**
 * 会计系统 公用枚举.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum EnumRecvTaskContinueAppointFlag {
    RECV_TASK_CONTINUE_APPOINT_FLAG0("0","接收到任务未继续指派"),
    RECV_TASK_CONTINUE_APPOINT_FLAG1("1","接收到任务__继续指派");
    private String code = null;
    private String title = null;
    private static Hashtable<String, EnumRecvTaskContinueAppointFlag> aliasEnums;

    EnumRecvTaskContinueAppointFlag(String code, String title){
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static EnumRecvTaskContinueAppointFlag getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
