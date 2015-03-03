package task.view.task;

import task.repository.model.not_mybatis.TaskShow;
import task.service.TaskService;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.common.enums.EnumTaskFinishFlag;

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
public class WorkorderExecuteQryAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderExecuteQryAction.class);
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
            TreeNode srTask = new DefaultTreeNode(taskShow, selfTaskTreeRoot);
            srTask.setExpanded(true);
            TaskShow taskShowFront1 = new TaskShow();
            taskShowFront1.setRowHeaderText("��ִ������");
            TreeNode todotask = new DefaultTreeNode(taskShowFront1, srTask);
            String todoSize = getChildTodoNode(todotask);
            if(!todoSize.equals("0")){
                taskShowFront1.setRowHeaderText("��ִ������("+todoSize+")");
            }
            TaskShow taskShowFront2 = new TaskShow();
            taskShowFront2.setRowHeaderText("��ִ������");
            TreeNode donetask = new DefaultTreeNode(taskShowFront2, srTask);
            String doneSize =  getChildDoneNode(donetask);
            if(!doneSize.equals("0")){
                taskShowFront2.setRowHeaderText("��ִ������("+doneSize+")");
            }
        } catch (Exception e) {
            logger.error("��ʼ��ʧ��", e);
        }
    }

    //��ȡ���㵥��ĩ�˽ڵ�   ����������ڵ�
    private String getChildDoneNode(TreeNode doneTask) throws Exception {
        List<TaskShow> taskListDone = taskService.initDoneTaskShowList();
        String resnum = "0";
        if (taskListDone.size() > 0) {
            resnum=String.valueOf(taskListDone.size());
            for (int i = 0; i < taskListDone.size(); i++) {
                TaskShow taskShow = new TaskShow();
                TaskShow taskShowTemp = taskListDone.get(i);
                taskShow.setWorkorderInfoPkid(taskShowTemp.getWorkorderInfoPkid());
                taskShow.setWorkorderInfoId(taskShowTemp.getWorkorderInfoId());
                taskShow.setWorkorderInfoName(taskShowTemp.getWorkorderInfoName());
//                taskShow.setRecvTaskFinishFlag(EnumTaskFinishFlag.getValueByKey(taskShowTemp.getRecvTaskFinishFlag()).getTitle());
                taskShow.setCreatedTime(taskShowTemp.getCreatedTime());
                taskShow.setRecvTaskPartName(taskShowTemp.getRecvTaskPartName());
                new DefaultTreeNode(taskShow, doneTask);
            }
        }
        return resnum;

    }

    private String getChildTodoNode(TreeNode todoTask)
            throws Exception {
        List<TaskShow> taskListTodo = taskService.initTodoTaskShowList();
        String resnum = "0";
        if (taskListTodo.size() > 0) {
            resnum=String.valueOf(taskListTodo.size());
            for (int i = 0; i < taskListTodo.size(); i++) {
                TaskShow taskShow = new TaskShow();
                TaskShow taskShowTemp = taskListTodo.get(i);
                taskShow.setWorkorderInfoPkid(taskShowTemp.getWorkorderInfoPkid());
                taskShow.setWorkorderInfoId(taskShowTemp.getWorkorderInfoId());
                taskShow.setWorkorderInfoName(taskShowTemp.getWorkorderInfoName());
//                taskShow.setRecvTaskFinishFlag(EnumTaskFinishFlag.getValueByKey(taskShowTemp.getRecvTaskFinishFlag()).getTitle());
                taskShow.setCreatedTime(taskShowTemp.getCreatedTime());
                taskShow.setRecvTaskPartName(taskShowTemp.getRecvTaskPartName());
                new DefaultTreeNode(taskShow, todoTask);
            }
        }
        return resnum;
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