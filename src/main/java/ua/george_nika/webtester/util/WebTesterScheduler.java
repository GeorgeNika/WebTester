package ua.george_nika.webtester.util;

import org.springframework.beans.factory.annotation.Autowired;
import ua.george_nika.webtester.services.AccountService;

/**
 * Created by George on 19.06.2015.
 */
public class WebTesterScheduler {
    @Autowired
    AccountService accountService;

    public void WebTesterScheduler (){

    }

    public void deleteNoneVerifiedAccount(){
        accountService.deleteUnverifiedAccount();
    }

}
