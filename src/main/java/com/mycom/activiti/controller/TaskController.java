package com.mycom.activiti.controller;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：songdalin
 * @date ：2022-04-13 上午 10:52
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping
public class TaskController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 部署了security框架，则必须要登录才能访问资源
     * @return
     */
    @GetMapping("/test")
    public String loginTest() {
        return "success";
    }

    /**
     *
     * 启动一个流程
     *
     * @return
     */
    @GetMapping("/start")
    public String start() {
        //流程key
        String key = "holiday";
        //动态参数
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "张三");
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, map);
        System.out.println("流程启动实例： " + processInstance);
        System.out.println("流程实例id： " + processInstance.getId());
        System.out.println("流程定义id： " + processInstance.getProcessDefinitionId());
        return "success";
    }

    /**
     *     流程申请
     *
     */
    @GetMapping("/apply")
    public String apply() {
        //流程实例id
        String instanceId = "1b160683-bae8-11ec-81aa-00ff022e4cea";
        final Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        if (task == null) {
            throw new RuntimeException("not found task..");
        }
        System.out.println("查询到任务id：" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());
        /**
         *
         * 传入流程变量
         *
         */
        Map<String, Object> map = new HashMap<>();
        //请10天假
        map.put("days", 10);

        taskService.complete(task.getId(), map);
        return "success";
    }

    /**
     * 拾取任务
     * @return
     */
    @GetMapping("/claim")
    public String claim() {
        /**
         * 拾取任务
         */
        String taskId = "61d4c3c9-baea-11ec-a70c-00ff022e4cea";
        String userId = "张财务";
        taskService.claim(taskId, userId);
        return "success";
    }


    /**
     * 查询并完成任务
     * @return
     */
    @GetMapping("/complete")
    public String queryAndComplete() {
        //负责人
        String assignee = "张财务";
        final Task task = taskService.createTaskQuery().taskAssignee(assignee).singleResult();

        if (task == null) {
            throw new RuntimeException("没有找到" + assignee + "的任务");
        }
        System.out.println("查询到任务id：" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());

        taskService.complete(task.getId());
        return "success";
    }

    /**
     * 查找历史任务
     * @return
     */
    @GetMapping("/history")
    public String findHistory() {
        //流程实例id
        String instanceId = "1b160683-bae8-11ec-81aa-00ff022e4cea";
        final List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).list();
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("没有找到流程实例的历史数据");
        }
        for (HistoricTaskInstance instance : list) {
            System.out.println("-------------------");
            System.out.println("流程id：" + instance.getId());
            System.out.println("流程负责人：" + instance.getAssignee());
            System.out.println("流程名称：" + instance.getName());
            System.out.println("-------------------");
        }
        return "success";
    }

}
