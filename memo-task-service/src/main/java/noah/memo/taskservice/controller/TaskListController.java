package noah.memo.taskservice.controller;

import noah.memo.framework.bean.request.ReqData;
import noah.memo.framework.bean.response.RspData;
import noah.memo.taskapi.bean.TaskList;
import noah.memo.taskservice.service.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 描述:
 * 任务列表控制器
 *
 * @author Noah
 * @create 2020-01-28 18:49
 */
@RestController
@RequestMapping(value = "/dc/taskList")
public class TaskListController {

    @Autowired
    TaskListService taskListService;

    /**
     * 根据用户id查找所有taskList
     *
     * @return
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RspData getAccountTaskList(@RequestBody ReqData paramData) {

        Integer accountId = paramData.getAsInt("accountId");
        List<TaskList> accountTaskList = taskListService.getAccountTaskList(accountId);

        return RspData.getSuccessRspData(accountTaskList);
    }

}
