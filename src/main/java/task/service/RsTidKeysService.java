package task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;
import task.repository.dao.RsTidKeysMapper;
import task.repository.dao.RsTidKeysMapper;
import task.repository.dao.not_mybatis.MyMenuMapper;
import task.repository.model.RsTidKeys;
import task.repository.model.RsTidKeysExample;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ÏÂÎç6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RsTidKeysService {
    @Autowired
    private RsTidKeysMapper rsTidKeysMapper;

    public List<RsTidKeys> getRsTidKeysList(RsTidKeys rsTidKeysPara) {
        RsTidKeysExample example= new RsTidKeysExample();
        RsTidKeysExample.Criteria criteria = example.createCriteria();
        if (!ToolUtil.getStrIgnoreNull(rsTidKeysPara.getTid()).equals("")){
            criteria.andTidEqualTo(rsTidKeysPara .getTid());
        }
        return rsTidKeysMapper.selectByExample(example);
    }

    public boolean isExistInDb(RsTidKeys rsTidKeysPara) {
        RsTidKeysExample example = new RsTidKeysExample();
        example.createCriteria().andTidEqualTo(rsTidKeysPara.getTid());
        return rsTidKeysMapper .countByExample(example) >= 1;
    }

    public void insertRecord(RsTidKeys rsTidKeysPara) {
        rsTidKeysMapper.insert(rsTidKeysPara);
    }

    public void updateRecord(RsTidKeys rsTidKeysPara){
        rsTidKeysMapper.updateByPrimaryKey(rsTidKeysPara);
    }

    public int deleteRecord(RsTidKeys rsTidKeysPara){
        return rsTidKeysMapper.deleteByPrimaryKey(rsTidKeysPara.getPkid());
    }
}
