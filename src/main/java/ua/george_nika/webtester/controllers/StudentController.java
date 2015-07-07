package ua.george_nika.webtester.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.george_nika.webtester.entity.*;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.forms.StudentAnswerForm;
import ua.george_nika.webtester.services.AccountService;
import ua.george_nika.webtester.services.QuestionService;
import ua.george_nika.webtester.services.ResultService;
import ua.george_nika.webtester.services.TestService;
import ua.george_nika.webtester.util.WebTesterConstants;
import ua.george_nika.webtester.util.WebTesterLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by George on 18.06.2015.
 */
@Controller
@RequestMapping("/student")
public class StudentController {
    private static String LOGGER_NAME = StudentController.class.getSimpleName();

    @Autowired
    AccountService accountService;

    @Autowired
    TestService testService;

    @Autowired
    QuestionService questionService;

    @Autowired
    ResultService resultService;

    // *****************
    //   Page section
    // *****************

    @RequestMapping("/mainPage")
    public String mainPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            //todo need paging
            testService.clearAllDependence();
            testService.addDependence("active", true);
            model.addAttribute("testList", testService.getPartOfTest(0, 100));
            request.setAttribute("namePage", "STUDENT   MAIN   PAGE");
            return "/student/main";
        } catch (Exception ex) {
            return "redirect:/welcomeAction";
        }
    }

    @RequestMapping("/beginTestPage/{idTest}")
    public String beginTestPage(HttpServletRequest request,
                                HttpSession session,
                                Model model,
                                @PathVariable("idTest") int idTest) {
        try {
            TestEntity testEntity = testService.getTestByIdWithoutCheck(idTest);
            request.setAttribute("editTest", testEntity);
            request.setAttribute("namePage", "BEGIN   TEST");
            return "student/beginTest";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't begin test id: " + idTest
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't begin test id: " + idTest + " - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/nextQuestionPage")
    public String nextQuestionPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            LinkedList<Integer> idQuestionList =
                    (LinkedList) session.getAttribute(WebTesterConstants.SESSION_ALL_ID_QUESTION_LIST);
            if (idQuestionList.size() == 0) {
                return endTestPage(request, session, model);
            }
            QuestionEntity questionEntity = questionService.getQuestionByIdWithoutCheck(idQuestionList.getFirst());

            model.addAttribute("studentAnswerForm", new StudentAnswerForm(questionEntity.getAnswerList()));
            model.addAttribute("question", questionEntity.getQuestion());
            request.setAttribute("namePage", questionEntity.getTest().getName());
            return "/student/nextQuestion";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show next question " + " - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/endTestPage")
    public String endTestPage(HttpServletRequest request, HttpSession session, Model model) {
        try {
            ResultEntity resultEntity = (ResultEntity) session.getAttribute(WebTesterConstants.SESSION_RESULT);
            model.addAttribute("result", resultEntity);
            request.setAttribute("namePage", "END   TEST");
            return "/student/endTest";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show end test " + " - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/viewResultPage/{idAccount}")
    public String viewResultPage(HttpServletRequest request,
                                 HttpSession session,
                                 Model model,
                                 @PathVariable("idAccount") int idAccount) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            if (accountEntity.getIdAccount() != idAccount) {
                throw new UserWrongInputException("Have NO permission");
            }
            //todo paging
            model.addAttribute("testResultList", resultService.getPartOfResultByAccount(accountEntity, 0, 100));
            request.setAttribute("namePage", "SHOW   RESULT");
            return "student/viewResult";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show result id: " + idAccount
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't show result id: " + idAccount + " - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    // *****************
    //   Action section
    // *****************

    @RequestMapping("/beginTestAction/{idTest}")
    public String beginTestAction(HttpServletRequest request,
                                  HttpSession session,
                                  Model model,
                                  @PathVariable("idTest") int idTest) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            TestEntity testEntity = testService.getTestByIdWithoutCheck(idTest);
            List<Integer> idQuestionList = new LinkedList<Integer>();
            for (QuestionEntity tempQuestion : testEntity.getQuestionList()) {
                if (tempQuestion.isActive()) {
                    idQuestionList.add(tempQuestion.getIdQuestion());
                }
            }
            Collections.shuffle(idQuestionList);
            ResultEntity newResult = new ResultEntity();
            resultService.createNewResult(accountEntity, testEntity, newResult);
            session.setAttribute(WebTesterConstants.SESSION_RESULT, newResult);
            session.setAttribute(WebTesterConstants.SESSION_ALL_ID_QUESTION_LIST, idQuestionList);
            session.setAttribute(WebTesterConstants.SESSION_DURATION_FOR_QUESTION,
                    (testEntity.getDurationInSecond() * 1000));
            return nextQuestionPage(request, session, model);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't begin test id: " + idTest
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't begin test id: " + idTest + " - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }

    @RequestMapping("/nextQuestionAction")
    public String nextQuestionAction(HttpServletRequest request,
                                     HttpSession session,
                                     Model model,
                                     @ModelAttribute("studentAnswerForm") StudentAnswerForm studentAnswerForm) {

        try {
            //check answer
            LinkedList<Integer> idQuestionList =
                    (LinkedList<Integer>) session.getAttribute(WebTesterConstants.SESSION_ALL_ID_QUESTION_LIST);
            QuestionEntity questionEntity = questionService.getQuestionByIdWithoutCheck(idQuestionList.getFirst());
            int countRightAnswers = 0;
            int countGoodAnswers = 0;
            int countBadAnswers = 0;
            List<Integer> answerFromStudent = studentAnswerForm.getStudentAnswerList();
            int tempCount;
            for (AnswerEntity tempAnswer : questionEntity.getAnswerList()) {
                if (answerFromStudent.contains(tempAnswer.getIdAnswer())) {
                    tempCount = 1; // if student check this answer
                } else {
                    tempCount = 0;
                }
                if (tempAnswer.isCheckRight()) {
                    countRightAnswers = countRightAnswers + 1;
                    countGoodAnswers = countGoodAnswers + tempCount;  // if student check this answer
                } else {
                    countBadAnswers = countBadAnswers + tempCount; // if student check this answer
                }
            }

            // save information in Database
            ResultEntity resultEntity = (ResultEntity) session.getAttribute(WebTesterConstants.SESSION_RESULT);
            resultEntity.setCountQuestion(resultEntity.getCountQuestion() + 1);
            resultEntity.setAllRightAnswer(resultEntity.getAllRightAnswer() + countRightAnswers);
            resultEntity.setRightAnswer(resultEntity.getRightAnswer() + countGoodAnswers);
            resultEntity.setWrongAnswer(resultEntity.getWrongAnswer() + countBadAnswers);
            resultService.updateResult(resultEntity);

            idQuestionList.removeFirst();
            if (idQuestionList.size() == 0) {
                return endTestPage(request, session, model);
            }
            return nextQuestionPage(request, session, model);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't continue test " + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't continue test " + " - " + ex.getMessage());
            return mainPage(request, session, model);
        }
    }


}