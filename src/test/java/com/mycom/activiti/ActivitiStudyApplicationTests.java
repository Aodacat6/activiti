package com.mycom.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ActivitiStudyApplicationTests {

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Test
	void contextLoads() {
	}

	/**
	 * 启动流程
	 *
	 */
	@Test
	public void start() {
		/**
		 * 传入赋值人临时变量
		 */
		Map<String, Object> var = new HashMap<>();
		var.put("dep", "王经理");
		var.put("leader", "张总");
		var.put("mon", "李财务");
		var.put("em", "小明");

		//流程定义key
		String processDefinitionKey = "businesstrip";
		//启动流程
		final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, var);
		System.out.println("流程id：" + processInstance.getId());
		System.out.println("流程名称：" + processInstance.getName());
		System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
		System.out.println("流程部署id：" + processInstance.getDeploymentId());

	}

	/**
	 * 申请
	 */
	@Test
	public void apply() {
		//申请
		String assignee = "小明11";
		final Task task = taskService.createTaskQuery().taskAssignee(assignee).singleResult();

		if (task == null) {
			throw new RuntimeException(assignee + "没有代办任务");
		}


	}


}
