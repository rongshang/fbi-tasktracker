package task.view.task;

import task.repository.model.model_show.TaskShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class ScrollInfoAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ScrollInfoAction.class);
    private String strCurrentViewMng;
    private String strLastViewMng;

    @PostConstruct
    public void init() {
        try {
            getViewMsg();
            if ("".equals(ToolUtil.getStrIgnoreNull(strCurrentViewMng))){
                strCurrentViewMng="欢迎您！！";
            }
            strLastViewMng=strCurrentViewMng;
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    public void getViewMsg() {
        List<TaskShow> taskShowTempList =new ArrayList<>();//taskService.initTodoTaskShowList();
        if (taskShowTempList != null) {
            String strViewMngTemp="待处理任务：";
            for (TaskShow taskShowItem : taskShowTempList) {
                if (taskShowItem.getName()!=null){
                    strViewMngTemp+=taskShowItem.getName()+";";
                }
            }
            if ("待处理任务：".equals(strViewMngTemp)){
                strCurrentViewMng="待处理任务：无";
            }else {
                if (!(strViewMngTemp.equals(strLastViewMng))){
                    strCurrentViewMng=strViewMngTemp;
                    strLastViewMng=strCurrentViewMng;
                }
            }
        }
    }

    /*智能字段 Start*/

    public String getStrCurrentViewMng() {
        return strCurrentViewMng;
    }

    public void setStrCurrentViewMng(String strCurrentViewMng) {
        this.strCurrentViewMng = strCurrentViewMng;
    }
}
