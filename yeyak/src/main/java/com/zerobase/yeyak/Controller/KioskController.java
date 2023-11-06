package com.zerobase.yeyak.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zerobase.yeyak.dto.ReservationDto;
import com.zerobase.yeyak.repo.StoreRepo;
import com.zerobase.yeyak.repo.UserRepo;
import com.zerobase.yeyak.service.ReservService;
import com.zerobase.yeyak.service.StoreService;
import com.zerobase.yeyak.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller()
@RequestMapping("/kiosk/*")
@RequiredArgsConstructor
public class KioskController {
    private final ReservService reservService;

    @GetMapping(value = "/")
    public String kioskHome(HttpServletRequest request) {
	return "kiosk/main";
    }

    @GetMapping("/search")
    public String reservLists(@RequestParam(required = false) String name,
			      @RequestParam(required = false) String phone,
			      HttpServletRequest request, Model model) {
	if (name != null && phone != null) {
	    List<ReservationDto> reservList = reservService
		.getReservListByUserNameAndPhoneAndStoreNo(name, phone, request);
	    model.addAttribute("reservList", reservList);
	    model.addAttribute("name", name);
	    model.addAttribute("phone", phone);
	}
	return "kiosk/search";
    }

    // 방문확인 처리
    @PostMapping("/visit")
    public String visitedDo(RedirectAttributes attributes, @RequestParam String name,
			    @RequestParam String phone,
			    @RequestParam Long reservNo) {
	attributes.addAttribute("name", name)
	    .addAttribute("phone", phone);
	reservService.visit(reservNo);
	return "redirect:/kiosk/search";
    }

}
