package task.view.operFuncRes;

import task.common.enums.EnumResType;
import task.common.enums.EnumFlowStatus;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeSelectEvent;
import skyline.util.JxlsManager;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import task.repository.model.WorkorderInfo;
import task.repository.model.model_show.*;
import task.service.*;
import jxl.write.WriteException;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@ManagedBean
@ViewScoped
public class OperFuncBusiResMngAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperFuncBusiResMngAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{workorderInfoService}")
    private WorkorderInfoService workorderInfoService;
    @ManagedProperty(value = "#{workorderItemService}")
    private WorkorderItemService workorderItemService;

    private List<SelectItem> taskFunctionList;
    private List<DeptOperShow> deptOperShowSeledList;

    private List<OperFuncResShow> operFuncResShowFowExcelList;
    private Map beansMap;

    private WorkorderInfoShow workorderInfoShowSel;
    private WorkorderInfoShow workorderInfoShowAdd;
    private WorkorderInfoShow workorderInfoShowUpd;
    private WorkorderInfoShow workorderInfoShowDel;

    private List<SelectItem> esInitCttList;
    private List<WorkorderInfoShow> workorderInfoShowList;
    //workorder tree
    private TreeNode resRoot;
    private TreeNode deptOperRoot;
    private TreeNode currentSelectedNode;
    private TreeNode currentSelectedResNode;
    private TreeNode lastSelectedResNode;

    private String strBtnRender;
    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        workorderInfoShowAdd =new WorkorderInfoShow();
        workorderInfoShowUpd =new WorkorderInfoShow();
        workorderInfoShowDel =new WorkorderInfoShow();
        esInitCttList = new ArrayList<>();
        workorderInfoShowList = new ArrayList<>();
        deptOperShowSeledList = new ArrayList<>();
        operFuncResShowFowExcelList= new ArrayList<>();
        strBtnRender="false";
        // 资源-用户-功能
        initRes();
        initFuncListByResType(resRoot);
        initDeptOper();
        beansMap.put("operFuncResShowFowExcelList", operFuncResShowFowExcelList);
    }
    private void initRes(){
        OperFuncResShow operFuncResShowTemp=new OperFuncResShow();
        operFuncResShowTemp.setResPkid("ROOT");
        operFuncResShowTemp.setResName("资源信息");
        resRoot = new DefaultTreeNode("ROOT", null);
        TreeNode node0 = new DefaultTreeNode(operFuncResShowTemp,resRoot);
        try {
            //recursiveResTreeNode("ROOT", node0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        node0.setExpanded(true);
    }
    private void initDeptOper(){
        deptOperRoot = new DefaultTreeNode("ROOT", null);
        DeptOperShow deptOperShowTemp =new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("机构人员信息");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptOperRoot);
        recursiveOperTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }

    private void recursiveOperTreeNode(String strParentPkidPara,TreeNode parentNode){
        List<DeptOperShow> operResShowListTemp=
                deptOperService.selectDeptAndOperRecords(strParentPkidPara);
        for (int i=0;i<operResShowListTemp.size();i++){
            TreeNode childNode = new DefaultTreeNode(operResShowListTemp.get(i), parentNode);
            recursiveOperTreeNode(operResShowListTemp.get(i).getPkid(), childNode);
        }
    }

    public void recursiveResTreeNode(TreeNode treeNodePara){
        treeNodePara.setExpanded(false);
        if (treeNodePara.getChildCount()!=0){
            for (int i=0;i<treeNodePara.getChildCount();i++){
                recursiveResTreeNode(treeNodePara.getChildren().get(i));
            }
        }
    }

    public void initFuncListByResType(TreeNode treeNodePara) {
        if (!treeNodePara.getData().toString().equals("ROOT")){
            OperFuncResShow operFuncResShowTemp = ((OperFuncResShow) treeNodePara.getData());
            if (EnumResType.RES_TYPE3.getCode().equals(operFuncResShowTemp.getResType())
                    || EnumResType.RES_TYPE4.getCode().equals(operFuncResShowTemp.getResType())) {
                operFuncResShowTemp.setIsMng(EnumFlowStatus.FLOW_STATUS0.getCode());
                operFuncResShowTemp.setIsCheck(EnumFlowStatus.FLOW_STATUS1.getCode());
                operFuncResShowTemp.setIsDoubleCheck(EnumFlowStatus.FLOW_STATUS2.getCode());
                operFuncResShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            } else if (EnumResType.RES_TYPE5.getCode().equals(operFuncResShowTemp.getResType())) {
                operFuncResShowTemp.setIsApprove(EnumFlowStatus.FLOW_STATUS3.getCode());
                operFuncResShowTemp.setIsAccount(EnumFlowStatus.FLOW_STATUS4.getCode());
                operFuncResShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            } else {
                operFuncResShowTemp.setIsMng(EnumFlowStatus.FLOW_STATUS0.getCode());
                operFuncResShowTemp.setIsCheck(EnumFlowStatus.FLOW_STATUS1.getCode());
                operFuncResShowTemp.setIsDoubleCheck(EnumFlowStatus.FLOW_STATUS2.getCode());
                operFuncResShowTemp.setIsApprove(EnumFlowStatus.FLOW_STATUS3.getCode());
                operFuncResShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            }
        }
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                initFuncListByResType(treeNodePara.getChildren().get(i));
            }
        }
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        recursiveResTreeNode(event.getTreeNode());
    }

    public void findSelectedNode(
            OperFuncResShow operFuncResShowPara,
            TreeNode treeNodePara,
            String strSubmitTypePara) {
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                TreeNode treeNodeTemp = treeNodePara.getChildren().get(i);
                if (operFuncResShowPara == treeNodeTemp.getData()) {
                    if ("Add".equals(strSubmitTypePara)){
                        currentSelectedNode = treeNodeTemp;
                    }else if ("Upd".equals(strSubmitTypePara)||"Del".equals(strSubmitTypePara)||"Sel".equals(strSubmitTypePara)){
                        currentSelectedNode=treeNodeTemp.getParent();
                    }
                    return;
                }
                findSelectedNode(operFuncResShowPara, treeNodeTemp,strSubmitTypePara);
            }
        }
    }

    public void selectRecordAction(String strSubmitTypePara,OperFuncResShow operFuncResShowPara) {
        try {
            findSelectedNode(operFuncResShowPara,resRoot,strSubmitTypePara);
            if (strSubmitTypePara.equals("Add")) {
                workorderInfoShowAdd = new WorkorderInfoShow();
                if(operFuncResShowPara.getResPkid().equals("ROOT")) {
                    workorderInfoShowAdd.setCttType(EnumResType.RES_TYPE0.getCode());
                    workorderInfoShowAdd.setParentPkid("ROOT");
                }else if(operFuncResShowPara.getResType().equals(EnumResType.RES_TYPE0.getCode())) {
                    workorderInfoShowAdd.setCttType(EnumResType.RES_TYPE1.getCode());
                    workorderInfoShowAdd.setParentPkid(operFuncResShowPara.getResPkid());
                }else if(operFuncResShowPara.getResType().equals(EnumResType.RES_TYPE1.getCode())) {
                    workorderInfoShowAdd.setCttType(EnumResType.RES_TYPE2.getCode());
                    workorderInfoShowAdd.setParentPkid(operFuncResShowPara.getResPkid());
                }
                workorderInfoShowAdd.setId(setMaxNoPlusOne(workorderInfoShowAdd.getCttType()));
            } else if (strSubmitTypePara.equals("Upd")){
                workorderInfoShowUpd = fromResModelShowToCttInfoShow(operFuncResShowPara);
            } else if (strSubmitTypePara.equals("Del")) {
                workorderInfoShowDel = fromResModelShowToCttInfoShow(operFuncResShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public void selectRecordAction(String strSubmitTypePara, OperFuncResShow operFuncResShowPara, String strResFlowStatus) {
        try {
            findSelectedNode(operFuncResShowPara, resRoot, strSubmitTypePara);
            if (strSubmitTypePara.equals("Sel")) {
                workorderInfoShowSel = fromResModelShowToCttInfoShow(operFuncResShowPara);
                workorderInfoShowSel.setFlowStatus(strResFlowStatus);
                initDeptOper();
                deptOperShowSeledList.clear();
                /*OperResShow operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(operFuncResShowPara.getResType());
                operResShowTemp.setInfoPkid(operFuncResShowPara.getResPkid());
                operResShowTemp.setFlowStatus(strResFlowStatus);
                List<OperResShow> operResShowListTemp = operResService.selectOperaResRecordsByModelShow(operResShowTemp);
                if (operResShowListTemp.size() > 0) {
                    recursiveOperTreeNodeForFuncChange(deptOperRoot, operResShowListTemp);
                }*/
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    public void selOperRecordAction(DeptOperShow deptOperShowPara){
        if (deptOperShowPara.getIsSeled()){
            deptOperShowSeledList.add(deptOperShowPara);
        }else{
            deptOperShowSeledList.remove(deptOperShowPara);
        }
    }

    public String setMaxNoPlusOne(String strResType) {
        Integer intTemp;
        String strType = null;
        if (EnumResType.RES_TYPE0.getCode().equals(strResType)){
            strType="TKCTT";
        }else if (EnumResType.RES_TYPE1.getCode().equals(strResType)){
            strType="CSTPL";
        }else if (EnumResType.RES_TYPE2.getCode().equals(strResType)){
            strType="SUBCTT";
        }
        String strMaxId = workorderInfoService.getStrMaxCttId(strResType);
        if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
            strMaxId = strType + ToolUtil.getStrToday() + "001";
        } else {
            if (strMaxId.length() > 3) {
                String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                if (ToolUtil.strIsDigit(strTemp)) {
                    intTemp = Integer.parseInt(strTemp);
                    intTemp = intTemp + 1;
                    strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                } else {
                    strMaxId += "001";
                }
            }
        }
        return strMaxId;
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction(String strSubmitTypePara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                if (!submitPreCheck(workorderInfoShowAdd)) {
                    MessageUtil.addError("请输入名称！");
                    return;
                }
                WorkorderInfoShow workorderInfoShowTemp =new WorkorderInfoShow();
                workorderInfoShowTemp.setCttType(workorderInfoShowAdd.getCttType());
                workorderInfoShowTemp.setName(workorderInfoShowAdd.getName());
                if (workorderInfoService.getListByModelShow(workorderInfoShowTemp).size()>0) {
                    MessageUtil.addError("该记录已存在，请重新录入！");
                    return;
                } else {
                    workorderInfoService.insertRecord(workorderInfoShowAdd);
                    MessageUtil.addInfo("新增数据完成。");
					String strCttTypeTemp= workorderInfoShowAdd.getCttType();
					String strParentPkidTemp= workorderInfoShowAdd.getParentPkid();
                    workorderInfoShowAdd = new WorkorderInfoShow();
					workorderInfoShowAdd.setCttType(strCttTypeTemp);
                    workorderInfoShowAdd.setParentPkid(strParentPkidTemp);
                    workorderInfoShowAdd.setId(setMaxNoPlusOne(workorderInfoShowAdd.getCttType()));
                }
            } else if (strSubmitTypePara.equals("Upd")) {
                if (!submitPreCheck(workorderInfoShowUpd)) {
                    MessageUtil.addError("请输入名称！");
                    return;
                }
                WorkorderInfo workorderInfoTemp = workorderInfoService.getCttInfoByPkId(workorderInfoShowUpd.getPkid());
                workorderInfoTemp.setName(workorderInfoShowUpd.getName());
                workorderInfoService.updateRecord(workorderInfoTemp);
                MessageUtil.addInfo("更新数据完成。");
            } else if (strSubmitTypePara.equals("Del")) {
                if (!submitPreCheck(workorderInfoShowDel)) {
                    MessageUtil.addError("该记录已被删除！");
                    return;
                }
                //MessageUtil.addInfo(operResService.deleteResRecord(workorderInfoShowDel));
            } else if (strSubmitTypePara.equals("Power")) {
                /*OperRes operResTemp = new OperRes();
                operResTemp.setInfoType(workorderInfoShowSel.getCttType());
                operResTemp.setInfoPkid(workorderInfoShowSel.getPkid());
                operResTemp.setFlowStatus(workorderInfoShowSel.getFlowStatus());
                operResService.deleteRecord(operResTemp);
                for (DeptOperShow deptOperShowUnit:deptOperShowSeledList) {
                    operResTemp = new OperRes();
                    operResTemp.setOperPkid(deptOperShowUnit.getPkid());
                    operResTemp.setInfoType(workorderInfoShowSel.getCttType());
                    operResTemp.setInfoPkid(workorderInfoShowSel.getPkid());
                    operResTemp.setFlowStatus(workorderInfoShowSel.getFlowStatus());
                    operResTemp.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
                    operResTemp.setType("business");
                    operResService.insertRecord(operResTemp);
                }*/
                MessageUtil.addInfo("权限添加成功!");
            }
            initRes();
            initFuncListByResType(resRoot);
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    private WorkorderInfoShow fromResModelShowToCttInfoShow(OperFuncResShow operFuncResShowPara){
        WorkorderInfoShow workorderInfoShowTemp =new WorkorderInfoShow();
        workorderInfoShowTemp.setCttType(operFuncResShowPara.getResType());
        workorderInfoShowTemp.setPkid(operFuncResShowPara.getResPkid());
        workorderInfoShowTemp.setName(operFuncResShowPara.getResName());
        return workorderInfoShowTemp;
    }

    private boolean submitPreCheck(WorkorderInfoShow workorderInfoShowPara) {
        if ("".equals(ToolUtil.getStrIgnoreNull(workorderInfoShowPara.getName()))){
            return false;
        }
        return true;
    }

    public String onExportExcel()throws IOException, WriteException {
        if (this.operFuncResShowFowExcelList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "人员权限资源分配表-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"operFuncRes.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }

    public void onRowSelect(NodeSelectEvent event) {
        currentSelectedResNode=event.getTreeNode();
        if (lastSelectedResNode==null){
            lastSelectedResNode=currentSelectedResNode;
        }else {
            ((OperFuncResShow)lastSelectedResNode.getData()).setIsActived("false");
            lastSelectedResNode=currentSelectedResNode;
        }
        ((OperFuncResShow)currentSelectedResNode.getData()).setIsActived("true");
        strBtnRender="true";
    }
    /*智能字段 Start*/
    public WorkorderInfoService getWorkorderInfoService() {
        return workorderInfoService;
    }

    public void setWorkorderInfoService(WorkorderInfoService workorderInfoService) {
        this.workorderInfoService = workorderInfoService;
    }

    public WorkorderItemService getWorkorderItemService() {
        return workorderItemService;
    }

    public void setWorkorderItemService(WorkorderItemService workorderItemService) {
        this.workorderItemService = workorderItemService;
    }

    public List<SelectItem> getTaskFunctionList() {
        return taskFunctionList;
    }

    public void setTaskFunctionList(List<SelectItem> taskFunctionList) {
        this.taskFunctionList = taskFunctionList;
    }

    public List<SelectItem> getEsInitCttList() {
        return esInitCttList;
    }

    public void setEsInitCttList(List<SelectItem> esInitCttList) {
        this.esInitCttList = esInitCttList;
    }

    public List<WorkorderInfoShow> getWorkorderInfoShowList() {
        return workorderInfoShowList;
    }

    public void setWorkorderInfoShowList(List<WorkorderInfoShow> workorderInfoShowList) {
        this.workorderInfoShowList = workorderInfoShowList;
    }

    public TreeNode getResRoot() {
        return resRoot;
    }

    public void setResRoot(TreeNode resRoot) {
        this.resRoot = resRoot;
    }

    public TreeNode getDeptOperRoot() {
        return deptOperRoot;
    }

    public void setDeptOperRoot(TreeNode deptOperRoot) {
        this.deptOperRoot = deptOperRoot;
    }

    public WorkorderInfoShow getWorkorderInfoShowAdd() {
        return workorderInfoShowAdd;
    }

    public void setWorkorderInfoShowAdd(WorkorderInfoShow workorderInfoShowAdd) {
        this.workorderInfoShowAdd = workorderInfoShowAdd;
    }

    public WorkorderInfoShow getWorkorderInfoShowUpd() {
        return workorderInfoShowUpd;
    }

    public void setWorkorderInfoShowUpd(WorkorderInfoShow workorderInfoShowUpd) {
        this.workorderInfoShowUpd = workorderInfoShowUpd;
    }

    public WorkorderInfoShow getWorkorderInfoShowDel() {
        return workorderInfoShowDel;
    }

    public void setWorkorderInfoShowDel(WorkorderInfoShow workorderInfoShowDel) {
        this.workorderInfoShowDel = workorderInfoShowDel;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public String getStrBtnRender() {
        return strBtnRender;
    }

    public void setStrBtnRender(String strBtnRender) {
        this.strBtnRender = strBtnRender;
    }
}
