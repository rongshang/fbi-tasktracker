package task.service;

import task.common.enums.EnumResType;
import task.repository.model.*;
import org.springframework.transaction.annotation.Transactional;
import skyline.util.ToolUtil;
import task.repository.dao.OperResMapper;
import task.repository.dao.not_mybatis.MyOperResMapper;
import task.repository.model.model_show.CttInfoShow;
import task.repository.model.model_show.OperResShow;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class OperResService {
    @Resource
    private MyOperResMapper myOperResMapper;
    @Resource
    private OperResMapper operResMapper;
    @Resource
    private CttInfoService cttInfoService;
    @Resource
    private CttItemService cttItemService;

    public List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara){
        return myOperResMapper.selectOperaResRecordsByModelShow(operResShowPara);
    }
    public List<OperResShow> selectOperaResRecordsByModel(OperRes operResPara){
        return myOperResMapper.selectOperaResRecordsByModelShow(fromModelToModelShow(operResPara));
    }
    public List<OperResShow> getInfoListByOperFlowPkid(String strInfoTypePara,String strFlowStatusPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        return myOperResMapper.getInfoListByOperFlowPkid(strInfoTypePara,strFlowStatusPara,strOperatorIdTemp);
    }
    public OperRes getOperResByPkid(String strOperResPkidPara){
        return  operResMapper.selectByPrimaryKey(strOperResPkidPara);
    }
    public void insertRecord(OperResShow operResShowPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        operResShowPara.setCreatedBy(strOperatorIdTemp);
        operResShowPara.setCreatedTime(strLastUpdTime);
        operResShowPara.setLastUpdBy(strOperatorIdTemp);
        operResShowPara.setLastUpdTime(strLastUpdTime);
        operResMapper.insert(fromOperShowToModel(operResShowPara));
    }
    public void insertRecord(OperRes operResPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        operResPara.setCreatedBy(strOperatorIdTemp);
        operResPara.setCreatedTime(strLastUpdTime);
        operResPara.setLastUpdBy(strOperatorIdTemp);
        operResPara.setLastUpdTime(strLastUpdTime);
        operResMapper.insertSelective(operResPara);
    }
    public void updateRecord(OperRes operResPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        operResPara.setLastUpdBy(strOperatorIdTemp);
        operResPara.setLastUpdTime(strLastUpdTime);
        operResMapper.updateByPrimaryKey(operResPara);
    }
    public void deleteRecord(OperRes operResPara){
        OperResExample example =new OperResExample();
        OperResExample.Criteria criteria = example.createCriteria();
        if(!ToolUtil.getStrIgnoreNull(operResPara.getInfoType()).equals("")){
            criteria.andInfoTypeEqualTo(operResPara.getInfoType());
        }
        if(!ToolUtil.getStrIgnoreNull(operResPara.getFlowStatus()).equals("")){
            criteria.andFlowStatusEqualTo(operResPara.getFlowStatus());
        }
        criteria.andInfoPkidEqualTo(operResPara.getInfoPkid());
        operResMapper.deleteByExample(example);
    }

    @Transactional
    public String deleteResRecord(CttInfoShow cttInfoShowPara){
        if (cttInfoService.findChildRecordsByPkid(cttInfoShowPara.getPkid())) {
            return "�ò����з�֧���޷�ɾ����";
        }
        if (EnumResType.RES_TYPE0.getCode().equals(cttInfoShowPara.getCttType())
                || EnumResType.RES_TYPE1.getCode().equals(cttInfoShowPara.getCttType())
                || EnumResType.RES_TYPE2.getCode().equals(cttInfoShowPara.getCttType())){
            if (cttItemService.getEsItemList(cttInfoShowPara.getCttType(),cttInfoShowPara.getPkid()).size()>0
                    ||cttInfoService.getCttInfoByPkId(cttInfoShowPara.getPkid()).getFlowStatus()!=null) {
                return "�����ѱ����ã�����ɾ����";
            }else {
                cttInfoService.deleteRecord(cttInfoShowPara.getPkid());
            }
        }
        OperResExample example=new OperResExample();
        OperResExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeEqualTo(cttInfoShowPara.getCttType())
                .andInfoPkidEqualTo(cttInfoShowPara.getPkid());
        operResMapper.deleteByExample(example);
        return "ɾ��������ɡ�";
    }

    public OperRes fromOperShowToModel(OperResShow record) {
        OperRes operResPara=new OperRes();
        operResPara.setTid(record.getTid());
        operResPara.setOperPkid(record.getOperPkid());
        operResPara.setFlowStatus(record.getFlowStatus());
        operResPara.setInfoType(record.getInfoType());
        operResPara.setInfoPkid(record.getInfoPkid());
        operResPara.setArchivedFlag(record.getArchivedFlag());
        operResPara.setCreatedBy(record.getCreatedBy());
        operResPara.setCreatedTime(record.getCreatedTime());
        operResPara.setLastUpdBy(record.getLastUpdBy());
        operResPara.setLastUpdTime(record.getLastUpdTime());
        operResPara.setRemark(record.getRemark());
        operResPara.setRecVersion( ToolUtil.getIntIgnoreNull(record.getRecVersion()));
        return operResPara;
    }
    public OperResShow fromModelToModelShow(OperRes operResPara) {
        OperResShow operResShowTemp=new OperResShow();
        operResShowTemp.setTid(operResPara.getTid());
        operResShowTemp.setOperPkid(operResPara.getOperPkid());
        operResShowTemp.setFlowStatus(operResPara.getFlowStatus());
        operResShowTemp.setInfoType(operResPara.getInfoType());
        operResShowTemp.setInfoPkid(operResPara.getInfoPkid());
        operResShowTemp.setArchivedFlag(operResPara.getArchivedFlag());
        operResShowTemp.setCreatedBy(operResPara.getCreatedBy());
        operResShowTemp.setCreatedTime(operResPara.getCreatedTime());
        operResShowTemp.setLastUpdBy(operResPara.getLastUpdBy());
        operResShowTemp.setLastUpdTime(operResPara.getLastUpdTime());
        operResShowTemp.setRemark(operResPara.getRemark());
        operResShowTemp.setRecVersion( ToolUtil.getIntIgnoreNull(operResPara.getRecVersion()));
        operResShowTemp.setType(operResPara.getType());
        return operResShowTemp;
    }
}
