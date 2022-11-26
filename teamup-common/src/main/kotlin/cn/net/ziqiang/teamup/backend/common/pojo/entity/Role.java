package cn.net.ziqiang.teamup.backend.common.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name", nullable = false, length = 30)
    @Schema(description = "角色名称")
    String name;
}
