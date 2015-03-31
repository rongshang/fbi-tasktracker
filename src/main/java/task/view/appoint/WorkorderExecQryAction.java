package task.view.appoint;

import skyline.util.MessageUtil;
import task.common.UrlCtrl;
import task.common.enums.EnumRecvTaskExecFlag;
import task.repository.model.WorkorderAppoint;
import task.repository.model.not_mybatis.TaskShow;
import task.service.TaskService;
import task.service.WorkorderAppointService;
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
public class WorkorderExecQryAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderExecQryAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;
    @ManagedProperty(value = "#{workorderAppointService}")
    private WorkorderAppointService workorderAppointService;

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
            taskShowFront2.setRowHeaderText("ִ��������");
            TreeNode doingtask = new DefaultTreeNode(taskShowFront2, srTask);
            String doingSize =  getChildDoingNode(doingtask);
            if(!doingSize.equals("0")){
                taskShowFront2.setRowHeaderText("ִ��������("+doingSize+")");
            }
            TaskShow taskShowFront3 = new TaskShow();
            taskShowFront3.setRowHeaderText("��ִ������");
            TreeNode donetask = new DefaultTreeNode(taskShowFront3, srTask);
            String doneSize =  getChildDoneNode(donetask);
            if(!doneSize.equals("0")){
                taskShowFront3.setRowHeaderText("��ִ������("+doneSize+")");
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
                taskShow.setWorkorderAppointPkid(taskShowTemp.getWorkorderAppointPkid());
                taskShow.setRecvTaskExecFlag(EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG2.getCode());
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
                taskShow.setWorkorderAppointPkid(taskShowTemp.getWorkorderAppointPkid());
                taskShow.setRecvTaskExecFlag(EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG0.getCode());
                taskShow.setCreatedTime(taskShowTemp.getCreatedTime());
                taskShow.setRecvTaskPartName(taskShowTemp.getRecvTaskPartName());
                new DefaultTreeNode(taskShow, todoTask);
            }
        }
        return resnum;
    }
    private String getChildDoingNode(TreeNode doingTask)
            throws Exception {
        List<TaskShow> taskListTodo = taskService.initDoingTaskShowList();
        String resnum = "0";
        if (taskListTodo.size() > 0) {
            resnum=String.valueOf(taskListTodo.size());
            for (int i = 0; i < taskListTodo.size(); i++) {
                TaskShow taskShow = new TaskShow();
                TaskShow taskShowTemp = taskListTodo.get(i);
                taskShow.setWorkorderInfoPkid(taskShowTemp.getWorkorderInfoPkid());
                taskShow.setWorkorderInfoId(taskShowTemp.getWorkorderInfoId());
                taskShow.setWorkorderInfoName(taskShowTemp.getWorkorderInfoName());
                taskShow.setWorkorderAppointPkid(taskShowTemp.getWorkorderAppointPkid());
                taskShow.setRecvTaskExecFlag(EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG1.getCode());
                taskShow.setCreatedTime(taskShowTemp.getCreatedTime());
                taskShow.setRecvTaskPartName(taskShowTemp.getRecvTaskPartName());
                new DefaultTreeNode(taskShow, doingTask);
            }
        }
        return resnum;
    }
    public void onClickDoingAction(String pkid){
       try{
            if (!(pkid.equals("")) ||( pkid !=null )) {
                // ״̬��־��
                WorkorderAppoint workorderAppoint = workorderAppointService.getWorkorderAppointByPkid(pkid);
                workorderAppoint.setRecvTaskExecFlag(EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG1.getCode());
                workorderAppointService.updateBypkid(workorderAppoint);
                MessageUtil.addInfo("�����ɹ���");
                init();
            }
        }catch(Exception e){
            logger.error("����ʧ��", e);
         MessageUtil.addError("����ʧ��");
        }
    return  ;
    }
    public void onClickDoneAction(String pkid){
        try{
            if ((!pkid.equals("")) ||( pkid !=null )) {
                // ״̬��־��
                WorkorderAppoint workorderAppoint = workorderAppointService.getWorkorderAppointByPkid(pkid);
                workorderAppoint.setRecvTaskExecFlag(EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG2.getCode());
                workorderAppointService.updateBypkid(workorderAppoint);
                MessageUtil.addInfo("�����ɹ���");
                init();
            }
        }catch(Exception e){
            logger.error("����ʧ��", e);
            MessageUtil.addError("����ʧ��");
        }
        return  ;
    }
    public void onClickUndoAction(String pkid){
        try{
            if ((pkid != "") ||( pkid !=null )) {
                // ״̬��־��
                WorkorderAppoint workorderAppoint = workorderAppointService.getWorkorderAppointByPkid(pkid);
                workorderAppoint.setRecvTaskExecFlag(EnumRecvTaskExecFlag.RECV_TASK_EXEC_FLAG0.getCode());
                workorderAppointService.updateBypkid(workorderAppoint);
                MessageUtil.addInfo("�����ɹ���");
                init();
            }
        }catch(Exception e){
            logger.error("����ʧ��", e);
            MessageUtil.addError("����ʧ��");
        }
        return  ;
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

    public WorkorderAppointService getWorkorderAppointService() {
        return workorderAppointService;
    }

    public void setWorkorderAppointService(WorkorderAppointService workorderAppointService) {
        this.workorderAppointService = workorderAppointService;
    }
}