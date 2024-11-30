package com.retailedge.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String menuName;
    private Integer parentId;
    private UUID pageId;
    private String icon;
    private String path;
    private Integer orderNumber;
    private Boolean status;
    private String moduleName;
    private List<Integer> userAccessFeatureMappings;


    public Menu(Integer id, String menuName, String moduleName, Integer parentId, Integer orderNumber, String path, Boolean status, List<Integer> userAccessFeatureMappings) {
        this.id = id;
        this.menuName = menuName;
        this.moduleName = moduleName;
        this.parentId = parentId;
        this.orderNumber = orderNumber;
        this.path = path;
        this.status = status;
        this.userAccessFeatureMappings = userAccessFeatureMappings;
    }

}
