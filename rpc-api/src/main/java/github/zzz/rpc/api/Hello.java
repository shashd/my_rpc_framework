package github.zzz.rpc.api;

import lombok.*;

import java.io.Serializable;

/**
 * 测试api的对象
 * @author zzz
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hello implements Serializable {
    private Integer id;
    private String message;
}
