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
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class SendAndRecTaskQryAction {
    private static final Logger logger = LoggerFactory.getLogger(SendAndRecTaskQryAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private TreeNode selfTaskTreeRoot;
    private List<TaskShow> taskList;

    @PostConstruct
    public void init() {
        //���������б�
        try {
            selfTaskTreeRoot = new DefaultTreeNode("ROOT", null);
            TaskShow taskShow = new TaskShow();
            taskShow.setRowHeaderText("��������");
            TreeNode stlPower = new DefaultTreeNode(taskShow, selfTaskTreeRoot);
            stlPower.setExpanded(true);
            TaskShow taskShowFront1 = new TaskShow();
            taskShowFront1.setRowHeaderText("�ѽ�������");
            TreeNode rectask = new DefaultTreeNode(taskShowFront1, stlPower);
            getChildRecNode(rectask);
            TaskShow taskShowFront2 = new TaskShow();
            taskShowFront2.setRowHeaderText("�ѷ�������");
            TreeNode sendtask = new DefaultTreeNode(taskShowFront2, stlPower);
            getChildSendNode(sendtask);
        } catch (Exception e) {
            logger.error("��ʼ��ʧ��", e);
        }
    }

    //��ȡ���㵥��ĩ�˽ڵ�   ����������ڵ�
    private void getChildSendNode(TreeNode sendTask) throws Exception {
        List<TaskShow> taskListSend = taskService.initSendTaskShowList();
        for (TaskShow taskShowUnit:taskListSend) {
            new DefaultTreeNode(taskShowUnit, sendTask);
        }
    }

    private void getChildRecNode(TreeNode recTask) throws Exception {
        List<TaskShow> taskListRec = taskService.initRecTaskShowList();
        for (TaskShow taskShowUnit:taskListRec) {
            new DefaultTreeNode(taskShowUnit, recTask);
        }
    }

    public TreeNode getSelfTaskTreeRoot() {
        return selfTaskTreeRoot;
    }

    public void setSelfTaskTreeRoot(TreeNode selfTaskTreeRoot) {
        this.selfTaskTreeRoot = selfTaskTreeRoot;
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