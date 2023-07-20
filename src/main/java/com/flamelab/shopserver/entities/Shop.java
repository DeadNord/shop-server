package com.flamelab.shopserver.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "shops")
public class Shop {

    @Id
    private String id;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
    private String name;
    private String ownerId;
    private String ownerName;
    private String walletId;

}
