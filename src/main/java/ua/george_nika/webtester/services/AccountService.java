package ua.george_nika.webtester.services;

import com.restfb.types.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import ua.george_nika.webtester.dao.intface.AccountDao;
import ua.george_nika.webtester.dao.intface.AccountVerificationDao;
import ua.george_nika.webtester.dao.intface.RoleDao;
import ua.george_nika.webtester.dao.util.SortAndRestrictForEntity;
import ua.george_nika.webtester.entity.AccountEntity;
import ua.george_nika.webtester.entity.AccountVerificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.george_nika.webtester.entity.RoleEntity;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.util.WebTesterCheckEmail;
import ua.george_nika.webtester.util.WebTesterConstants;
import ua.george_nika.webtester.util.WebTesterLogger;
import ua.george_nika.webtester.util.WebTesterRole;

import javax.mail.internet.InternetAddress;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

@Service("accountService")
@Transactional(readOnly = true)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AccountService {
    private static final int ID_ROLE_STUDENT = WebTesterRole.STUDENT.getId();
    private static String LOGGER_NAME = AccountService.class.getSimpleName();
    private SortAndRestrictForEntity sortAndRestrict = new SortAndRestrictForEntity();

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountVerificationDao accountVerificationDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private JavaMailSender defaultMailSender;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${web.host}")
    private String webHost;

    @Value("${web.context}")
    private String webContext;

    @Transactional(readOnly = false)
    public AccountEntity loginInSystemWithFacebook(User user) {
        try {
            AccountEntity resultAccount = null;
            if (accountDao.getCountAccountsWithLogin(user.getEmail()) > 0) {
                resultAccount = accountDao.getAccountByLogin(user.getEmail());
            } else {
                resultAccount = new AccountEntity();
                resultAccount.setLogin(user.getEmail());
                resultAccount.setEmail(user.getEmail());
                resultAccount.setFirstName(user.getFirstName());
                resultAccount.setLastName(user.getLastName());
                resultAccount.setMiddleName(user.getMiddleName());

                String password = UUID.randomUUID().toString();
                resultAccount.setPassword(password.substring(0, 7));
                createNewAccount(resultAccount);
                resultAccount.setActive(true);
                accountDao.update(resultAccount);
            }
            return resultAccount;
        } catch (Exception ex) {
            WebTesterLogger.info(LOGGER_NAME, "Can`t login in system with Facebook: " + user.getEmail());
            throw new UserWrongInputException("Can`t login in system with Facebook: " + user.getEmail());
        }

    }

    @Transactional(readOnly = false)
    public void createNewAccount(AccountEntity newAccount) {
        try {
            checkAllRequirements(newAccount);

            newAccount.setEmailVerified(false);
            newAccount.setActive(false);
            newAccount.addRoleToRoleSet(roleDao.getById(ID_ROLE_STUDENT));
            newAccount.setCreated(new Timestamp(new Date().getTime()));

            accountDao.save(newAccount);

            AccountVerificationEntity accountVerificationEntity = new AccountVerificationEntity();
            accountVerificationEntity.setIdAccount(newAccount.getIdAccount());
            accountVerificationEntity.setCode(UUID.randomUUID().toString());
            accountVerificationDao.save(accountVerificationEntity);

            sendRegistrationMessage(newAccount, accountVerificationEntity.getCode());
            WebTesterLogger.info(LOGGER_NAME, "Create new account " + newAccount.getLogin());
        } catch (Exception ex) {
            WebTesterLogger.info(LOGGER_NAME, "Can't create account: " + newAccount.getLogin()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't create account: " + newAccount.getLogin()
                    + " - " + ex.getMessage());
        }
    }


    private void checkAllRequirements(AccountEntity newAccount) {
        if (newAccount.getLogin().isEmpty()) {
            throw new UserWrongInputException("Empty login");
        }
        if (!isUniqueLogin(newAccount, newAccount.getLogin())) {
            throw new UserWrongInputException("Login already exist");
        }
        if (newAccount.getPassword().isEmpty()) {
            throw new UserWrongInputException("Empty password");
        }
        if (newAccount.getEmail().isEmpty()) {
            throw new UserWrongInputException("Empty email");
        }
        if (!isUniqueEmail(newAccount, newAccount.getEmail())) {
            throw new UserWrongInputException("Email already exist");
        }
        if (!WebTesterCheckEmail.validate(newAccount.getEmail())) {
            throw new UserWrongInputException("Incorrect email");
        }
        if (newAccount.getFirstName().isEmpty()) {
            throw new UserWrongInputException("Empty first name");
        }
        if (newAccount.getLastName().isEmpty()) {
            throw new UserWrongInputException("Empty last name");
        }
    }

    private Boolean isUniqueLogin(AccountEntity accountEntity, String newLogin) {
        int count = accountDao.getCountAccountsWithLogin(newLogin);
        if (count != 0) {
            AccountEntity tempAccount = accountDao.getAccountByLogin(newLogin);
            if (accountEntity.getIdAccount() != tempAccount.getIdAccount()) {
                return false;
            }
        }
        return true;
    }

    private Boolean isUniqueEmail(AccountEntity accountEntity, String newEmail) {
        int count = accountDao.getCountAccountsWithEmail(newEmail);
        if (count != 0) {
            AccountEntity tempAccount = accountDao.getAccountByEmail(newEmail);
            if (accountEntity.getIdAccount() != tempAccount.getIdAccount()) {
                return false;
            }
        }
        return true;
    }

    @Transactional(readOnly = false)
    public void verifyAccount(String code) {
        try {
            if (accountVerificationDao.getCountRecordWithCode(code) != 1) {
                throw new UserWrongInputException("No such code in system " + code);
            }
            AccountVerificationEntity accountVerification = accountVerificationDao.getByCode(code);
            AccountEntity account = accountVerification.getAccount();
            accountVerificationDao.delete(accountVerification);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, -24);

            if (account.getCreated().before(calendar.getTime())) {
                accountDao.delete(account);
                throw new UserWrongInputException("Time is out. " + calendar.getTime());
            }

            account.setEmailVerified(true);
            account.setActive(true);
            accountDao.update(account);
        } catch (Exception ex) {
            WebTesterLogger.info(LOGGER_NAME, "Can't verify account - " + ex.getMessage());
            throw new UserWrongInputException("Can't verify account - " + ex.getMessage());
        }
    }

    public void backupPassword(String email) {
        try {
            if (!WebTesterCheckEmail.validate(email)) {
                throw new UserWrongInputException("Incorrect email");
            }
            if (accountDao.getCountAccountsWithEmail(email) != 1) {
                throw new UserWrongInputException("Unknown email");
            }
            AccountEntity account = accountDao.getAccountByEmail(email);
            sendRecoveryMessage(account);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't backup password - " + ex.getMessage());
            throw new UserWrongInputException("Can't backup password - " + ex.getMessage());
        }
    }

    public AccountEntity getAccountById(AccountEntity accountEntity, int idAccount) {
        try {
            checkAccountPermissionById(accountEntity, idAccount);
            AccountEntity resultAccount = accountDao.getAccountById(idAccount);
            return resultAccount;
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't get account by id: " + idAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get account by id: " + idAccount + " - " + ex.getMessage());
        }
    }

    public AccountEntity getAccountByIdWithoutCheck(int idAccount) {
        try {
            AccountEntity resultAccount = accountDao.getAccountById(idAccount);
            return resultAccount;
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't get account by id: " + idAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get account by id: " + idAccount + " - " + ex.getMessage());
        }
    }

    private void checkAccountPermissionById(AccountEntity currentAccount, int idAccount) {
        boolean result = false;
        for (RoleEntity tempRole : currentAccount.getRoleSet()) {
            if (tempRole.getIdRole() == WebTesterRole.ADMINISTRATOR.getId()) {
                result = true;
                break;
            }
        }
        if (currentAccount.getIdAccount() == idAccount) {
            result = true;
        }
        if (result == false) {
            throw new UserWrongInputException("Have NO permission");
        }
    }


    @Transactional(readOnly = false)
    public void setEnableAccountById(int id) {
        try {
            AccountEntity tempAccount = accountDao.getAccountById(id);
            tempAccount.setActive(true);
            accountDao.save(tempAccount);
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't set enable account by id: " + id + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't set enable account by id: " + id + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void setDisableAccountById(int id) {
        try {
            AccountEntity tempAccount = accountDao.getAccountById(id);
            tempAccount.setActive(false);
            accountDao.save(tempAccount);
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't set disable account by id: " + id + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't set disable account by id: " + id + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void addRoleToAccountById(int idAccount, int idRole) {
        try {
            AccountEntity tempAccount = accountDao.getAccountById(idAccount);
            tempAccount.addRoleToRoleSet(roleDao.getRoleById(idRole));
            accountDao.save(tempAccount);
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't add role: " + idRole + " to account: "
                    + idAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't add role: " + idRole + " to account: "
                    + idAccount + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void deleteRoleFromAccountById(int idAccount, int idRole) {
        try {
            AccountEntity tempAccount = accountDao.getAccountById(idAccount);
            tempAccount.deleteRoleFromRoleSet(roleDao.getRoleById(idRole));
            accountDao.save(tempAccount);
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't delete role: " + idRole + " to account: "
                    + idAccount + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete role: " + idRole + " to account: "
                    + idAccount + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void deleteUnverifiedAccount() {
        try {
            AccountEntity tempAccount;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, -24);
            for (AccountVerificationEntity tempAccountVerification : accountVerificationDao.getAll()) {
                tempAccount = tempAccountVerification.getAccount();
                if (tempAccount.isEmailVerified()) {
                    accountVerificationDao.delete(tempAccountVerification);
                    continue;
                }
                if (tempAccount.getCreated().before(calendar.getTime())) {
                    if (!tempAccount.isActive()) {
                        WebTesterLogger.info(LOGGER_NAME, "delete none verified account " + tempAccount.getLogin());
                        accountDao.delete(tempAccount);
                    } else {
                        accountVerificationDao.delete(tempAccountVerification);
                    }
                }
            }
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't delete UnverifiedAccount - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete UnverifiedAccount - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void updateAccount(AccountEntity account) {
        try {
            checkAllRequirements(account);
            accountDao.update(account);
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't update account id: " + account.getIdAccount()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't update account id: " + account.getIdAccount()
                    + " - " + ex.getMessage());
        }
    }

    @Transactional(readOnly = false)
    public void deleteAccountById(int id) {
        try {
            accountDao.delete(accountDao.getAccountById(id));
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't delete account id: " + id + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't delete account id: " + id + " - " + ex.getMessage());
        }
    }

    public List<AccountEntity> getPartOfAccount(int offset, int limit) {
        try {
            List<AccountEntity> resultList = accountDao.getFilteredAndSortedList(offset, limit, sortAndRestrict);
            return resultList;
        } catch (Exception ex) {
            WebTesterLogger.error(LOGGER_NAME, "Can't get accounts offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't get accounts offset: " + offset
                    + " - limit: " + limit + " - " + ex.getMessage());
        }
    }

    public void addDependence(String field, Object value) {
        sortAndRestrict.addEqualRestriction(field, value);
    }

    public void clearAllDependence() {
        sortAndRestrict.clearAllEqualRestriction();
    }

    public void deleteDependence(String field) {
        sortAndRestrict.deleteEqualRestriction(field);
    }

    public void addRestriction(String field, String value) {
        sortAndRestrict.addLikeRestriction(field, value);
    }

    public void clearAllRestriction() {
        sortAndRestrict.clearAllLikeRestriction();
    }

    public void deleteRestricton(String field) {
        sortAndRestrict.deleteLikeRestriction(field);
    }

    public void clearAllSort() {
        sortAndRestrict.clearAllSort();
    }

    public void setSort(String field) {
        sortAndRestrict.setSort(field);
    }

    public void nextSort(String field) {
        sortAndRestrict.nextSort(field);
    }

    public void deleteSort(String field) {
        sortAndRestrict.deleteSort(field);
    }


    private void sendRegistrationMessage(AccountEntity accountEntity, String code) {
        try {
            Resource resource = applicationContext.getResource("resources/email/registration.email");
            String emailText = readFromResource(resource);
            Map<String, String> params = new HashMap<String, String>();
            params.put("user", accountEntity.getFirstName());
            params.put("password", accountEntity.getPassword());
            params.put("login", accountEntity.getLogin());
            params.put("host_context", webHost + "/" + webContext + "/verifyAccountAction");
            params.put("code", code);
            emailText = resolveVariables(emailText, params);

            MimeMessageHelper message = new MimeMessageHelper(defaultMailSender.createMimeMessage(), false);
            message.setSubject(WebTesterConstants.EMAIL_REGISTRATION);
            message.setTo(new InternetAddress(accountEntity.getEmail(), accountEntity.getFirstName()));
            message.setFrom(new InternetAddress(WebTesterConstants.EMAIL_ADDRESS, WebTesterConstants.EMAIL_NAME));
            message.setText(emailText, true);
            MimeMailMessage mimeMailMessage = new MimeMailMessage(message);
            defaultMailSender.send(mimeMailMessage.getMimeMessage());
            WebTesterLogger.info(LOGGER_NAME, "Sent registration email: " + accountEntity.getEmail());
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't sent registration email: " + accountEntity.getEmail()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't sent registration email: " + accountEntity.getEmail()
                    + " - " + ex.getMessage());
        }
    }

    private void sendRecoveryMessage(AccountEntity accountEntity) {
        try {
            Resource resource = applicationContext.getResource("resources/email/recovery.email");
            String emailText = readFromResource(resource);
            Map<String, String> params = new HashMap<String, String>();
            params.put("user", accountEntity.getFirstName());
            params.put("password", accountEntity.getPassword());
            params.put("login", accountEntity.getLogin());
            emailText = resolveVariables(emailText, params);

            MimeMessageHelper message = new MimeMessageHelper(defaultMailSender.createMimeMessage(), false);
            message.setSubject(WebTesterConstants.EMAIL_RECOVERY);
            message.setTo(new InternetAddress(accountEntity.getEmail(), accountEntity.getFirstName()));
            message.setFrom(new InternetAddress(WebTesterConstants.EMAIL_ADDRESS, WebTesterConstants.EMAIL_NAME));
            message.setText(emailText, true);
            MimeMailMessage mimeMailMessage = new MimeMailMessage(message);
            defaultMailSender.send(mimeMailMessage.getMimeMessage());
            WebTesterLogger.info(LOGGER_NAME, "Sent recovery email: " + accountEntity.getEmail());
        } catch (Exception ex) {
            WebTesterLogger.warn(LOGGER_NAME, "Can't sent recovery email: " + accountEntity.getEmail()
                    + " - " + ex.getMessage());
            throw new UserWrongInputException("Can't sent recovery email: " + accountEntity.getEmail()
                    + " - " + ex.getMessage());
        }
    }

    private String readFromResource(Resource resource) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = resource.getFile();
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    stringBuilder.append(s);
                    stringBuilder.append("\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    private String resolveVariables(String text, Map<String, String> params) {
        String result = text;
        for (Map.Entry<String, String> entity : params.entrySet()) {
            String var = "${" + entity.getKey() + "}";
            String value = entity.getValue() == null ? "" : entity.getValue();
            result = result.replace(var, value);
        }
        return result;
    }

}
