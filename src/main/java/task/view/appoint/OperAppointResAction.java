package task.view.appoint;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import task.common.enums.EnumArchivedFlag;
import task.repository.model.Oper;
import task.repository.model.OperRes;
import task.repository.model.Ptmenu;
import task.repository.model.model_show.DeptOperShow;
import task.repository.model.model_show.OperResShow;
import task.service.DeptOperService;
import task.service.MenuService;
import task.service.OperResService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class OperAppointResAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperAppointResAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{menuService}")
    private MenuService menuService;

    private OperResShow operResShowSeled;
    private List<OperResShow> operResShowList;
    private List<OperResShow> filteredOperResShowList;

    private List<OperResShow> operResShowList_Res;
    private List<OperResShow> operResShowList_ResSel;

    @PostConstruct
    public void init() {
        try {
            operResShowList = new ArrayList<>();
            filteredOperResShowList= new ArrayList<>();
            operResShowList_Res = new ArrayList<>();
            operResShowList_ResSel= new ArrayList<>();
            // 资源-用户-功能
            initOper();
            filteredOperResShowList.addAll(operResShowList);
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    public void selectRecordAction(OperResShow operResShowPara) {
        try {
            operResShowSeled=operResShowPara;
            operResShowList_Res.clear();
            operResShowList_ResSel.clear();
            Ptmenu ptmenuTemp=new Ptmenu();
            List<Ptmenu> ptmenuListTemp=menuService.selectListByModel(ptmenuTemp);
            OperResShow operResShowTemp = new OperResShow();
            operResShowTemp.setOperPkid(operResShowPara.getOperPkid());
            List<OperResShow> operResListTemp=operResService.selectOperResRecordsByModelShow(operResShowTemp);
            for (Ptmenu ptmenuUnit:ptmenuListTemp){
                OperResShow operResShow_Res=new OperResShow();
                operResShow_Res.setResPkid(ptmenuUnit.getPkid());
                operResShow_Res.setResName(ptmenuUnit.getMenulabel());
                for (OperResShow operResShowUnit:operResListTemp){
                    if(ptmenuUnit.getPkid().equals(operResShowUnit.getResPkid())){
                        operResShowList_ResSel.add(operResShow_Res);
                        break;
                    }
                }
                operResShowList_Res.add(operResShow_Res);
            }

        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    private void initOper(){
        operResShowList.clear();
        // 确定人员列表
        List<Oper> operListTemp=deptOperService.getOperList();
        // 人员列表对应的资源信息
        List<OperResShow> operResShowListOfOperResPtmenu=operResService.getOperResPtmenuList();
        for(Oper operUnit:operListTemp){
            String strResName="";
            for(OperResShow item_OperResPtmenu:operResShowListOfOperResPtmenu){
                if(operUnit.getPkid().equals(item_OperResPtmenu.getOperPkid())){
                    if(strResName.length()==0){
                        strResName =
                                ToolUtil.getStrIgnoreNull(item_OperResPtmenu.getResName());
                    }else {
                        strResName = strResName + "," +
                                ToolUtil.getStrIgnoreNull(item_OperResPtmenu.getResName());
                    }
                }
            }
            OperResShow operResShowTemp=new OperResShow();
            operResShowTemp.setOperPkid(operUnit.getPkid());
            operResShowTemp.setOperName(operUnit.getName());
            operResShowTemp.setResName(strResName);
            operResShowList.add(operResShowTemp);
        }
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction() {
        try {
            OperRes operResTemp = new OperRes();
            operResTemp.setOperPkid(operResShowSeled.getOperPkid());
            operResService.deleteRecordByOperPkid(operResTemp);
            for (OperResShow operResShowUnit : operResShowList_ResSel) {
                operResTemp.setResPkid(operResShowUnit.getResPkid());
                operResService.insertRecord(operResTemp);
            }
            MessageUtil.addInfo("权限添加成功!");
            initOper();
            //过滤需要和原数据同步
            int selIndex=filteredOperResShowList.indexOf(operResShowSeled);
            filteredOperResShowList.remove(operResShowSeled);
            for(OperResShow operResShowUnit:operResShowList){
                if(operResShowUnit.getOperPkid().equals(operResShowSeled.getOperPkid())){
                    filteredOperResShowList.add(selIndex,operResShowUnit);
                }
            }
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    /*智能字段 Start*/

    public List<OperResShow> getOperResShowList_Res() {
        return operResShowList_Res;
    }

    public void setOperResShowList_Res(List<OperResShow> operResShowList_Res) {
        this.operResShowList_Res = operResShowList_Res;
    }

    public List<OperResShow> getFilteredOperResShowList() {
        return filteredOperResShowList;
    }

    public void setFilteredOperResShowList(List<OperResShow> filteredOperResShowList) {
        this.filteredOperResShowList = filteredOperResShowList;
    }

    public List<OperResShow> getOperResShowList_ResSel() {
        return operResShowList_ResSel;
    }

    public void setOperResShowList_ResSel(List<OperResShow> operResShowList_ResSel) {
        this.operResShowList_ResSel = operResShowList_ResSel;
    }

    public List<OperResShow> getOperResShowList() {
        return operResShowList;
    }

    public void setOperResShowList(List<OperResShow> operResShowList) {
        this.operResShowList = operResShowList;
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
