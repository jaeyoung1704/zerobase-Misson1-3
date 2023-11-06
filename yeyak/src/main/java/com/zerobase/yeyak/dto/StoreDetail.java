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
public class StoreDetail {
    private Long storeNo;
    private UserDto user;
    private String name;
    private String address1;
    private String address2;
    private String description;

    public Store toEntity() {
	return Store.builder()
	    .storeNo(storeNo)
	    .user(user.toEntity())
	    .name(name)
	    .address1(address1)
	    .address2(address2)
	    .description(description)
	    .build();
    }
}
