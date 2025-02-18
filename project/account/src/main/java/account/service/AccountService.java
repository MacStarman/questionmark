package account.service;


import service.core.AccountInfo;
import java.util.List;

public interface AccountService {

    List<AccountInfo> getAllAccountInfo();

    AccountInfo createAccount(AccountInfo accountInfo);

    AccountInfo getAccountInfoById(String id);

    AccountInfo updateAccountInfo(AccountInfo accountInfo);

    void deleteAccountInfoById(String id);
}
