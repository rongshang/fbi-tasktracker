package task.common;

import skyline.platform.form.config.SystemAttributeNames;
import skyline.platform.security.OperatorManager;
import task.repository.model.not_mybatis.AttachmentModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class UrlCtrl {
    public String getStrJumpUrl(String strJumpUrlPara,String strPkid1,String strPkid2){
        String strJumpUrl="";
        //首页任务执行情况
        if("ToWAOperMng".equals(strJumpUrlPara)) {
            strJumpUrl = "/UI/task/appoint/execQryToWAOperMng.xhtml?faces-redirect=true&strWorkorderInfoPkid=" +
                    strPkid1 + "&amp;strWorkorderAppointPkid=" + strPkid2;
        }else if("ToExecQry".equals(strJumpUrlPara)) {
            strJumpUrl = "/UI/task/taskDisplay/workorderExecQry.xhtml?faces-redirect=true&strWorkorderInfoPkid=" +
                    strPkid1 + "&amp;strWorkorderAppointPkid=" + strPkid2;
        }
        //工单查询信息
        else if("ToWADetail".equals(strJumpUrlPara)) {
            strJumpUrl = "/UI/task/taskUnit/workorderItemQry.xhtml?faces-redirect=true&strWorkorderInfoPkid=" +
                    strPkid1;
        }else if("ToWAInfo".equals(strJumpUrlPara)) {
            strJumpUrl = "/UI/task/taskUnit/workorderInfoQry.xhtml?faces-redirect=true&strWorkorderInfoPkid=" +
                    strPkid1;
        }
        return strJumpUrl;
    }
    public static void main(String[] argv) {
        //System.out.println(getDateString("2004-10-20"));
    }
}

