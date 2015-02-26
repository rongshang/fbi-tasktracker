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
 * ����ָ��
 */

@ManagedBean
@ViewScoped
public class WorkorderAssignAction {

    private static final Logger logger = LoggerFactory.getLogger(WorkorderAssignAction.class);
    //����pkid
    private String orderPkId;
    //������
    private String orderName;
    //workOrderAssignMng.xhtml(����ָ��ҳ��)ѡ�ж�������
    private WorkorderInfo[] selectedWorkorderInfo;

    //workOrderAssignMng.xhtml(����ָ��ҳ��)��ʾ��
    List<WorkorderInfo> workorderInfos = null;


    @ManagedProperty(value = "#{workorderAssignService}")
    private WorkorderAssignService workorderAssignService;

    /***
     * ���ݹ���ID���߹�������ȡ�������Ĺ�����Ϣ
     * param:orderPkId(����pkid),orderName(������)
     * @return List<WorkorderInfo>
     */
    public List<WorkorderInfo> getWorkorderInfoByPkIdOrName(){

        try{
            //System.out.print("========1111111111=========");
            workorderInfos = workorderAssignService.getWorkorderInfoByPkIdOrName(orderPkId,orderName);
        }catch (Exception e){
            logger.info("WorkorderAssignAction���е�getWorkorderInfoByPkIdOrName�쳣:"+e.toString());
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
