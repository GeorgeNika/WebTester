package ua.george_nika.webtester.controllers;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.RoleEntity;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.forms.BackupPasswordForm;
import ua.george_nika.webtester.forms.EditAccountForNonAdminForm;
import ua.george_nika.webtester.forms.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.george_nika.webtester.forms.RegisterNewAccountForm;
import ua.george_nika.webtester.security.CurrentAccount;
import ua.george_nika.webtester.security.SecurityUtils;
import ua.george_nika.webtester.services.AccountService;
import ua.george_nika.webtester.services.RoleService;
import ua.george_nika.webtester.util.WebTesterConstants;
import ua.george_nika.webtester.util.WebTesterLogger;
import ua.george_nika.webtester.util.WebTesterRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Controller
public class CommonController {

    private static String LOGGER_NAME = CommonController.class.getSimpleName();
    private static Map<Integer, String> mainRolePage = new HashMap<Integer, String>();

    static {
        mainRolePage.put(WebTesterRole.ADMINISTRATOR.getId(), "/admin/mainPage");
        mainRolePage.put(WebTesterRole.ADVANCE_TUTOR.getId(), "/tutor/mainPage");
        mainRolePage.put(WebTesterRole.TUTOR.getId(), "/tutor/mainPage");
        mainRolePage.put(WebTesterRole.STUDENT.getId(), "/student/mainPage");
    }

    @Autowired
    private RoleService roleService;

    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

    @Value("${fb.clientId}")
    private String facebookClientId;

    @Value("${fb.secretKey}")
    private String facebookSecretKey;

    @Value("${web.host}")
    private String applicationHost;

    @Value("${web.context}")
    private String applicationContext;

    private String fbReferrer;
    private String fbRedirectUri;

    //***************
    //  Page section
    //**************

    @RequestMapping("/loginPage")
    public String loginPage(HttpServletRequest request, HttpSession session, Model model) {
        session.setAttribute(WebTesterConstants.SESSION_ALL_ROLE_LIST, roleService.getAllRole());
        model.addAttribute("loginForm", new LoginForm());
        return "account/loginPage";
    }

    @RequestMapping("/backupPasswordPage")
    public String backupPasswordPage(HttpServletRequest request,
                                     HttpSession session,
                                     Model model,
                                     @ModelAttribute("loginForm") LoginForm loginForm) {
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        model.addAttribute("backupPasswordForm", new BackupPasswordForm());
        model.addAttribute("loginForm", loginForm);
        return "account/backupPassword";
    }


    @RequestMapping("/registerNewAccountPage")
    public String registerNewAccountPage(HttpServletRequest request,
                                         HttpSession session,
                                         Model model,
                                         @ModelAttribute("loginForm") LoginForm loginForm) {
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        model.addAttribute("registerNewAccountForm", new RegisterNewAccountForm());
        model.addAttribute("loginForm", loginForm);
        return "account/registerNewAccount";
    }

    @RequestMapping("/editAccountForNonAdminPage/{idAccount}")
    public String editAccountForNonAdminPage(HttpServletRequest request,
                                             HttpSession session,
                                             Model model,
                                             @PathVariable("idAccount") int idAccount) {
        try {
            AccountEntity currentAccount = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            AccountEntity editAccount = accountService.getAccountById(currentAccount, idAccount);
            request.setAttribute("editAccount", editAccount);
            model.addAttribute("editAccountForNonAdminForm", new EditAccountForNonAdminForm(editAccount));
            return "/account/editAccountForNonAdmin";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show account id: " + idAccount
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't show account id: " + idAccount
                    + " - " + ex.getMessage());
            return welcomeAction(request, session, model);
        }
    }

    @RequestMapping("/taskPage")
    public String taskPage(HttpServletRequest request,
                        HttpSession session,
                        Model model) {
        return "account/taskPage";
    }

    @RequestMapping("/error")
    public String error(HttpServletRequest request,
                        HttpSession session,
                        Model model,
                        @ModelAttribute("loginForm") LoginForm loginForm,
                        @RequestParam("url") String url) {
        model.addAttribute("loginForm", loginForm);
        request.setAttribute(WebTesterConstants.REQUEST_ERROR, url);
        return "account/error";
    }

    //***************
    //  Action section
    //**************

    @RequestMapping("/welcomeAction")
    public String welcomeAction(HttpServletRequest request,
                                HttpSession session,
                                Model model) {
        try {
            CurrentAccount currentAccount = SecurityUtils.getCurrentAccount();
            AccountEntity accountEntity = accountService.getAccountByIdWithoutCheck(currentAccount.getIdAccount());
            if (session.getAttribute(WebTesterConstants.SESSION_ROLE) != null) {
                currentAccount.setIdRole(((RoleEntity) session.getAttribute(WebTesterConstants.SESSION_ROLE)).getIdRole());
            }
            // if account haven`t this role
            RoleEntity roleEntity = roleService.getRoleById(currentAccount.getIdRole());
            if (!accountEntity.getRoleSet().contains(roleEntity)) {
                for (RoleEntity tempRole : accountEntity.getRoleSet()) {
                    roleEntity = tempRole;
                    currentAccount.setIdRole(roleEntity.getIdRole());
                    break;
                }
            }

            setSessionProperty(session, accountEntity, roleEntity);
            return "redirect:" + mainRolePage.get(currentAccount.getIdRole());
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't enter - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't enter: - " + ex.getMessage());
            return loginPage(request, session, model);
        }
    }


    @RequestMapping("/logoutAction")
    public String logoutAction(HttpSession session) {
        session.invalidate();
        return "redirect:/welcomeAction";
    }


    @RequestMapping("/editAccountForNonAdminAction/{idAccount}")
    public String editAccountForNonAdminAction(HttpServletRequest request,
                                               HttpSession session,
                                               Model model,
                                               @ModelAttribute("editAccountForNonAdminForm")
                                               EditAccountForNonAdminForm editAccountForNonAdminForm,
                                               @PathVariable("idAccount") int idAccount) {
        try {
            AccountEntity currentAccount =
                    (AccountEntity) request.getSession().getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            AccountEntity tempAccountEntity = accountService.getAccountById(currentAccount, idAccount);
            editAccountForNonAdminForm.updateAccount(tempAccountEntity);
            accountService.updateAccount(tempAccountEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return welcomeAction(request, session, model);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountForNonAdminPage(request, session, model, idAccount);
        }
    }

    private void setSessionProperty(HttpSession session, AccountEntity accountEntity, RoleEntity roleEntity) {
        session.setAttribute(WebTesterConstants.SESSION_ACCOUNT, accountEntity);
        session.setAttribute(WebTesterConstants.SESSION_ROLE, roleEntity);
        int tempOrBugButWithoutItDoNotWork = accountEntity.getRoleSet().size();
        // I think it's cause many-to-many relationship
        // may be it need only with hebirnate realization JPA
        if (tempOrBugButWithoutItDoNotWork > 0) {
            session.setAttribute(WebTesterConstants.SESSION_ROLE_LIST, accountEntity.getRoleSet());
        } else {
            throw new UserWrongInputException("no role to account id: " + accountEntity.getIdAccount());
        }
        session.setAttribute(WebTesterConstants.SESSION_ALL_ROLE_LIST, roleService.getAllRole());
    }

    @RequestMapping("/changeRoleAction/{idRole}")
    public String changeRoleAction(HttpServletRequest request,
                                   HttpSession session,
                                   Model model,
                                   @PathVariable("idRole") int idRole) {
        try {
            RoleEntity roleEntity = roleService.getRoleById(idRole);
            session.setAttribute(WebTesterConstants.SESSION_ROLE, roleEntity);
            return "redirect:" + mainRolePage.get(idRole);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't set role - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't set role: " + idRole + " - " + ex.getMessage());
            return loginPage(request, session, model);
        }
    }

    @RequestMapping("/backupPasswordAction")
    public String backupPasswordAction(HttpServletRequest request,
                                       HttpSession session,
                                       Model model,
                                       @ModelAttribute("backupPasswordForm") BackupPasswordForm backupPasswordForm) {
        try {
            accountService.backupPassword(backupPasswordForm.getEmail());
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Email with information sent to you address");
            return loginPage(request, session, model);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't sent email - " + ex.getMessage());
            model.addAttribute("backupPasswordForm", backupPasswordForm);
            model.addAttribute("loginForm", new LoginForm());
            return "account/backupPassword";
        }
    }

    @RequestMapping("/registerNewAccountAction")
    public String registerNewAccountAction(HttpServletRequest request,
                                           HttpSession session,
                                           Model model,
                                           @ModelAttribute("RegisterNewAccountForm")
                                           RegisterNewAccountForm registerNewAccountForm) {
        try {
            if (!registerNewAccountForm.getPassword().equals(registerNewAccountForm.getConfirmPassword())) {
                throw new RuntimeException("Password not the same");
            }
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setLogin(registerNewAccountForm.getLogin());
            accountEntity.setPassword(registerNewAccountForm.getPassword());
            accountEntity.setEmail(registerNewAccountForm.getEmail());
            accountEntity.setFirstName(registerNewAccountForm.getFirstName());
            accountEntity.setLastName(registerNewAccountForm.getLastName());
            accountEntity.setMiddleName(registerNewAccountForm.getMiddleName());

            accountService.createNewAccount(accountEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Account successfully registered. Email sent.");
            return loginPage(request, session, model);
        } catch (Exception ex) {
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerNewAccountForm", registerNewAccountForm);
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't register new account - " + ex.getMessage());
            return "account/registerNewAccount";
        }
    }

    @RequestMapping("/verifyAccountAction/{code}")
    public String verifyAccount(HttpServletRequest request,
                                HttpSession session,
                                Model model,
                                @PathVariable("code") String code) {
        try {
            accountService.verifyAccount(code);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Account successfully registered");
        } catch (RuntimeException ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Account NOT registered. Wrong data or time");
        } finally {
            return loginPage(request, session, model);
        }
    }

    @RequestMapping("/fbLoginAction")
    public String facebookLoginAction(@ModelAttribute("loginForm") LoginForm loginForm) {
        fbRedirectUri = applicationHost + "/" + applicationContext + "/loginFromFbAction/"
                + WebTesterRole.STUDENT.getId();
        fbReferrer = "https://graph.facebook.com/oauth/authorize?client_id=" + facebookClientId +
                "&redirect_uri=" + fbRedirectUri + "&scope=email";
        return "redirect:" + fbReferrer;
    }

    @RequestMapping("/loginFromFbAction/{idRole}")
    public String loginFromFacebookAction(HttpServletRequest request,
                                          HttpSession session,
                                          Model model,
                                          @RequestParam("code") String code,
                                          @PathVariable("idRole") int idRole) throws IOException {
        try {
            if (code == null) {
                throw new UserWrongInputException("Wrong facebook information");
            }
            User user = getFacebookUser(code);
            if (user.getEmail() == null) {
                throw new UserWrongInputException("Wrong facebook email information");
            }
            AccountEntity accountEntity = accountService.loginInSystemWithFacebook(user);
            RoleEntity roleEntity = roleService.getRoleById(idRole);
            if (!accountEntity.getRoleSet().contains(roleEntity)) {
                for (RoleEntity tempRole : accountEntity.getRoleSet()) {
                    roleEntity = tempRole;
                    break;
                }
            }
            SecurityUtils.authenticate(accountEntity, roleEntity);
            setSessionProperty(session, accountEntity, roleEntity);
            return "redirect:" + mainRolePage.get(roleEntity.getIdRole());
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can`t login from Facebook - " + ex.getMessage());
            return loginPage(request, session, model);
        }
    }

    protected User getFacebookUser(String code) throws IOException {
        String url = "https://graph.facebook.com/oauth/access_token?client_id=" + facebookClientId
                + "&redirect_uri=" + fbRedirectUri + "&client_secret=" + facebookSecretKey + "&code=" + code;
        URLConnection connection = new URL(url).openConnection();
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("//Z");
            String out = scanner.next();
            String[] auth1 = out.split("=");
            String[] auth2 = auth1[1].split("&");
            FacebookClient facebookClient = new DefaultFacebookClient(auth2[0]);
            User user = facebookClient.fetchObject("me", User.class);
            return user;
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't get facebook user - " + ex.getMessage());
            throw new UserWrongInputException("Can't get facebook user");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}

