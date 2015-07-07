package ua.george_nika.webtester.controllers;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.html.HTMLTableSectionElement;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.forms.EditAccountForm;
import ua.george_nika.webtester.forms.RegisterNewAccountForm;
import ua.george_nika.webtester.services.AccountService;
import ua.george_nika.webtester.util.WebTesterConstants;
import ua.george_nika.webtester.util.WebTesterLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static String LOGGER_NAME = AdminController.class.getSimpleName();

    @Autowired
    AccountService accountService;

    // *****************
    //   Page section
    // *****************

    @RequestMapping("/mainPage")
    public String mainPage(HttpServletRequest request, Model model) {
        //todo need paging
        model.addAttribute("userList", accountService.getPartOfAccount(0, 100));
        request.setAttribute("namePage", "ADMIN   MAIN   PAGE");
        return "/admin/main";
    }

    @RequestMapping("/createNewAccountPage")
    public String createNewAccountPage(HttpServletRequest request, Model model) {
        model.addAttribute("registerNewAccountForm", new RegisterNewAccountForm());
        request.setAttribute("namePage", "NEW   ACCOUNT");
        return "admin/createAccount";
    }

    @RequestMapping("/editAccountPage/{idAccount}")
    public String editAccountPage(HttpServletRequest request,
                                  Model model,
                                  @PathVariable("idAccount") int idAccount) {
        try {
            AccountEntity currentAccount =
                    (AccountEntity) request.getSession().getAttribute(WebTesterConstants.SESSION_ACCOUNT);

            AccountEntity account = accountService.getAccountById(currentAccount, idAccount);
            request.setAttribute("editAccount", account);
            model.addAttribute("editAccountForm", new EditAccountForm(account));
            request.setAttribute("namePage", "EDIT   ACCOUNT");
            return "admin/editAccount";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show account id: " + idAccount
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't show account id: " + idAccount
                    + " - " + ex.getMessage());
            return mainPage(request, model);
        }
    }

    // *****************
    //   Action section
    // *****************

    @RequestMapping("/createNewAccountAction")
    public String createNewAccountAction(HttpServletRequest request,
                                         Model model,
                                         @ModelAttribute("registerNewAccountForm")
                                         RegisterNewAccountForm registerNewAccountForm) {
        try {
            if (!registerNewAccountForm.getPassword().equals(registerNewAccountForm.getConfirmPassword())) {
                throw new UserWrongInputException("password not the same");
            }
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setLogin(registerNewAccountForm.getLogin());
            accountEntity.setPassword(registerNewAccountForm.getPassword());
            accountEntity.setEmail(registerNewAccountForm.getEmail());
            accountEntity.setFirstName(registerNewAccountForm.getFirstName());
            accountEntity.setLastName(registerNewAccountForm.getLastName());
            accountEntity.setMiddleName(registerNewAccountForm.getMiddleName());

            accountService.createNewAccount(accountEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Account successfully created. Email sent.");
            return mainPage(request, model);
        } catch (Exception ex) {
            model.addAttribute("registerNewAccountForm", registerNewAccountForm);
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't create new account - " + ex.getMessage());
            request.setAttribute("namePage", "NEW   ACCOUNT");
            return "admin/createAccount";
        }
    }

    @RequestMapping("/enableAccountAction/{idAccount}")
    public String enableAccountAction(HttpServletRequest request,
                                      Model model,
                                      @PathVariable("idAccount") int idAccount) {
        try {
            accountService.setEnableAccountById(idAccount);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, model, idAccount);
        }
    }

    @RequestMapping("/disableAccountAction/{idAccount}")
    public String disableAccountAction(HttpServletRequest request,
                                       Model model,
                                       @PathVariable("idAccount") int idAccount) {
        try {
            accountService.setDisableAccountById(idAccount);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, model, idAccount);
        }
    }

    @RequestMapping("/deleteAccountAction/{idAccount}")
    public String deleteAccountAction(HttpServletRequest request,
                                      Model model,
                                      @PathVariable("idAccount") int idAccount) {
        try {
            accountService.deleteAccountById(idAccount);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Account deleted.");
            return mainPage(request, model);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't delete account - " + ex.getMessage());
            return editAccountPage(request, model, idAccount);
        }
    }

    @RequestMapping("/editAccountAction/{idAccount}")
    public String editAccountAction(HttpServletRequest request,
                                    Model model,
                                    @ModelAttribute("editAccountForm") EditAccountForm editAccountForm,
                                    @PathVariable("idAccount") int idAccount) {
        try {
            AccountEntity currentAccount =
                    (AccountEntity) request.getSession().getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            AccountEntity tempAccountEntity = accountService.getAccountById(currentAccount, idAccount);
            editAccountForm.updateAccount(tempAccountEntity);
            accountService.updateAccount(tempAccountEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, model, idAccount);
        }
    }

    @RequestMapping("/addRoleAction/{idAccount}/{idRole}")
    public String addRoleAction(HttpServletRequest request,
                                Model model,
                                @PathVariable("idAccount") int idAccount,
                                @PathVariable("idRole") int idRole) {
        try {
            accountService.addRoleToAccountById(idAccount, idRole);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, model, idAccount);
        }
    }

    @RequestMapping("/deleteRoleAction/{idAccount}/{idRole}")
    public String deleteRoleAction(HttpServletRequest request,
                                   Model model,
                                   @PathVariable("idAccount") int idAccount,
                                   @PathVariable("idRole") int idRole) {
        try {
            accountService.deleteRoleFromAccountById(idAccount, idRole);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, model, idAccount);
        }
    }
}
