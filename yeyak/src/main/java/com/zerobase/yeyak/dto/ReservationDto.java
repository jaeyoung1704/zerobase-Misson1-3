package com.zerobase.yeyak.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.zerobase.yeyak.entity.Reservation;
import com.zerobase.yeyak.entity.Store;
import com.zerobase.yeyak.entity.User;
import com.zerobase.yeyak.type.Approval;
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
public class ReservationDto {
    private Long reservNo;
    private User user;
    private Store store;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservDt;
    private String message;
    private Approval approval = Approval.CHECKING;
    private boolean visited;
    private String review;

    public Reservation toEntity() {
	return Reservation.builder()
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
