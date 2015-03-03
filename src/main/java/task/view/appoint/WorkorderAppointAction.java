package task.view.appoint;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import task.common.enums.EnumArchivedFlag;
import task.common.enums.EnumFirstAppointFlag;
import task.common.enums.EnumInputFinishFlag;
import task.repository.model.WorkorderAppoint;
import task.repository.model.WorkorderInfo;
import task.repository.model.not_mybatis.DeptOperShow;
import task.repository.model.not_mybatis.WorkorderAppointShow;
import task.repository.model.not_mybatis.WorkorderInfoShow;
import task.service.DeptOperService;
import task.service.WorkorderAppointService;
import task.service.WorkorderInfoService;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/13.
 * atuo: huzy
 * 工单指派
 */

@ManagedBean
@ViewScoped
public class WorkorderAppointAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderAppointAction.class);
    @ManagedProperty(value = "#{workorderAppointService}")
    private WorkorderAppointService workorderAppointService;
    @ManagedProperty(value = "#{workorderInfoService}")
    private WorkorderInfoService workorderInfoService;
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;

    private TreeNode deptOperShowRoot;
    private WorkorderInfoShow workorderInfoShow;

    //workorderAppointOperMng.xhtml(工单指派页面)选中多条数据
    private WorkorderInfo[] selectedWorkorderInfo;

    //workorderAppointOperMng.xhtml(工单指派页面)显示用
    List<WorkorderInfoShow> workorderInfoShowList = null;

    /*任务跟踪显示标志*/
    private String strTaskTrackerFlag;

    public void init(){
        strTaskTrackerFlag="true";
    }

    /**
     * atuo: huzy
     */
    @PostConstruct
    public void initTree() {
        DeptOperShow deptOperShowTemp = new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("机构人员信息");
        deptOperShowTemp.setType("0");
        deptOperShowRoot=deptOperService.getDeptOperTreeNode(deptOperShowTemp);
    }

    /**
     * atuo: huzy
     * @return String 路径
     */
    public String getURL(){
        return "workorderAppointOperQry";
    }

    public void onNodeSelect(SelectEvent event) {
        TreeNode node = (TreeNode) event.getObject();
        //populate if not already loaded
        if(node.getChildren().isEmpty()) {
            //Object label = node.getLabel();
        }
    }

    public void onNodeDblselect(SelectEvent event) {
        //this.selectedNode = (TreeNode) event.getObject();
    }

    public List<WorkorderInfoShow> getWorkorderInfoShowList() {
        return workorderInfoShowList;
    }

    public void setWorkorderInfoShowList(List<WorkorderInfoShow> workorderInfoShowList) {
        this.workorderInfoShowList = workorderInfoShowList;
    }

    public void setWorkorderAppointService(WorkorderAppointService workorderAppointService) {
        this.workorderAppointService = workorderAppointService;
    }

    public WorkorderInfoService getWorkorderInfoService() {
        return workorderInfoService;
    }

    public void setWorkorderInfoService(WorkorderInfoService workorderInfoService) {
        this.workorderInfoService = workorderInfoService;
    }

    public WorkorderAppointService getWorkorderAppointService() {
        return workorderAppointService;
    }

    public TreeNode getDeptOperShowRoot() {
        return deptOperShowRoot;
    }

    public void setDeptOperShowRoot(TreeNode deptOperShowRoot) {
        this.deptOperShowRoot = deptOperShowRoot;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public WorkorderInfo[] getSelectedWorkorderInfo() {
        return selectedWorkorderInfo;
    }

    public void setSelectedWorkorderInfo(WorkorderInfo[] selectedWorkorderInfo) {
        this.selectedWorkorderInfo = selectedWorkorderInfo;
    }

    public String getStrTaskTrackerFlag() {
        return strTaskTrackerFlag;
    }

    public void setStrTaskTrackerFlag(String strTaskTrackerFlag) {
        this.strTaskTrackerFlag = strTaskTrackerFlag;
    }

    public WorkorderInfoShow getWorkorderInfoShow() {
        return workorderInfoShow;
    }

    public void setWorkorderInfoShow(WorkorderInfoShow workorderInfoShow) {
        this.workorderInfoShow = workorderInfoShow;
    }
}
