package pers.lwb.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.MemoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pers.lwb.constant.MessageConstant;
import pers.lwb.exception.DeleteException;
import pers.lwb.exception.InsertException;
import pers.lwb.pojo.Appointment;
import pers.lwb.service.AppointmentService;

/**
 * @author LiWenBin
 */
@Slf4j
@Component
@Description("挂号相关功能")
public class AppointmentTool {

    private final AppointmentService appointmentService;

    public AppointmentTool(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Tool(name = "查询号源",
            value = "预约挂号前先执行该方法，首先必须先知晓用户的这些信息。根据挂号预约信息中的挂号科室、日期、时间和医生姓名查询是否有号源")
    public boolean query(@MemoryId Long memoryId, @P("挂号预约信息") Appointment appointment) {
        log.info("查询号源：{}", appointment);
        return true;

        // TODO 维护医生的排班信息：
        // 如果没有指定医生名字，则根据其他条件查询是否有可以预约的医生（有返回true，否则返回false）
        // 如果指定了医生名字，则判断医生是否有排班（没有排版返回false）
        // 如果有排班，则判断医生排班时间段是否已约满（约满返回false，有空闲时间返回true）
    }

    @Tool(name = "预约挂号",
            value = "查询号源后，执行该方法进行预约挂号，首先必须知晓用户的相关信息。" +
                    "该方法会先查询是否已经存在挂号记录，如果已经有了则提醒用户，否则保存挂号信息。")
    public void saveAppointment(@MemoryId Long memoryId, @P("挂号预约信息") Appointment appointment) {
        log.info("挂号预约：{}", appointment);
        Appointment res = appointmentService.getOne(appointment);
        if (res == null) {
            appointment.setId(null);
            // 防止幻读，确保 id 由数据库自动生成
            boolean flag = appointmentService.save(appointment);
            if (!flag) {
                throw new InsertException(MessageConstant.APPOINTMENT_SAVE_ERROR);
            }
            log.info(MessageConstant.APPOINTMENT_SAVE_SUCCESS);
        } else {
            throw new InsertException(MessageConstant.APPOINTMENT_EXIST);
        }
    }

    @Tool(name = "取消挂号预约",
            value = "取消预约前，根据挂号预约信息查询是否存在对应的预约，如果存在则删除预约信息" +
                    "最后提示用户取消预约的结果")
    public void cancelAppointment(@MemoryId Long memoryId, @P("挂号预约信息") Appointment appointment) {
        log.info("取消挂号预约：{}", appointment);
        Appointment res = appointmentService.getOne(appointment);
        if (res == null) {
            throw new DeleteException(MessageConstant.APPOINTMENT_NOT_EXIST);
        } else {
            boolean flag = appointmentService.removeById(res.getId());
            if (!flag) {
                throw new DeleteException(MessageConstant.APPOINTMENT_CANCEL_ERROR);
            }
            log.info(MessageConstant.APPOINTMENT_CANCEL_SUCCESS);
        }
    }
}
