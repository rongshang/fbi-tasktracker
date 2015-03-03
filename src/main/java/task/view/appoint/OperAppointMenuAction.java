package task.view.appoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import task.repository.model.Oper;
import task.repository.model.MenuAppoint;
import task.repository.model.Ptmenu;
import task.repository.model.not_mybatis.DeptOperShow;
import task.repository.model.not_mybatis.MenuAppointShow;
import task.service.DeptOperService;
import task.service.MenuService;
import task.service.OperMenuService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class OperAppointMenuAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperAppointMenuAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{operMenuService}")
    private OperMenuService operMenuService;
    @ManagedProperty(value = "#{menuService}")
    private MenuService menuService;

    private MenuAppointShow menuAppointShowSeled;
    private List<MenuAppointShow> menuAppointShowList;
    private List<MenuAppointShow> filteredMenuAppointShowList;

    private List<MenuAppointShow> menuAppointShowList_Res;
    private List<MenuAppointShow> menuAppointSel;

    @PostConstruct
    public void init() {
        try {
            menuAppointShowList = new ArrayList<>();
            filteredMenuAppointShowList = new ArrayList<>();
            menuAppointShowList_Res = new ArrayList<>();
            menuAppointSel = new ArrayList<>();
            // 资源-用户-功能
            initOper();
            filteredMenuAppointShowList.addAll(menuAppointShowList);
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    public void selectRecordAction(MenuAppointShow menuAppointShowPara) {
        try {
            menuAppointShowSeled = menuAppointShowPara;
            menuAppointShowList_Res.clear();
            menuAppointSel.clear();
            Ptmenu ptmenuTemp=new Ptmenu();
            List<Ptmenu> ptmenuListTemp=menuService.selectListByModel(ptmenuTemp);
            MenuAppointShow menuAppointShowTemp = new MenuAppointShow();
            menuAppointShowTemp.setOperPkid(menuAppointShowPara.getOperPkid());
            List<MenuAppointShow> operResListTemp=
                    operMenuService.getMenuAppointShowList(menuAppointShowTemp);
            for (Ptmenu ptmenuUnit:ptmenuListTemp){
                MenuAppointShow menuAppoint =new MenuAppointShow();
                menuAppoint.setMenuPkid(ptmenuUnit.getPkid());
                menuAppoint.setMenuName(ptmenuUnit.getMenulabel());
                for (MenuAppointShow menuAppointShowUnit :operResListTemp){
                    if(ptmenuUnit.getPkid().equals(menuAppointShowUnit.getMenuPkid())){
                        menuAppointSel.add(menuAppoint);
                        break;
                    }
                }
                menuAppointShowList_Res.add(menuAppoint);
            }

        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    private void initOper(){
        menuAppointShowList.clear();
        // 确定人员列表
        DeptOperShow deptOperShowPara=new DeptOperShow();
        List<Oper> operListTemp=deptOperService.getOperListByModelShow(deptOperShowPara);
        // 人员列表对应的资源信息
        List<MenuAppointShow> operMenuShowListOfMenuAppoint =
                operMenuService.getMenuAppointShowList(new MenuAppointShow());
        for(Oper operUnit:operListTemp){
            String strResName="";
            for(MenuAppointShow item_OperMenuPtmenu: operMenuShowListOfMenuAppoint){
                if(operUnit.getPkid().equals(item_OperMenuPtmenu.getOperPkid())){
                    if(strResName.length()==0){
                        strResName =
                                ToolUtil.getStrIgnoreNull(item_OperMenuPtmenu.getMenuName());
                    }else {
                        strResName = strResName + "," +
                                ToolUtil.getStrIgnoreNull(item_OperMenuPtmenu.getMenuName());
                    }
                }
            }
            MenuAppointShow menuAppointShowTemp =new MenuAppointShow();
            menuAppointShowTemp.setOperPkid(operUnit.getPkid());
            menuAppointShowTemp.setOperName(operUnit.getName());
            menuAppointShowTemp.setMenuName(strResName);
            menuAppointShowList.add(menuAppointShowTemp);
        }
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction() {
        try {
            MenuAppoint menuAppointTemp = new MenuAppoint();
            menuAppointTemp.setOperPkid(menuAppointShowSeled.getOperPkid());
            operMenuService.deleteRecordByOperPkid(menuAppointTemp);
            for (MenuAppointShow menuAppointShowUnit : menuAppointSel) {
                menuAppointTemp.setMenuPkid(menuAppointShowUnit.getMenuPkid());
                operMenuService.insertRecord(menuAppointTemp);
            }
            MessageUtil.addInfo("权限添加成功!");
            initOper();
            //过滤需要和原数据同步
            int selIndex= filteredMenuAppointShowList.indexOf(menuAppointShowSeled);
            filteredMenuAppointShowList.remove(menuAppointShowSeled);
            for(MenuAppointShow menuAppointShowUnit : menuAppointShowList){
                if(menuAppointShowUnit.getOperPkid().equals(menuAppointShowSeled.getOperPkid())){
                    filteredMenuAppointShowList.add(selIndex, menuAppointShowUnit);
                }
            }
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    /*智能字段 Start*/

    public List<MenuAppointShow> getMenuAppointShowList_Res() {
        return menuAppointShowList_Res;
    }

    public void setMenuAppointShowList_Res(List<MenuAppointShow> menuAppointShowList_Res) {
        this.menuAppointShowList_Res = menuAppointShowList_Res;
    }

    public List<MenuAppointShow> getFilteredMenuAppointShowList() {
        return filteredMenuAppointShowList;
    }

    public void setFilteredMenuAppointShowList(List<MenuAppointShow> filteredMenuAppointShowList) {
        this.filteredMenuAppointShowList = filteredMenuAppointShowList;
    }

    public List<MenuAppointShow> getMenuAppointSel() {
        return menuAppointSel;
    }

    public void setMenuAppointSel(List<MenuAppointShow> menuAppointSel) {
        this.menuAppointSel = menuAppointSel;
    }

    public List<MenuAppointShow> getMenuAppointShowList() {
        return menuAppointShowList;
    }

    public void setMenuAppointShowList(List<MenuAppointShow> menuAppointShowList) {
        this.menuAppointShowList = menuAppointShowList;
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

    public OperMenuService getOperMenuService() {
        return operMenuService;
    }

    public void setOperMenuService(OperMenuService operMenuService) {
        this.operMenuService = operMenuService;
    }
}
