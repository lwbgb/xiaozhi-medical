package pers.lwb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.lwb.pojo.Appointment;

/**
 * @author LiWenBin
 */
public interface AppointmentService extends IService<Appointment> {

    Appointment getOne(Appointment appointment);
}
