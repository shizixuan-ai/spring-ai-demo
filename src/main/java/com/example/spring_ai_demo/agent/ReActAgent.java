package com.example.spring_ai_demo.agent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ReActAgent extends BaseAgent {

    /**
     * 执行单个步骤：思考和行动
     */
    @Override
    public String step() {
        try {
            boolean shouldAct = this.think();
            if (!shouldAct) {
                return "思考完成 - 无需行动";
            }
            return act();
        } catch (Exception e) {
            // TODO: handle exception
            log.error("步骤执行失败,", e);
            return "步骤执行失败：" + e.getMessage();
        }
    }

    /**
     * 处理当前状态并决定下一步行动
     *
     * @return 是否需要执行行动，true表示需要执行，false表示不需要执行
     */
    public abstract boolean think();

    /**
     * 执行决定的行动
     *
     * @return 行动执行结果
     */
    public abstract String act();

}
