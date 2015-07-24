package ua.george_nika.webtester.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.forms.AjaxSendInformation;
import ua.george_nika.webtester.forms.EditAccountForm;
import ua.george_nika.webtester.forms.RegisterNewAccountForm;
import ua.george_nika.webtester.services.AccountService;
import ua.george_nika.webtester.util.WebTesterConstants;
import ua.george_nika.webtester.util.WebTesterLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static String LOGGER_NAME = AdminController.class.getSimpleName();

    @Autowired
    AccountService accountService;

    @Autowired
    SortRestrictUtil sortAndRestrictUtil;

    // *****************
    //   Page section
    // *****************

    @RequestMapping("/mainPage")
    public String mainPage(HttpServletRequest request, HttpSession session, Model model,
                           @RequestParam(value = "sort", required = false) String sort,
                           @RequestParam(value = "page", required = false) String pageDirection,
                           @RequestParam(value = "idLike", required = false) String idLike,
                           @RequestParam(value = "nameLike", required = false) String nameLike) {

        sortAndRestrictUtil.setService(accountService);
        sortAndRestrictUtil.setSessionPageProperty(WebTesterConstants.SESSION_SHOW_ACCOUNT_ADMIN_PAGE);
        sortAndRestrictUtil.executeSortBlock(sort);
        int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
        sortAndRestrictUtil.setShowPageToSession(session, showPage);
        sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

        model.addAttribute("userList", accountService.getPartOfAccount(
                (showPage - 1) * WebTesterConstants.ROW_ON_PAGE, showPage * WebTesterConstants.ROW_ON_PAGE));
        model.addAttribute("pageNumber", showPage);

        request.setAttribute("namePage", "ADMIN   MAIN   PAGE");
        return "/admin/main";
    }

    @RequestMapping("/mainAjaxPage")
    @ResponseBody
    public AjaxSendInformation mainAjaxPage(HttpServletRequest request, HttpSession session, Model model,
                           @RequestParam(value = "sort", required = false) String sort,
                           @RequestParam(value = "page", required = false) String pageDirection,
                           @RequestParam(value = "idLike", required = false) String idLike,
                           @RequestParam(value = "nameLike", required = false) String nameLike) {

        sortAndRestrictUtil.setService(accountService);
        sortAndRestrictUtil.setSessionPageProperty(WebTesterConstants.SESSION_SHOW_ACCOUNT_ADMIN_PAGE);
        sortAndRestrictUtil.executeSortBlock(sort);
        int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
        sortAndRestrictUtil.setShowPageToSession(session, showPage);
        sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

        AjaxSendInformation ajaxResult = new AjaxSendInformation();
        ajaxResult.setPage(showPage);
        ajaxResult.setIdLike(accountService.getIdLikeRestriction());
        ajaxResult.setNameLike(accountService.getNameLikeRestriction());
        List<AccountEntity> resultList = accountService.getPartOfAccount(
                (showPage - 1) * WebTesterConstants.ROW_ON_PAGE, showPage * WebTesterConstants.ROW_ON_PAGE);
        ajaxResult.setEntityList(resultList);
        return ajaxResult;
    }

    @RequestMapping("/createNewAccountPage")
    public String createNewAccountPage(HttpServletRequest request, Model model) {
        model.addAttribute("registerNewAccountForm", new RegisterNewAccountForm());
        request.setAttribute("namePage", "NEW   ACCOUNT");
        return "admin/createAccount";
    }

    @RequestMapping("/editAccountPage/{idAccount}")
    public String editAccountPage(HttpServletRequest request,
                                  HttpSession session,
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
            return mainPage(request, session, model, null, null, null, null);
        }
    }

    // *****************
    //   Action section
    // *****************

    @RequestMapping("/createNewAccountAction")
    public String createNewAccountAction(HttpServletRequest request,
                                         HttpSession session,
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
            return mainPage(request, session, model, null, null, null, null);
        } catch (Exception ex) {
            model.addAttribute("registerNewAccountForm", registerNewAccountForm);
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't create new account - " + ex.getMessage());
            request.setAttribute("namePage", "NEW   ACCOUNT");
            return "admin/createAccount";
        }
    }

    @RequestMapping("/enableAccountAction/{idAccount}")
    public String enableAccountAction(HttpServletRequest request,
                                      HttpSession session,
                                      Model model,
                                      @PathVariable("idAccount") int idAccount) {
        try {
            accountService.setEnableAccountById(idAccount);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, session, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, session, model, idAccount);
        }
    }

    @RequestMapping("/disableAccountAction/{idAccount}")
    public String disableAccountAction(HttpServletRequest request,
                                       HttpSession session,
                                       Model model,
                                       @PathVariable("idAccount") int idAccount) {
        try {
            accountService.setDisableAccountById(idAccount);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, session, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, session, model, idAccount);
        }
    }

    @RequestMapping("/deleteAccountAction/{idAccount}")
    public String deleteAccountAction(HttpServletRequest request,
                                      HttpSession session,
                                      Model model,
                                      @PathVariable("idAccount") int idAccount) {
        try {
            accountService.deleteAccountById(idAccount);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Account deleted.");
            return mainPage(request, session, model, null, null, null, null);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't delete account - " + ex.getMessage());
            return editAccountPage(request, session, model, idAccount);
        }
    }

    @RequestMapping("/editAccountAction/{idAccount}")
    public String editAccountAction(HttpServletRequest request,
                                    HttpSession session,
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
            return editAccountPage(request, session, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, session, model, idAccount);
        }
    }

    @RequestMapping("/addRoleAction/{idAccount}/{idRole}")
    public String addRoleAction(HttpServletRequest request,
                                HttpSession session,
                                Model model,
                                @PathVariable("idAccount") int idAccount,
                                @PathVariable("idRole") int idRole) {
        try {
            accountService.addRoleToAccountById(idAccount, idRole);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, session, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, session, model, idAccount);
        }
    }

    @RequestMapping("/deleteRoleAction/{idAccount}/{idRole}")
    public String deleteRoleAction(HttpServletRequest request,
                                   HttpSession session,
                                   Model model,
                                   @PathVariable("idAccount") int idAccount,
                                   @PathVariable("idRole") int idRole) {
        try {
            accountService.deleteRoleFromAccountById(idAccount, idRole);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editAccountPage(request, session, model, idAccount);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update account - " + ex.getMessage());
            return editAccountPage(request, session, model, idAccount);
        }
    }
}
