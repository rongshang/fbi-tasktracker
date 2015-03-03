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
 * Time: 下午4:53
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
        //整个任务列表
        try {
            selfTaskTreeRoot = new DefaultTreeNode("ROOT", null);
            TaskShow taskShow = new TaskShow();
            taskShow.setRowHeaderText("工单任务");
            TreeNode srTask = new DefaultTreeNode(taskShow, selfTaskTreeRoot);
            srTask.setExpanded(true);
            TaskShow taskShowFront1 = new TaskShow();
            taskShowFront1.setRowHeaderText("待执行任务");
            TreeNode todotask = new DefaultTreeNode(taskShowFront1, srTask);
            String todoSize = getChildTodoNode(todotask);
            if(!todoSize.equals("0")){
                taskShowFront1.setRowHeaderText("待执行任务("+todoSize+")");
            }
            TaskShow taskShowFront2 = new TaskShow();
            taskShowFront2.setRowHeaderText("已执行任务");
            TreeNode donetask = new DefaultTreeNode(taskShowFront2, srTask);
            String doneSize =  getChildDoneNode(donetask);
            if(!doneSize.equals("0")){
                taskShowFront2.setRowHeaderText("已执行任务("+doneSize+")");
            }
        } catch (Exception e) {
            logger.error("初始化失败", e);
        }
    }

    //获取结算单最末端节点   传入参数父节点
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