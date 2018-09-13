package org.csu.travelbyex.controller;

import io.swagger.annotations.ApiOperation;

import org.csu.travelbyex.core.RequestEx;
import org.csu.travelbyex.core.Result;
import org.csu.travelbyex.core.ResultGenerator;
import org.csu.travelbyex.domain.Account;
import org.csu.travelbyex.domain.AccountInfo;
import org.csu.travelbyex.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@CrossOrigin
public class AccountController {

    @Autowired
    AccountService accountService;

    @ApiOperation(value = "创建一个新用户", notes = "前端需保证userId不重复")
    @PostMapping("/accounts")
    public Result createAccount(@RequestBody RequestEx requestEx)
    {
        Account account = requestEx.getAccount();
        AccountInfo accountInfo = requestEx.getAccountInfo();
        if (accountInfo == null) accountInfo = new AccountInfo();
        if(account.getPassword()==null || account.getUserId() == null)
            return ResultGenerator.fail("注册失败");
        accountService.insertAccount(account);
        accountInfo.setUserId(account.getUserId());
        accountService.insertAccountInfo(accountInfo);
        return ResultGenerator.success("注册失败");
    }

    @ApiOperation(value = "根据userId更新用户信息", notes = "前端需保证userId不重复")
    @PutMapping("/accounts")
    public Result updateAccount(@RequestBody RequestEx requestEx)
    {
        try
        {
            Account account = requestEx.getAccount();
            AccountInfo accountInfo = requestEx.getAccountInfo();
            if (account != null) accountService.updateAccount(account);
            if (accountInfo != null) accountService.updateAccountInfo(accountInfo);
            return ResultGenerator.success("更新成功");
        }catch (Exception e)
        {
            return ResultGenerator.fail("更新失败");
        }
    }


    @ApiOperation(value = "根据userId查询用户", notes = "判断用户Id是否已存在，不存在则status为0，存在则status为1")
    @GetMapping("/accounts")
    public Result getAccountByName(@RequestParam("userId") String userId)
    {
        Account account = accountService.getAccountByUserId(userId);
        if (account == null)
            return ResultGenerator.fail("用户不存在");
        List<AccountInfo> accountInfos = new ArrayList<>();
        AccountInfo accountInfo = accountService.getAccountInfoByUserId(userId);
        accountInfos.add(accountInfo);
        return ResultGenerator.success(accountInfos);
    }


    @ApiOperation(value = "根据userId删除用户", notes = "0成功，1失败")
    @DeleteMapping("/accounts")
    public Result deleteAccountByUserId(@RequestParam("userId") String userId)
    {
        Account account = accountService.getAccountByUserId(userId);
        if (account == null) return ResultGenerator.fail("该用户不存在");
        accountService.delteAccountByUserId(userId);
        accountService.delteAccountInfoByUserId(userId);
        return ResultGenerator.success("删除成功");
    }


    @ApiOperation(value="根据姓名和密码返回用户信息", notes = "用于登录，信息正确则status为0并返回用户详细信息，信息错误则status为1")
    @PostMapping("/accounts/login")
    public Result getAccountByNameAndPassword(@RequestBody RequestEx requestEx)
    {
        Account account = requestEx.getAccount();
        account = accountService.getAccountByUserIdAndPassword(account.getUserId(),account.getPassword());
        if (account == null) return ResultGenerator.fail("登录失败");
        return ResultGenerator.success(accountService.getAccountInfoByUserId(account.getUserId()));
    }

    @ApiOperation(value="测试", notes = "测试")
    @GetMapping("/test")
    public Map xixi(@RequestParam(value = "page") int page, @RequestParam(value = "per_page") int per_page)
    {
        System.out.println(page);
        System.out.println(per_page);
        return null;
    }

}
