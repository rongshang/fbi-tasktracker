package task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;
import task.repository.dao.TidKeysMapper;
import task.repository.dao.not_mybatis.MyMenuMapper;
import task.repository.model.TidKeys;
import task.repository.model.TidKeysExample;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ÏÂÎç6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TidKeysService {
    @Autowired
    private TidKeysMapper tidKeysMapper;

    public List<TidKeys> getTidKeysList(TidKeys tidKeysPara) {
        TidKeysExample example= new TidKeysExample();
        TidKeysExample.Criteria criteria = example.createCriteria();
        if (!ToolUtil.getStrIgnoreNull(tidKeysPara.getTid()).equals("")){
            criteria.andTidEqualTo(tidKeysPara .getTid());
        }
        return tidKeysMapper.selectByExample(example);
    }

    public boolean isExistInDb(TidKeys tidKeysPara) {
        TidKeysExample example = new TidKeysExample();
        example.createCriteria().andTidEqualTo(tidKeysPara.getTid());
        return tidKeysMapper .countByExample(example) >= 1;
    }

    public void insertRecord(TidKeys tidKeysPara) {
        tidKeysMapper.insert(tidKeysPara);
    }

    public void updateRecord(TidKeys tidKeysPara){
        tidKeysMapper.updateByPrimaryKey(tidKeysPara);
    }

    public int deleteRecord(TidKeys tidKeysPara){
        return tidKeysMapper.deleteByPrimaryKey(tidKeysPara.getPkid());
    }
}
