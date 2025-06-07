package com.enefit.energyconsumption.modules.user.models.entity;


import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Role entity which use to grant access to users
 *
 * @author Keshani
 * @since 2021/11/13
 */

@Entity
@Table(name = "ROLES")
@Getter
@Setter
public class Role {
    @Id
    @Column(name = "ROLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "ROLE_NAME")
    private String name;
}