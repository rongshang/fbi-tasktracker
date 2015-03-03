package task.service;

import task.repository.dao.WorkorderInfoMapper;
import task.repository.dao.not_mybatis.MyTaskMapper;
import task.repository.model.not_mybatis.TaskShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: 上午8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaskService {
    @Autowired
    private WorkorderInfoMapper workorderInfoMapper;
    @Autowired
    private MyTaskMapper myTaskMapper;

    public List<TaskShow> getTodoTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getTodoTaskShowList(strOperPkidPara);
    }

    public List<TaskShow> getDoneTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getDoneTaskShowList(strOperPkidPara);
    }

    public List<TaskShow> initDoneTaskShowList() {
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //通过OperatorManager
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();

        List<TaskShow> detailTaskShowListTemp = getDoneTaskShowList(strOperPkidTemp);

        return detailTaskShowListTemp;
    }

    public List<TaskShow> initTodoTaskShowList() {
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //通过OperatorManager
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();

        List<TaskShow> detailTaskShowListTemp = getTodoTaskShowList(strOperPkidTemp);

        return detailTaskShowListTemp;
    }


}
