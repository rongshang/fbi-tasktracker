package task.view.task;

import task.common.enums.EnumResType;
import task.repository.model.model_show.TaskShow;
import task.service.TaskService;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
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
public class StlPowerAction {
    private static final Logger logger = LoggerFactory.getLogger(StlPowerAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private TreeNode stlPowerRoot;
    private List<TaskShow> stlPowerList;

    @PostConstruct
    public void init() {
        //���������б�
        try {
            stlPowerRoot = new DefaultTreeNode("ROOT", null);
            TaskShow taskShow=new TaskShow();
            taskShow.setId("����Ȩ��");
            TreeNode stlPower= new DefaultTreeNode(taskShow, stlPowerRoot);
            stlPower.setExpanded(true);
            TaskShow taskShowFront1 = new TaskShow();
            taskShowFront1.setId("�ְ����Ƚ���");
            TreeNode subStl = new DefaultTreeNode(taskShowFront1, stlPower);
            TaskShow taskShowFront2 = new TaskShow();
            taskShowFront2.setId("����������");
            TreeNode subStlQ = new DefaultTreeNode(taskShowFront2, subStl);
            TaskShow taskShowFront3 = new TaskShow();
            taskShowFront3.setId("��������������");
            TreeNode subStlM = new DefaultTreeNode(taskShowFront3, subStl);
            TaskShow taskShowFront4 = new TaskShow();
            taskShowFront4.setId("���ý���");
            TreeNode subStlF = new DefaultTreeNode(taskShowFront4, subStl);
            TaskShow taskShowFront5 = new TaskShow();
            taskShowFront5.setId("�ܰ����Ƚ���");
            TreeNode tkStl = new DefaultTreeNode(taskShowFront5, stlPower);
            TaskShow taskShowFront6 = new TaskShow();
            taskShowFront6.setId("������ͳ�ƽ���");
            TreeNode tkStlEst = new DefaultTreeNode(taskShowFront6, tkStl);
            TaskShow taskShowFront7 = new TaskShow();
            taskShowFront7.setId("��������������");
            TreeNode tkStlMea = new DefaultTreeNode(taskShowFront7, tkStl);
            getChildNode(subStlQ,subStlM,subStlF,tkStlEst,tkStlMea);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    //��ȡ���㵥��ĩ�˽ڵ�   ����������ڵ�
    private TreeNode getChildNode(TreeNode subStlQ,TreeNode subStlM,TreeNode subStlF,TreeNode tkStlEst,TreeNode tkStlMea)
            throws Exception {
        TreeNode parentNode;
        stlPowerList=taskService.initRecentlyPowerTaskShowList();
        for (int i=0;i<stlPowerList.size();i++){
            TaskShow taskShow=new TaskShow();
            TaskShow taskShowTemp=stlPowerList.get(i);
            if(taskShowTemp.getType()!=null){
                if(taskShowTemp.getType().equals(EnumResType.RES_TYPE3.getCode())){
                    parentNode=subStlQ;
                }else
                if(taskShowTemp.getType().equals(EnumResType.RES_TYPE4.getCode())){
                    parentNode=subStlM;
                }else
                if(taskShowTemp.getType().equals(EnumResType.RES_TYPE8.getCode())){
                    parentNode=subStlF;
                }else
                if(taskShowTemp.getType().equals(EnumResType.RES_TYPE6.getCode())){
                    parentNode=tkStlEst;
                }else
                if(taskShowTemp.getType().equals(EnumResType.RES_TYPE7.getCode())){
                    parentNode=tkStlMea;
                }else
                {parentNode= null;}
            if(parentNode!=null) {
                taskShow.setPkid(taskShowTemp.getPkid());
                taskShow.setId(taskShowTemp.getId());
                taskShow.setType(taskShowTemp.getType());
                taskShow.setName(taskShowTemp.getName());
                taskShow.setSignPartBName(taskShowTemp.getSignPartBName());
                new DefaultTreeNode(taskShow, parentNode);
            }}
        }
        return  null;
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
        return stlPowerList;
    }

    public void setStlPowerList(List<TaskShow> stlPowerList) {
        this.stlPowerList = stlPowerList;
    }
}