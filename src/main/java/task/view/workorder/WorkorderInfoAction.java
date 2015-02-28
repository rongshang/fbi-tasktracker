package task.view.workorder;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import task.common.enums.EnumInputFinishFlag;
import task.repository.model.not_mybatis.WorkorderInfoShow;
import task.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
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
public class WorkorderInfoAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderInfoAction.class);
    @ManagedProperty(value = "#{workorderInfoService}")
    private WorkorderInfoService workorderInfoService;
    @ManagedProperty(value = "#{workorderItemService}")
    private WorkorderItemService workorderItemService;

    private WorkorderInfoShow workorderInfoShowQry;
    private WorkorderInfoShow workorderInfoShowSel;
    private WorkorderInfoShow workorderInfoShowAdd;
    private WorkorderInfoShow workorderInfoShowUpd;
    private WorkorderInfoShow workorderInfoShowDel;
    private List<WorkorderInfoShow> workorderInfoShowList;

    private String strSubmitType;
    private TreeNode root;
    /*任务跟踪显示标志*/
    private String strTaskTrackerFlag;

    @PostConstruct
    public void init() {
        try {
            initData();
            initTree();
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }
    public void initData() {
        try {
            this.workorderInfoShowList = new ArrayList<>();
            workorderInfoShowQry = new WorkorderInfoShow();
            workorderInfoShowSel = new WorkorderInfoShow();
            workorderInfoShowAdd = new WorkorderInfoShow();
            workorderInfoShowUpd = new WorkorderInfoShow();
            workorderInfoShowDel = new WorkorderInfoShow();
            strSubmitType = "";
            strTaskTrackerFlag="false";
        }catch (Exception e){
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
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
    public void initTree() {
        workorderInfoShowAdd = new WorkorderInfoShow();
        root = new DefaultTreeNode("Root", null);
        TreeNode node0 = new DefaultTreeNode("Node 0", root);

        TreeNode node00 = new DefaultTreeNode("Node 0.0", node0);
        TreeNode node01 = new DefaultTreeNode("Node 0.1", node0);

        node00.getChildren().add(new DefaultTreeNode("Node 0.0.0"));
        node00.getChildren().add(new DefaultTreeNode("Node 0.0.1"));
        node01.getChildren().add(new DefaultTreeNode("Node 0.1.0"));
    }

    public TreeNode getRoot() {
        return root;
    }

    public String onQueryAllAction(String strQryMsgOutPara) {
        try {
            this.workorderInfoShowList.clear();
            workorderInfoShowList =
                    workorderInfoService.getWorkorderShowInfoListByModelShow(workorderInfoShowQry);
            if(strQryMsgOutPara.equals("true"))  {
                if (workorderInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("工单信息查询失败", e);
            MessageUtil.addError("工单信息查询失败");
        }
        return null;
    }
    public String onQueryFinishAction(String strQryMsgOutPara) {
        try {
            this.workorderInfoShowList.clear();
            workorderInfoShowQry.setFinishFlag(EnumInputFinishFlag.INPUT_FINISH_FLAG1.getCode());
            workorderInfoShowList =
                    workorderInfoService.getWorkorderShowInfoListByModelShow(workorderInfoShowQry);
            if(strQryMsgOutPara.equals("true"))  {
                if (workorderInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("工单信息查询失败", e);
            MessageUtil.addError("工单信息查询失败");
        }
        return null;
    }

    public void initForAdd(){
        strSubmitType="Add";
        workorderInfoShowAdd = new WorkorderInfoShow();
        workorderInfoShowAdd.setId(workorderInfoService.getMaxNoPlusOne());
    }
    public void selectRecordAction(String strSubmitTypePara,WorkorderInfoShow workorderInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            // 查询
            if (strSubmitTypePara.equals("Sel")) {
                workorderInfoShowSel = (WorkorderInfoShow) BeanUtils.cloneBean(workorderInfoShowPara);
            }else if (strSubmitTypePara.equals("Upd")) {
                workorderInfoShowUpd = (WorkorderInfoShow) BeanUtils.cloneBean(workorderInfoShowPara);
            }else if (strSubmitTypePara.equals("Del")){
                workorderInfoShowDel = (WorkorderInfoShow) BeanUtils.cloneBean(workorderInfoShowPara);
            }else if(strSubmitTypePara.equals("Tra")){
                strTaskTrackerFlag="true";
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
     */
    private boolean submitPreCheck(WorkorderInfoShow workorderInfoShowPara) {
        if (StringUtils.isEmpty(workorderInfoShowPara.getId())) {
            MessageUtil.addError("请输入工单号！");
            return false;
        } else if (StringUtils.isEmpty(workorderInfoShowPara.getName())) {
            MessageUtil.addError("请输入工单名！");
            return false;
        } else if (StringUtils.isEmpty(workorderInfoShowPara.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
            return false;
        }
        else if (StringUtils.isEmpty(workorderInfoShowPara.getStartTime())) {
            MessageUtil.addError("请输入工单开始时间！");
            return false;
        } else if (StringUtils.isEmpty(workorderInfoShowPara.getEndTime())) {
            MessageUtil.addError("请输入工单截止时间！");
            return false;
        }
        return true;
    }
    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction() {
        if (strSubmitType.equals("Del")) {
            deleteRecordAction(workorderInfoShowDel);
            MessageUtil.addInfo("删除数据完成。");
        }else {
            if (strSubmitType.equals("Add")) {
                if (!submitPreCheck(workorderInfoShowAdd)) {
                    return;
                }
                addRecordAction(workorderInfoShowAdd);
                MessageUtil.addInfo("新增数据完成。");
                initForAdd();
            } else if (strSubmitType.equals("Upd")) {
                if (!submitPreCheck(workorderInfoShowUpd)) {
                    return;
                }
                updRecordAction(workorderInfoShowUpd);
                MessageUtil.addInfo("更新数据完成。");
            }
        }
        onQueryAllAction("false");
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    private void addRecordAction(WorkorderInfoShow workorderInfoShowPara) {
        try {
            workorderInfoService.insertRecord(workorderInfoShowPara);
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(WorkorderInfoShow workorderInfoShowPara) {
        try {
            workorderInfoService.updateRecord(workorderInfoShowPara);
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteRecordAction(WorkorderInfoShow workorderInfoShowPara) {
        try {
            int deleteRecordNumOfCttItem=
                    workorderItemService.deleteRecordByInfoPkid(workorderInfoShowPara.getPkid());
            int deleteRecordNumOfCtt=
                    workorderInfoService.deleteRecord(workorderInfoShowPara.getPkid());
            if (deleteRecordNumOfCtt<=0&&deleteRecordNumOfCttItem<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段 Start*/

    public WorkorderItemService getWorkorderItemService() {
        return workorderItemService;
    }

    public void setWorkorderItemService(WorkorderItemService workorderItemService) {
        this.workorderItemService = workorderItemService;
    }

    public WorkorderInfoService getWorkorderInfoService() {
        return workorderInfoService;
    }

    public void setWorkorderInfoService(WorkorderInfoService workorderInfoService) {
        this.workorderInfoService = workorderInfoService;
    }

    public WorkorderInfoShow getWorkorderInfoShowQry() {
        return workorderInfoShowQry;
    }

    public void setWorkorderInfoShowQry(WorkorderInfoShow workorderInfoShowQry) {
        this.workorderInfoShowQry = workorderInfoShowQry;
    }

    public WorkorderInfoShow getWorkorderInfoShowAdd() {
        return workorderInfoShowAdd;
    }

    public void setWorkorderInfoShowAdd(WorkorderInfoShow workorderInfoShowAdd) {
        this.workorderInfoShowAdd = workorderInfoShowAdd;
    }

    public WorkorderInfoShow getWorkorderInfoShowDel() {
        return workorderInfoShowDel;
    }

    public void setWorkorderInfoShowDel(WorkorderInfoShow workorderInfoShowDel) {
        this.workorderInfoShowDel = workorderInfoShowDel;
    }

    public List<WorkorderInfoShow> getWorkorderInfoShowList() {
        return workorderInfoShowList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public WorkorderInfoShow getWorkorderInfoShowUpd() {
        return workorderInfoShowUpd;
    }

    public void setWorkorderInfoShowUpd(WorkorderInfoShow workorderInfoShowUpd) {
        this.workorderInfoShowUpd = workorderInfoShowUpd;
    }

    public WorkorderInfoShow getWorkorderInfoShowSel() {
        return workorderInfoShowSel;
    }

    public void setWorkorderInfoShowSel(WorkorderInfoShow workorderInfoShowSel) {
        this.workorderInfoShowSel = workorderInfoShowSel;
    }

    public String getStrTaskTrackerFlag() {
        return strTaskTrackerFlag;
    }

    public void setStrTaskTrackerFlag(String strTaskTrackerFlag) {
        this.strTaskTrackerFlag = strTaskTrackerFlag;
    }
/*智能字段 End*/
}
