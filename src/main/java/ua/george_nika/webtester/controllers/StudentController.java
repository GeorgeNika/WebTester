package ua.george_nika.webtester.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.george_nika.webtester.entity.*;
import ua.george_nika.webtester.errors.UserWrongInputException;
import ua.george_nika.webtester.forms.StudentAnswerForm;
import ua.george_nika.webtester.services.*;
import ua.george_nika.webtester.util.WebTesterConstants;
import ua.george_nika.webtester.util.WebTesterLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.Calendar;

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

    @Autowired
    SortAndRestrictUtil sortAndRestrictUtil;


    // *****************
    //   Page section
    // *****************

    @RequestMapping("/mainPage")
    public String mainPage(HttpServletRequest request, HttpSession session, Model model,
                           @RequestParam(value = "sort", required = false) String sort,
                           @RequestParam(value = "page", required = false) String pageDirection,
                           @RequestParam(value = "idLike", required = false) String idLike,
                           @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            testService.clearAllEqualRestriction();
            testService.addTestActiveEqualRestriction(true);

            sortAndRestrictUtil.setService(testService);
            sortAndRestrictUtil.setSessionPageProperty(WebTesterConstants.SESSION_SHOW_TEST_STUDENT_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            model.addAttribute("testList", testService.getPartOfTest(
                    (showPage - 1) * WebTesterConstants.ROW_ON_PAGE, showPage * WebTesterConstants.ROW_ON_PAGE));
            model.addAttribute("pageNumber", showPage);

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
            return mainPage(request, session, model, null, null, null, null);
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

            List<AnswerEntity> answerList = questionEntity.getAnswerList();
            Collections.shuffle(answerList);
            model.addAttribute("studentAnswerForm", new StudentAnswerForm(answerList));
            model.addAttribute("question", questionEntity.getQuestion());
            session.setAttribute(WebTesterConstants.SESSION_TIME_START_QUESTION, Calendar.getInstance().getTime());
            request.setAttribute("namePage", questionEntity.getTest().getName());
            return "/student/nextQuestion";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show next question " + " - " + ex.getMessage());
            return mainPage(request, session, model, null, null, null, null);
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
            return mainPage(request, session, model, null, null, null, null);
        }
    }

    @RequestMapping("/viewResultPage/{idAccount}")
    public String viewResultPage(HttpServletRequest request,
                                 HttpSession session,
                                 Model model,
                                 @PathVariable("idAccount") int idAccount,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "page", required = false) String pageDirection,
                                 @RequestParam(value = "idLike", required = false) String idLike,
                                 @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            if (accountEntity.getIdAccount() != idAccount) {
                throw new UserWrongInputException("Have NO permission");
            }

            sortAndRestrictUtil.setService(resultService);
            sortAndRestrictUtil.setSessionPageProperty(WebTesterConstants.SESSION_SHOW_RESULT_STUDENT_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            model.addAttribute("testResultList", resultService.getPartOfResultByAccount(accountEntity,
                    (showPage - 1) * WebTesterConstants.ROW_ON_PAGE, showPage * WebTesterConstants.ROW_ON_PAGE));
            model.addAttribute("idAccount", idAccount);
            model.addAttribute("pageNumber", showPage);
            request.setAttribute("namePage", "VIEW   RESULT");
            return "student/viewResult";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show result id: " + idAccount
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't show result id: " + idAccount + " - " + ex.getMessage());
            return mainPage(request, session, model, null, null, null, null);
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
            return mainPage(request, session, model, null, null, null, null);
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
            if (answerFromStudent == null) {
                answerFromStudent = new ArrayList<Integer>();
            }
            //check Time add 5% + 1sec;
            Calendar calendar = Calendar.getInstance();
            Double durationInSec =
                    (Integer) session.getAttribute(WebTesterConstants.SESSION_DURATION_FOR_QUESTION) / 1000 * 1.05;
            durationInSec = durationInSec + 1;
            int timeInterval = durationInSec.intValue();
            calendar.add(Calendar.SECOND, -timeInterval);
            Date startQuestionTime =
                    (Date) session.getAttribute(WebTesterConstants.SESSION_TIME_START_QUESTION);
            if (startQuestionTime.before(calendar.getTime())) {
                request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Time for question is out.");
                answerFromStudent.clear();
            }

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
            return mainPage(request, session, model, null, null, null, null);
        }
    }


}