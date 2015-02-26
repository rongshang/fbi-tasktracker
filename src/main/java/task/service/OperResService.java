package task.service;

import org.springframework.transaction.annotation.Transactional;
import skyline.util.ToolUtil;
import org.springframework.stereotype.Service;
import task.repository.dao.OperResMapper;
import task.repository.dao.not_mybatis.MyOperResMapper;
import task.repository.model.OperRes;
import task.repository.model.OperResExample;
import task.repository.model.model_show.OperResShow;

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

    public List<OperResShow> selectOperResRecordsByModelShow(OperResShow operResShowPara){
        return myOperResMapper.selectOperResRecordsByModelShow(operResShowPara);
    }
    public List<OperResShow> selectOperaResRecordsByModel(OperRes operResPara){
        return myOperResMapper.selectOperResRecordsByModelShow(fromModelToModelShow(operResPara));
    }
    public List<OperResShow> getOperResPtmenuList(){
        return myOperResMapper.getOperResPtmenuList();
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
    public void deleteRecordByResPkid(OperRes operResPara){
        OperResExample example =new OperResExample();
        OperResExample.Criteria criteria = example.createCriteria();
        criteria.andResPkidEqualTo(operResPara.getResPkid());
        operResMapper.deleteByExample(example);
    }
    public void deleteRecordByOperPkid(OperRes operResPara){
        OperResExample example =new OperResExample();
        OperResExample.Criteria criteria = example.createCriteria();
        criteria.andOperPkidEqualTo(operResPara.getOperPkid());
        operResMapper.deleteByExample(example);
    }

    public OperRes fromOperShowToModel(OperResShow record) {
        OperRes operResPara=new OperRes();
        operResPara.setTid(record.getTid());
        operResPara.setOperPkid(record.getOperPkid());
        operResPara.setResPkid(record.getResPkid());
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
        operResShowTemp.setResPkid(operResPara.getResPkid());
        operResShowTemp.setArchivedFlag(operResPara.getArchivedFlag());
        operResShowTemp.setCreatedBy(operResPara.getCreatedBy());
        operResShowTemp.setCreatedTime(operResPara.getCreatedTime());
        operResShowTemp.setLastUpdBy(operResPara.getLastUpdBy());
        operResShowTemp.setLastUpdTime(operResPara.getLastUpdTime());
        operResShowTemp.setRemark(operResPara.getRemark());
        operResShowTemp.setRecVersion(ToolUtil.getIntIgnoreNull(operResPara.getRecVersion()));
        return operResShowTemp;
    }
}
