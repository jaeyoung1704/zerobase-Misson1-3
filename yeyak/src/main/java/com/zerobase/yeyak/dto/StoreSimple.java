package com.zerobase.yeyak.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.zerobase.yeyak.entity.Store;
import com.zerobase.yeyak.type.Role;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StoreSimple {
    private Long storeNo;
    private String name;
    private String description;

    public Store toEntity() {
	return Store.builder()
	    .storeNo(storeNo)
	    .name(name)
	    .description(description)
	    .build();
    }
}
