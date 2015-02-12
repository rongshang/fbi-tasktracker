package task.view.task;

import task.repository.model.model_show.TaskShow;
import task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class ScrollInfoAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ScrollInfoAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private String strCurrentViewMng;
    private String strLastViewMng;

    @PostConstruct
    public void init() {
        try {
            getViewMsg();
            if ("".equals(ToolUtil.getStrIgnoreNull(strCurrentViewMng))){
                strCurrentViewMng="��ӭ������";
            }
            strLastViewMng=strCurrentViewMng;
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    public void getViewMsg() {
        List<TaskShow> taskShowTempList =taskService.initTodoTaskShowList();
        if (taskShowTempList != null) {
            String strViewMngTemp="����������";
            for (TaskShow taskShowItem : taskShowTempList) {
                if (taskShowItem.getName()!=null){
                    strViewMngTemp+=taskShowItem.getName()+";";
                }
            }
            if ("����������".equals(strViewMngTemp)){
                strCurrentViewMng="������������";
            }else {
                if (!(strViewMngTemp.equals(strLastViewMng))){
                    strCurrentViewMng=strViewMngTemp;
                    strLastViewMng=strCurrentViewMng;
                }
            }
        }
    }

    /*�����ֶ� Start*/

    public String getStrCurrentViewMng() {
        return strCurrentViewMng;
    }

    public void setStrCurrentViewMng(String strCurrentViewMng) {
        this.strCurrentViewMng = strCurrentViewMng;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
