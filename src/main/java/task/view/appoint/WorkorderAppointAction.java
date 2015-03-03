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
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private WorkorderInfoShow workorderInfoShow;
    private WorkorderInfo workorderInfo;
    //workorderAppointOperMng.xhtml(工单指派页面)选中多条数据
    private WorkorderInfo[] selectedWorkorderInfo;
    //workorderAppointOperMng.xhtml(工单指派页面)显示用
    List<WorkorderInfo> workorderInfos = null;
    private List<SelectItem> selectItems;
    private Map<String,String> cities = new HashMap<String, String>();
    private WorkorderAppoint workorderAppoint;

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
        workorderInfoShow = new WorkorderInfoShow();
        List<DeptOperShow> deptOperShowList = workorderAppointService.getDeptOper();
        String deptId="######";
        selectItems = new ArrayList<SelectItem>();
        //map key是部门value是部门的人
        Map<String,List<SelectItem>> map = new HashMap<String,List<SelectItem>>();
        SelectItemGroup selectItemGroup=null;
        List<SelectItem> selectItemList = null;
        for (DeptOperShow deptOperShowlist :deptOperShowList ){
            if(!deptOperShowlist.getDeptId().equals(deptId)){
                selectItemList = new ArrayList<SelectItem>();
                //selectItemList.add(new SelectItem(deptOperShowlist.getPkid(),deptOperShowlist.getOperName()));
               // map.put(deptOperShowlist.getDeptName(),selectItemList);
            }else{
                selectItemList =(List<SelectItem>)map.get(deptOperShowlist.getDeptName());
            }
            selectItemList.add(new SelectItem(deptOperShowlist.getPkid(),deptOperShowlist.getOperName()));
            map.put(deptOperShowlist.getDeptName(),selectItemList);
            deptId = deptOperShowlist.getDeptId();
        }

        for(Map.Entry<String,List<SelectItem>> entry: map.entrySet()) {
            //部门
            selectItemGroup = new SelectItemGroup(entry.getKey());
            //部门下的哪些人
            selectItemGroup.setSelectItems((SelectItem[])entry.getValue().toArray(new SelectItem[0]));
            selectItems.add(selectItemGroup);
        }
//        SelectItemGroup g1 = new SelectItemGroup("German Cars");
//        g1.setSelectItems( new SelectItem[] {new SelectItem("111", "胡珍洋"), new SelectItem("Mercedes", "于向阳"), new SelectItem("Volkswagen", "张自平")});
//        selectItems = new ArrayList<SelectItem>();
//        selectItems.add(g1);
    }

    /***
     * atuo: huzy
     * 根据工单ID或者工单名获取该条件的工单信息
     * param:orderId(工单id),orderName(工单名)
     * @return List<WorkorderInfo>
     */
    public void getWorkorderInfoByIdOrName(){
        try{
            //1  录入完成
            workorderInfoShow.setFinishFlag(EnumArchivedFlag.ARCHIVED_FLAG1.getCode());
            //0 未删除
            workorderInfoShow.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
            workorderInfos = workorderInfoService.getWorkorderInfoListByModelShow(workorderInfoShow);
        }catch (Exception e){
            logger.info("WorkorderAssignAction类中的getWorkorderInfoByPkIdOrName异常:"+e.toString());
        }
    }

    /**
     * atuo: huzy
     * @return String 路径
     */
    public String getURL(){
       return null;
    }


    public void initTree(WorkorderInfoShow workorderInfoShowPara) {
        WorkorderAppointShow workorderAppointShowPara=new WorkorderAppointShow();
        workorderAppointShowPara.setInfoPkid(workorderInfoShowPara.getPkid());
        workorderAppointShowPara.setFirstAppointFlag(EnumFirstAppointFlag.FIRST_APPOINT_FLAG0.getCode());
        List<WorkorderAppointShow> workorderAppointShowListTemp=
                workorderAppointService.getWorkorderAppointShowListByModelShow(workorderAppointShowPara);
        if(workorderAppointShowListTemp!=null&&workorderAppointShowListTemp.size()>0) {
            WorkorderAppointShow workorderAppointShowTemp=workorderAppointShowListTemp.get(0);
            WorkorderAppointShow workorderAppointShow_TreeNode=new WorkorderAppointShow();
            workorderAppointShow_TreeNode.setRecvTaskPartPkid(workorderAppointShowTemp.getSendTaskPartPkid());
            workorderAppointShow_TreeNode.setRecvTaskPartName(workorderAppointShowTemp.getSendTaskPartName());
            workorderAppointShow_TreeNode.setStrTreeNodeContent(workorderAppointShow_TreeNode.getRecvTaskPartName());
            root = new DefaultTreeNode(workorderAppointShow_TreeNode, null);
            root.setExpanded(true);
            recursiveTreeNode(workorderInfoShowPara.getPkid(),workorderAppointShow_TreeNode.getRecvTaskPartPkid(),root);

            strTaskTrackerFlag="false";
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveTreeNode(String strInfoPkidPara,String strSendTaskPartPkidPara,TreeNode parentNode){
        WorkorderAppointShow workorderAppointShowPara = new WorkorderAppointShow();
        workorderAppointShowPara.setInfoPkid(strInfoPkidPara);
        workorderAppointShowPara.setSendTaskPartPkid(strSendTaskPartPkidPara);
        List<WorkorderAppointShow> workorderAppointShowListTemp =
                workorderAppointService.getWorkorderAppointShowListByModelShow(workorderAppointShowPara);
        for (WorkorderAppointShow workorderAppointShowUnit : workorderAppointShowListTemp) {
            workorderAppointShowUnit.setStrTreeNodeContent(
                    workorderAppointShowUnit.getRecvTaskPartName()+"("+workorderAppointShowUnit.getRecvTaskFinishFlagName()+")");
            TreeNode childNodeTemp = new DefaultTreeNode(workorderAppointShowUnit, parentNode);
            childNodeTemp.setExpanded(true);
            recursiveTreeNode(strInfoPkidPara,workorderAppointShowUnit.getRecvTaskPartPkid(),childNodeTemp);
        }
    }

    public void selectRecordAction(WorkorderInfoShow workorderInfoShowPara) {
        try {
            initTree(workorderInfoShowPara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
            logger.info("WorkorderAppointAction类中的selectRecordAction异常:"+e.toString());
        }
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

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }

    public WorkorderAppoint getWorkorderAppoint() {
        return workorderAppoint;
    }

    public void setWorkorderAppoint(WorkorderAppoint workorderAppoint) {
        this.workorderAppoint = workorderAppoint;
    }
}
