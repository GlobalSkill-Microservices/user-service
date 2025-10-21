package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.CountUserResponse;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Repository.AccountRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepo accountRepo;

    public List<CountUserResponse> getUserCountByRole() {
        List<Account> accounts = accountRepo.findAll();

        Map<AccountRole, Long> grouped = accounts.stream()
                .collect(Collectors.groupingBy(Account::getAccountRole, Collectors.counting()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    CountUserResponse dto = new CountUserResponse();
                    dto.setRole(entry.getKey());
                    dto.setCountNumber(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
