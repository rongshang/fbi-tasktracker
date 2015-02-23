package task.view.appoint;

import task.common.enums.EnumArchivedFlag;
import task.repository.model.OperRes;
import task.repository.model.Ptmenu;
import task.repository.model.model_show.DeptOperShow;
import task.repository.model.model_show.OperResShow;
import task.service.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class ResAppointOperAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(ResAppointOperAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{menuService}")
    private MenuService menuService;

    private OperResShow operResShowSeled;

    private List<OperResShow> operResShowList;
    private List<OperResShow> filteredOperResShowList;

    private List<DeptOperShow> deptOperShowSeledList;
    private TreeNode deptOperRoot;

    @PostConstruct
    public void init() {
        try {
            operResShowList = new ArrayList<>();
            filteredOperResShowList= new ArrayList<>();

            deptOperShowSeledList = new ArrayList<>();

            // 资源-用户-功能
            initRes();
            filteredOperResShowList.addAll(operResShowList);
            initDeptOperAppoint();

        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }
    private void initRes(){
        deptOperShowSeledList.clear();
        operResShowList.clear();
        Ptmenu ptmenuTemp=new Ptmenu();
        List<Ptmenu> ptmenuListTemp=menuService.selectListByModel(ptmenuTemp);
        OperRes operResTemp=new OperRes();
        List<OperResShow> operResShowListTemp=operResService.selectOperaResRecordsByModel(operResTemp);
        for(Ptmenu ptmenuUnit:ptmenuListTemp){
            String strInputOperName="";
            for(OperResShow operResShowUnit:operResShowListTemp){
                if(ptmenuUnit.getPkid().equals(operResShowUnit.getResPkid())){
                    if(strInputOperName.length()==0){
                        strInputOperName =
                                ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strInputOperName = strInputOperName + "," +
                                ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }
                }
            }
            OperResShow operResShowTemp=new OperResShow();
            operResShowTemp.setResPkid(ptmenuUnit.getPkid());
            operResShowTemp.setResName(ptmenuUnit.getMenulabel());
            operResShowTemp.setOperName(strInputOperName);
            operResShowList.add(operResShowTemp);
        }
    }

    private void initDeptOperAppoint(){
        deptOperShowSeledList.clear();
        deptOperRoot = new DefaultTreeNode("ROOT", null);
        DeptOperShow deptOperShowTemp =new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("机构人员信息");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptOperRoot);
        recursiveOperTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }
    private void recursiveOperTreeNode(String strParentPkidPara, TreeNode parentNode) {
        List<DeptOperShow> operResShowListTemp = deptOperService.selectDeptAndOperRecords(strParentPkidPara);
        for (DeptOperShow anOperResShowListTemp : operResShowListTemp) {
            TreeNode childNodeTemp = new DefaultTreeNode(anOperResShowListTemp, parentNode);
            recursiveOperTreeNode(anOperResShowListTemp.getPkid(), childNodeTemp);
        }
    }
    private void recursiveOperTreeNodeForExpand(
            TreeNode treeNodePara,List<OperResShow> operResShowListPara) {
        if (operResShowListPara==null||operResShowListPara.size()==0){
            return;
        }
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                TreeNode treeNodeTemp = treeNodePara.getChildren().get(i);
                DeptOperShow deptOperShowTemp = (DeptOperShow) treeNodeTemp.getData();
                if (deptOperShowTemp.getPkid()!=null&&"1".equals(deptOperShowTemp.getType())){
                    for (int j = 0; j < operResShowListPara.size(); j++) {
                        if (deptOperShowTemp.getPkid().equals(operResShowListPara.get(j).getOperPkid())) {
                            deptOperShowTemp.setIsSeled(true);
                            deptOperShowSeledList.add(deptOperShowTemp);
                            while (!(treeNodeTemp.getParent()==null)){
                                if (!(treeNodeTemp.isExpanded())&&treeNodeTemp.getChildCount()>0){
                                    treeNodeTemp.setExpanded(true);
                                }
                                treeNodeTemp=treeNodeTemp.getParent();
                            }
                            operResShowListPara.remove(j);
                            break;
                        }
                    }
                }
                recursiveOperTreeNodeForExpand(treeNodeTemp, operResShowListPara);
            }
        }
    }

    public void selectRecordAction(OperResShow operResShowPara) {
        try {
            operResShowSeled=operResShowPara;
            initDeptOperAppoint();
            OperRes operResTemp=new OperRes();
            operResTemp.setResPkid(operResShowSeled.getResPkid());
            List<OperResShow> operResShowListTemp=operResService.selectOperaResRecordsByModel(operResTemp);
            recursiveOperTreeNodeForExpand(deptOperRoot,operResShowListTemp);
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

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction(String strSubmitTypePara) {
        try {
            if (strSubmitTypePara.equals("Power")) {
                OperRes operResTemp = new OperRes();
                operResTemp.setResPkid(operResShowSeled.getResPkid());
                operResService.deleteRecordByResPkid(operResTemp);
                operResTemp.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
                for (DeptOperShow deptOperShowUnit : deptOperShowSeledList) {
                    operResTemp.setOperPkid(deptOperShowUnit.getPkid());
                    operResService.insertRecord(operResTemp);
                }
                MessageUtil.addInfo("权限添加成功!");
            }
            initRes();
            //过滤需要和原数据同步
            int selIndex=filteredOperResShowList.indexOf(operResShowSeled);
            filteredOperResShowList.remove(operResShowSeled);
            for(OperResShow operResShowUnit:operResShowList){
                if(operResShowUnit.getOperPkid().equals(operResShowSeled.getOperPkid())){
                    filteredOperResShowList.add(selIndex,operResShowUnit);
                }
            }
            initDeptOperAppoint();
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    /*智能字段 Start*/
    public TreeNode getDeptOperRoot() {
        return deptOperRoot;
    }

    public void setDeptOperRoot(TreeNode deptOperRoot) {
        this.deptOperRoot = deptOperRoot;
    }

    public List<DeptOperShow> getDeptOperShowSeledList() {
        return deptOperShowSeledList;
    }

    public void setDeptOperShowSeledList(List<DeptOperShow> deptOperShowSeledList) {
        this.deptOperShowSeledList = deptOperShowSeledList;
    }

    public List<OperResShow> getOperResShowList() {
        return operResShowList;
    }

    public void setOperResShowList(List<OperResShow> operResShowList) {
        this.operResShowList = operResShowList;
    }

    public List<OperResShow> getFilteredOperResShowList() {
        return filteredOperResShowList;
    }

    public void setFilteredOperResShowList(List<OperResShow> filteredOperResShowList) {
        this.filteredOperResShowList = filteredOperResShowList;
    }

    public MenuService getMenuService() {
        return menuService;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
}
