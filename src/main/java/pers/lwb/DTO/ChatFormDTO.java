package pers.lwb.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiWenBin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ChatFormDTO")
public class ChatFormDTO {

    @Schema(defaultValue = "1")
    private Long memoryId;

    @Schema(defaultValue = "你是谁？")
    private String message;
}
