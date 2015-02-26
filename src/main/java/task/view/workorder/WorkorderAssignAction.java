package task.view.workorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.repository.model.WorkorderInfo;
import task.service.WorkorderAssignService;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by Administrator on 2015/2/13.
 * atuo: huzy
 * 工单指派
 */

@ManagedBean
@ViewScoped
public class WorkorderAssignAction {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderAssignAction.class);
    //工单pkid
    private String orderPkId;
    //工单名
    private String orderName;
    //workOrderAssignMng.xhtml(工单指派页面)选中多条数据
    private WorkorderInfo[] selectedWorkorderInfo;

    //workOrderAssignMng.xhtml(工单指派页面)显示用
    List<WorkorderInfo> workorderInfos = null;


    @ManagedProperty(value = "#{workorderAssignService}")
    private WorkorderAssignService workorderAssignService;

    /***
     * 根据工单ID或者工单名获取该条件的工单信息
     * param:orderPkId(工单pkid),orderName(工单名)
     * @return List<WorkorderInfo>
     */
    public List<WorkorderInfo> getWorkorderInfoByPkIdOrName(){

        try{
            //System.out.print("========1111111111=========");
            workorderInfos = workorderAssignService.getWorkorderInfoByPkIdOrName(orderPkId,orderName);
        }catch (Exception e){
            logger.info("WorkorderAssignAction类中的getWorkorderInfoByPkIdOrName异常:"+e.toString());
        }
        return workorderInfos;
    }

    public String getOrderPkId() {
        return orderPkId;
    }

    public void setOrderPkId(String orderPkId) {
        this.orderPkId = orderPkId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public List<WorkorderInfo> getWorkorderInfos() {
        return workorderInfos;
    }

    public void setWorkorderInfos(List<WorkorderInfo> workorderInfos) {
        this.workorderInfos = workorderInfos;
    }

    public void setWorkorderAssignService(WorkorderAssignService workorderAssignService) {
        this.workorderAssignService = workorderAssignService;
    }

    public WorkorderInfo[] getSelectedWorkorderInfo() {
        return selectedWorkorderInfo;
    }

    public void setSelectedWorkorderInfo(WorkorderInfo[] selectedWorkorderInfo) {
        this.selectedWorkorderInfo = selectedWorkorderInfo;
    }
}
