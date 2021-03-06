package task.view.his;

import task.repository.model.WorkorderAppointHis;
import task.service.WorkorderAppointHisService;
import task.service.WorkorderInfoService;
/*import task.service.FlowHisService;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
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
public class WorkorderAppointHisAction {
    private static final Logger logger = LoggerFactory.getLogger(WorkorderAppointHisAction.class);

    @ManagedProperty(value = "#{workorderAppointHisService}")
    private WorkorderAppointHisService workorderAppointHisService;
    @ManagedProperty(value = "#{workorderInfoService}")
    private WorkorderInfoService workorderInfoService;

    private WorkorderAppointHis workorderAppointHis;
    private List<WorkorderAppointHis> workorderAppointHisList;

    private String strRendered1;
    private String strRendered2;
    private String strLabel1;
    private String strLabel2;
    private List<SelectItem> esInitCtt1List;
    private List<SelectItem> esInitCtt2List;

    @PostConstruct
    public void init() {
        workorderAppointHis =new WorkorderAppointHis();
        this.workorderAppointHisList = new ArrayList<WorkorderAppointHis>();
        esInitCtt1List=new ArrayList<SelectItem> ();
        esInitCtt2List=new ArrayList<SelectItem> ();
        strRendered1="false";
        strRendered2="false";
/*        resetAction();*/
    }

    public String onQueryAction(String strQryMsgOutPara) {
        try {
            this.workorderAppointHisList = workorderAppointHisService.getListByModel(workorderAppointHis);
            if(strQryMsgOutPara.equals("true")){
                if (workorderAppointHisList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
        return null;
    }

    public WorkorderAppointHisService getWorkorderAppointHisService() {
        return workorderAppointHisService;
    }

    public void setWorkorderAppointHisService(WorkorderAppointHisService workorderAppointHisService) {
        this.workorderAppointHisService = workorderAppointHisService;
    }

    public WorkorderInfoService getWorkorderInfoService() {
        return workorderInfoService;
    }

    public void setWorkorderInfoService(WorkorderInfoService workorderInfoService) {
        this.workorderInfoService = workorderInfoService;
    }

    public String getStrRendered1() {
        return strRendered1;
    }

    public void setStrRendered1(String strRendered1) {
        this.strRendered1 = strRendered1;
    }

    public String getStrRendered2() {
        return strRendered2;
    }

    public void setStrRendered2(String strRendered2) {
        this.strRendered2 = strRendered2;
    }

    public String getStrLabel1() {
        return strLabel1;
    }

    public void setStrLabel1(String strLabel1) {
        this.strLabel1 = strLabel1;
    }

    public String getStrLabel2() {
        return strLabel2;
    }

    public void setStrLabel2(String strLabel2) {
        this.strLabel2 = strLabel2;
    }

    public List<SelectItem> getEsInitCtt1List() {
        return esInitCtt1List;
    }

    public void setEsInitCtt1List(List<SelectItem> esInitCtt1List) {
        this.esInitCtt1List = esInitCtt1List;
    }

    public List<SelectItem> getEsInitCtt2List() {
        return esInitCtt2List;
    }

    public void setEsInitCtt2List(List<SelectItem> esInitCtt2List) {
        this.esInitCtt2List = esInitCtt2List;
    }

    public WorkorderAppointHis getWorkorderAppointHis() {
        return workorderAppointHis;
    }

    public void setWorkorderAppointHis(WorkorderAppointHis workorderAppointHis) {
        this.workorderAppointHis = workorderAppointHis;
    }

    public List<WorkorderAppointHis> getWorkorderAppointHisList() {
        return workorderAppointHisList;
    }

    public void setWorkorderAppointHisList(List<WorkorderAppointHis> workorderAppointHisList) {
        this.workorderAppointHisList = workorderAppointHisList;
    }
}
