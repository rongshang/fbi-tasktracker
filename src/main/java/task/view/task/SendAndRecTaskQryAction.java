package task.view.task;

import task.repository.model.not_mybatis.TaskShow;
import task.service.TaskService;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class SendAndRecTaskQryAction {
    private static final Logger logger = LoggerFactory.getLogger(SendAndRecTaskQryAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private TreeNode stlPowerRoot;
    private List<TaskShow> taskList;

    @PostConstruct
    public void init() {
        //整个任务列表
        try {
            stlPowerRoot = new DefaultTreeNode("ROOT", null);
            TaskShow taskShow = new TaskShow();
            taskShow.setId("工单任务");
            TreeNode stlPower = new DefaultTreeNode(taskShow, stlPowerRoot);
            stlPower.setExpanded(true);
            TaskShow taskShowFront1 = new TaskShow();
            taskShowFront1.setId("已接收任务");
            TreeNode rectask = new DefaultTreeNode(taskShowFront1, stlPower);
            getChildRecNode(rectask);
            TaskShow taskShowFront2 = new TaskShow();
            taskShowFront2.setId("已发出任务");
            TreeNode sendtask = new DefaultTreeNode(taskShowFront2, stlPower);
            getChildSendNode(sendtask);
        } catch (Exception e) {
            logger.error("初始化失败", e);
        }
    }

    //获取结算单最末端节点   传入参数父节点
    private TreeNode getChildSendNode(TreeNode sendtask)
            throws Exception {
        List<TaskShow> taskListSend = taskService.initSendTaskShowList();
        if (taskListSend.size() > 0) {
            for (int i = 0; i < taskListSend.size(); i++) {
                TaskShow taskShow = new TaskShow();
                TaskShow taskShowTemp = taskListSend.get(i);
                taskShow.setTaskid(taskShowTemp.getTaskid());
                taskShow.setTaskname(taskShowTemp.getTaskname());
                taskShow.setCreateby(taskShowTemp.getCreateby());
                taskShow.setCreatetime(taskShowTemp.getCreatetime());
                taskShow.setName(taskShowTemp.getName());
                taskShow.setRecer(taskShowTemp.getRecer());
                new DefaultTreeNode(taskShow, sendtask);
            }
        } else {
            return null;
        }
        return null;
    }

    private TreeNode getChildRecNode(TreeNode rectask)
            throws Exception {
        List<TaskShow> taskListRec = taskService.initRecTaskShowList();
        if (taskListRec.size() > 0) {
            for (int i = 0; i < taskListRec.size(); i++) {
                TaskShow taskShow = new TaskShow();
                TaskShow taskShowTemp = taskListRec.get(i);
                taskShow.setTaskid(taskShowTemp.getTaskid());
                taskShow.setTaskname(taskShowTemp.getTaskname());
                taskShow.setCreateby(taskShowTemp.getCreateby());
                taskShow.setCreatetime(taskShowTemp.getCreatetime());
                taskShow.setName(taskShowTemp.getName());
                taskShow.setRecer(taskShowTemp.getRecer());
                new DefaultTreeNode(taskShow, rectask);
            }
        } else {
            return null;
        }
        return null;
    }

    public TreeNode getStlPowerRoot() {
        return stlPowerRoot;
    }

    public void setStlPowerRoot(TreeNode stlPowerRoot) {
        this.stlPowerRoot = stlPowerRoot;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public List<TaskShow> getStlPowerList() {
        return taskList;
    }

    public void setStlPowerList(List<TaskShow> stlPowerList) {
        this.taskList = stlPowerList;
    }
}