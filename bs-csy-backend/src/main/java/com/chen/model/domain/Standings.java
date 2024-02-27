package com.chen.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Standings  implements Serializable {
    private Long id;
    private Long userId;
    private Integer fraction;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
