package pers.lwb.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import dev.langchain4j.model.output.structured.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiWenBin
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Description("挂号预约信息")
public class Appointment {

    @TableId(type = IdType.AUTO)
    @Description("该字段为MySql中的主键值，设置为空即可")
    private Long id;

    @Description("姓名")
    private String username;

    @Description("身份证")
    private String idCard;

    @Description("挂号科室")
    private String department;

    @Description("挂号日期（年月日）")
    private String date;

    @Description("挂号具体时间")
    private String time;

    @Description("挂号医生姓名")
    private String doctorName;
}
