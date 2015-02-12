package task.view.task;

import task.repository.model.model_show.TaskShow;
import task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TodoTaskAction {
    private static final Logger logger = LoggerFactory.getLogger(TodoTaskAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private List<TaskShow> taskShowList;

    @PostConstruct
    public void init() {
        try {
            //���������б�
            taskShowList = new ArrayList<>();
            taskShowList = taskService.initTodoTaskShowList();
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    public List<TaskShow> getTaskShowList() {
        return taskShowList;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}