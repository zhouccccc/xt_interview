package com.xt.interview.service;

import com.xt.interview.dto.AccountDTO;

import java.util.List;
import java.util.Map;

public interface AccountService {

    AccountDTO createAccount(AccountDTO dto);

    AccountDTO updateAccount(Long id, AccountDTO dto);

    void deleteAccount(Long id);

    AccountDTO getAccountById(Long id);

    List<AccountDTO> getAllAccounts();

    // 任务三：按条件分页查询用户列表
    Map<String, Object> getAccountsPage(int page, int size, String username, Integer status, Integer roleLevel);

    AccountDTO login(String username, String password);
}
