package com.zerobase.yeyak.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.zerobase.yeyak.dto.ReservationDto;
import com.zerobase.yeyak.dto.StoreDetail;
import com.zerobase.yeyak.dto.StoreSimple;
import com.zerobase.yeyak.type.Approval;
import com.zerobase.yeyak.type.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservNo;
    @ManyToOne // 유저 하나당 예약 여러개 가능
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne // 가게 하나당 예약 여러개 가능
    @JoinColumn(name = "store_no")
    private Store store;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservDt;
    private String message;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime regDt;
    @Enumerated(value = EnumType.STRING)
    private Approval approval = Approval.CHECKING;
    private boolean visited = false;
    private String review;

    public ReservationDto toDto() {
	return ReservationDto.builder()
	    .reservNo(reservNo)
	    .user(user)
	    .store(store)
	    .reservDt(reservDt)
	    .message(message)
	    .approval(approval)
	    .visited(visited)
	    .review(review)
	    .build();
    }
}
