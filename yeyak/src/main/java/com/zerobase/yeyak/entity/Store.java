package com.zerobase.yeyak.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.zerobase.yeyak.dto.StoreDetail;
import com.zerobase.yeyak.dto.StoreSimple;
import com.zerobase.yeyak.type.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeNo;
    @ManyToOne // 유저하나당 매장 여러개 가능
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    private String address1;
    private String address2;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime regDt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime upDt;

    public StoreDetail toDetail() {
	return StoreDetail.builder()
	    .storeNo(storeNo)
	    .user(user.toDto())
	    .name(name)
	    .address1(address1)
	    .address2(address2)
	    .description(description)
	    .build();
    }

    public StoreSimple toSimple() {
	return StoreSimple.builder()
	    .storeNo(storeNo)
	    .name(name)
	    .description(description)
	    .build();
    }
}
