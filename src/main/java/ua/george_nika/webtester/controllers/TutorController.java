package ua.george_nika.webtester.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.george_nika.webtester.entity.*;
import ua.george_nika.webtester.forms.AnswerForm;
import ua.george_nika.webtester.forms.QuestionForm;
import ua.george_nika.webtester.forms.TestForm;
import ua.george_nika.webtester.services.*;
import ua.george_nika.webtester.util.WebTesterConstants;
import ua.george_nika.webtester.util.WebTesterLogger;
import ua.george_nika.webtester.util.WebTesterRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tutor")
public class TutorController {
    private static String LOGGER_NAME = TutorController.class.getSimpleName();

    @Autowired
    private TestService testService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private ResultService resultService;

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
            if (((RoleEntity) session.getAttribute(WebTesterConstants.SESSION_ROLE)).getIdRole()
                    == WebTesterRole.TUTOR.getId()) {
                testService.addTestAccountEqualRestriction((AccountEntity) session.getAttribute(
                        WebTesterConstants.SESSION_ACCOUNT));
            } else {
                testService.deleteTestAccountEqualRestriction();
            }

            sortAndRestrictUtil.setService(testService);
            sortAndRestrictUtil.setSessionPageProperty(WebTesterConstants.SESSION_SHOW_TEST_TUTOR_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            model.addAttribute("testList", testService.getPartOfTest(
                    (showPage - 1) * WebTesterConstants.ROW_ON_PAGE, showPage * WebTesterConstants.ROW_ON_PAGE));
            model.addAttribute("pageNumber", showPage);

            request.setAttribute("namePage", "TUTOR   MAIN   PAGE");
            return "/tutor/main";
        } catch (Exception ex) {
            return "redirect:/welcomeAction";
        }
    }

    @RequestMapping("/createNewTestPage")
    public String createNewTestPage(HttpServletRequest request, Model model) {
        model.addAttribute("createNewTestForm", new TestForm());
        request.setAttribute("namePage", "NEW   TEST");
        return "tutor/createTest";
    }

    @RequestMapping("/editTestPage/{idTest}")
    public String editTestPage(HttpServletRequest request,
                               HttpSession session,
                               Model model,
                               @PathVariable("idTest") int idTest,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "page", required = false) String pageDirection,
                               @RequestParam(value = "idLike", required = false) String idLike,
                               @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            TestEntity testEntity = testService.getTestById(accountEntity, idTest);
            request.setAttribute("editTest", testEntity);

            sortAndRestrictUtil.setService(questionService);
            sortAndRestrictUtil.setSessionPageProperty(WebTesterConstants.SESSION_SHOW_QUESTION_TUTOR_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            model.addAttribute("questionList", questionService.getPartOfQuestion(testEntity,
                    (showPage - 1) * WebTesterConstants.ROW_ON_PAGE, showPage * WebTesterConstants.ROW_ON_PAGE));
            model.addAttribute("pageNumber", showPage);
            model.addAttribute("idTest", idTest);

            model.addAttribute("editTestForm", new TestForm(testEntity));
            request.setAttribute("namePage", "EDIT   TEST");
            return "tutor/editTest";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show test id: " + idTest
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't show test id: " + idTest + " - " + ex.getMessage());
            return mainPage(request, session, model, null, null, null, null);
        }
    }

    @RequestMapping("/createNewQuestionPage/{idTest}")
    public String createNewQuestionPage(HttpServletRequest request,
                                        HttpSession session,
                                        Model model,
                                        @PathVariable("idTest") int idTest) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            TestEntity testEntity = testService.getTestById(accountEntity, idTest);
            request.setAttribute("editTest", testEntity);
            model.addAttribute("createNewQuestionForm", new QuestionForm());
            request.setAttribute("namePage", "NEW   QUESTION");
            return "tutor/createQuestion";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't create new question test id: " + idTest
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't create new question test id: " + idTest
                    + " - " + ex.getMessage());
            return editTestPage(request, session, model, idTest, null, null, null, null);
        }

    }

    @RequestMapping("/editQuestionPage/{idTest}/{idQuestion}")
    public String editQuestionPage(HttpServletRequest request,
                                   HttpSession session,
                                   Model model,
                                   @PathVariable("idTest") int idTest,
                                   @PathVariable("idQuestion") int idQuestion) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            QuestionEntity questionEntity = questionService.getQuestionById(accountEntity, idQuestion);
            request.setAttribute("editTest", questionEntity.getTest());
            request.setAttribute("editQuestion", questionEntity);
            model.addAttribute("editAnswerForm", new AnswerForm(questionEntity.getAnswerList()));
            model.addAttribute("editQuestionForm", new QuestionForm(questionEntity));
            request.setAttribute("namePage", "EDIT   QUESTION");
            return "tutor/editQuestion";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show question id: " + idQuestion
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't show question id: " + idQuestion + " - " + ex.getMessage());
            return editTestPage(request, session, model, idTest, null, null, null, null);
        }
    }

    @RequestMapping("/viewResultPage/{idTest}")
    public String viewResultPage(HttpServletRequest request,
                                 HttpSession session,
                                 Model model,
                                 @PathVariable("idTest") int idTest,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "page", required = false) String pageDirection,
                                 @RequestParam(value = "idLike", required = false) String idLike,
                                 @RequestParam(value = "nameLike", required = false) String nameLike) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            TestEntity testEntity = testService.getTestById(accountEntity, idTest);

            sortAndRestrictUtil.setService(resultService);
            sortAndRestrictUtil.setSessionPageProperty(WebTesterConstants.SESSION_SHOW_RESULT_TUTOR_PAGE);
            sortAndRestrictUtil.executeSortBlock(sort);
            int showPage = sortAndRestrictUtil.getShowPage(session, pageDirection);
            sortAndRestrictUtil.setShowPageToSession(session, showPage);
            sortAndRestrictUtil.executeLikeBlock(model, idLike, nameLike);

            model.addAttribute("testResultList", resultService.getPartOfResultByTest(testEntity,
                    (showPage - 1) * WebTesterConstants.ROW_ON_PAGE, showPage * WebTesterConstants.ROW_ON_PAGE));
            model.addAttribute("pageNumber", showPage);
            model.addAttribute("idTest", idTest);

            request.setAttribute("namePage", "SHOW   RESULT");
            return "tutor/viewResult";
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't show result id: " + idTest
                    + " - " + ex.getMessage());
            WebTesterLogger.error(LOGGER_NAME, "Can't show result id: " + idTest + " - " + ex.getMessage());
            return mainPage(request, session, model, null, null, null, null);
        }
    }
    // *****************
    //   Action section
    // *****************

    @RequestMapping("/createNewTestAction")
    public String createNewTestAction(HttpServletRequest request,
                                      HttpSession session,
                                      Model model,
                                      @ModelAttribute("createNewTestForm") TestForm createNewTestForm) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            TestEntity testEntity = new TestEntity();
            testEntity.setName(createNewTestForm.getName());
            testEntity.setComment(createNewTestForm.getComment());
            testEntity.setDurationInSecond(Integer.parseInt(createNewTestForm.getTime()));

            testService.createNewTest(accountEntity, testEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Test successfully created. Test empty and disable.");
            return mainPage(request, session, model, null, null, null, null);
        } catch (Exception ex) {
            model.addAttribute("createNewTestForm", createNewTestForm);
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't create new test - " + ex.getMessage());
            request.setAttribute("namePage", "NEW   TEST");
            return "tutor/createTest";
        }
    }

    @RequestMapping("/enableTestAction/{idTest}")
    public String enableTestAction(HttpServletRequest request,
                                   HttpSession session,
                                   Model model,
                                   @PathVariable("idTest") int idTest) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            testService.setEnableTestById(accountEntity, idTest);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editTestPage(request, session, model, idTest, null, null, null, null);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update test - " + ex.getMessage());
            return editTestPage(request, session, model, idTest, null, null, null, null);
        }
    }

    @RequestMapping("/disableTestAction/{idTest}")
    public String disableTestAction(HttpServletRequest request,
                                    HttpSession session,
                                    Model model,
                                    @PathVariable("idTest") int idTest) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            testService.setDisableTestById(accountEntity, idTest);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editTestPage(request, session, model, idTest, null, null, null, null);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update test - " + ex.getMessage());
            return editTestPage(request, session, model, idTest, null, null, null, null);
        }
    }

    @RequestMapping("/deleteTestAction/{idTest}")
    public String deleteTestAction(HttpServletRequest request,
                                   HttpSession session,
                                   Model model,
                                   @PathVariable("idTest") int idTest) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            testService.deleteTestById(accountEntity, idTest);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Test deleted.");
            return mainPage(request, session, model, null, null, null, null);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't delete test - " + ex.getMessage());
            return editTestPage(request, session, model, idTest, null, null, null, null);
        }
    }

    @RequestMapping("/editTestAction/{idTest}")
    public String editTestAction(HttpServletRequest request,
                                 HttpSession session,
                                 Model model,
                                 @ModelAttribute("editTestForm") TestForm editTestForm,
                                 @PathVariable("idTest") int idTest) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            TestEntity tempTestEntity = testService.getTestById(accountEntity, idTest);
            editTestForm.updateTest(tempTestEntity);
            testService.updateTest(accountEntity, tempTestEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editTestPage(request, session, model, idTest, null, null, null, null);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update test - " + ex.getMessage());
            return editTestPage(request, session, model, idTest, null, null, null, null);
        }
    }

    @RequestMapping("/createNewQuestionAction/{idTest}")
    public String createNewQuestionAction(HttpServletRequest request,
                                          HttpSession session,
                                          Model model,
                                          @ModelAttribute("createNewQuestionForm") QuestionForm createNewQuestionForm,
                                          @PathVariable("idTest") int idTest) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            TestEntity testEntity = testService.getTestById(accountEntity, idTest);
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setQuestion(createNewQuestionForm.getQuestion());

            questionService.createQuestion(accountEntity, testEntity, questionEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Question successfully created.");
            return editTestPage(request, session, model, idTest, null, null, null, null);
        } catch (Exception ex) {
            model.addAttribute("createNewQuestionForm", createNewQuestionForm);
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't create new question - " + ex.getMessage());
            request.setAttribute("namePage", "NEW   QUESTION");
            return "tutor/createQuestion";
        }
    }

    @RequestMapping("/enableQuestionAction/{idTest}/{idQuestion}")
    public String enableQuestionAction(HttpServletRequest request,
                                       HttpSession session,
                                       Model model,
                                       @ModelAttribute("editQuestionForm") QuestionForm editQuestionForm,
                                       @ModelAttribute("editAnswerForm") AnswerForm editAnswerForm,
                                       @PathVariable("idTest") int idTest,
                                       @PathVariable("idQuestion") int idQuestion) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            questionService.setEnableQuestionById(accountEntity, idQuestion);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editQuestionPage(request, session, model, idTest, idQuestion);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update question - " + ex.getMessage());
            return editQuestionPage(request, session, model, idTest, idQuestion);
        }
    }

    @RequestMapping("/disableQuestionAction/{idTest}/{idQuestion}")
    public String disableQuestionAction(HttpServletRequest request,
                                        HttpSession session,
                                        Model model,
                                        @ModelAttribute("editQuestionForm") QuestionForm editQuestionForm,
                                        @ModelAttribute("editAnswerForm") AnswerForm editAnswerForm,
                                        @PathVariable("idTest") int idTest,
                                        @PathVariable("idQuestion") int idQuestion) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            questionService.setDisableQuestionById(accountEntity, idQuestion);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editQuestionPage(request, session, model, idTest, idQuestion);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update question - " + ex.getMessage());
            return editQuestionPage(request, session, model, idTest, idQuestion);
        }
    }

    @RequestMapping("/deleteQuestionAction/{idTest}/{idQuestion}")
    public String deleteQuestionAction(HttpServletRequest request,
                                       HttpSession session,
                                       Model model,
                                       @ModelAttribute("editQuestionForm") QuestionForm editQuestionForm,
                                       @ModelAttribute("editAnswerForm") AnswerForm editAnswerForm,
                                       @PathVariable("idTest") int idTest,
                                       @PathVariable("idQuestion") int idQuestion) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            questionService.deleteQuestionById(accountEntity, idQuestion);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Delete successful.");
            return editTestPage(request, session, model, idTest, null, null, null, null);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update question - " + ex.getMessage());
            return editQuestionPage(request, session, model, idTest, idQuestion);
        }
    }

    @RequestMapping("/editQuestionAction/{idTest}/{idQuestion}")
    public String editQuestionAction(HttpServletRequest request,
                                     HttpSession session,
                                     Model model,
                                     @ModelAttribute("editQuestionForm") QuestionForm editQuestionForm,
                                     @ModelAttribute("editAnswerForm") AnswerForm editAnswerForm,
                                     @PathVariable("idTest") int idTest,
                                     @PathVariable("idQuestion") int idQuestion) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            QuestionEntity questionEntity = questionService.getQuestionById(accountEntity, idQuestion);
            editQuestionForm.updateQuestion(questionEntity);
            questionService.updateQuestion(accountEntity, questionEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Update successful.");
            return editQuestionPage(request, session, model, idTest, idQuestion);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update question - " + ex.getMessage());
            return editQuestionPage(request, session, model, idTest, idQuestion);
        }
    }

    @RequestMapping("/createNewAnswerAction/{idTest}/{idQuestion}")
    public String createNewAnswerAction(HttpServletRequest request,
                                        HttpSession session,
                                        Model model,
                                        @ModelAttribute("editQuestionForm") QuestionForm editQuestionForm,
                                        @ModelAttribute("editAnswerForm") AnswerForm editAnswerForm,
                                        @PathVariable("idTest") int idTest,
                                        @PathVariable("idQuestion") int idQuestion) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            QuestionEntity questionEntity = questionService.getQuestionById(accountEntity, idQuestion);
            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setAnswer("new answer");
            answerEntity.setCheckRight(false);
            answerService.createNewAnswer(accountEntity, questionEntity, answerEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Answer successfully created.");
            return editQuestionPage(request, session, model, idTest, idQuestion);
        } catch (Exception ex) {
            model.addAttribute("editQuestionForm", editQuestionForm);
            model.addAttribute("editAnswerForm", editAnswerForm);
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't create new answer - " + ex.getMessage());
            return editQuestionPage(request, session, model, idTest, idQuestion);
        }
    }

    @RequestMapping("/deleteAnswerAction/{idTest}/{idQuestion}/{idAnswer}")
    public String deleteAnswerAction(HttpServletRequest request,
                                     HttpSession session,
                                     Model model,
                                     @ModelAttribute("editQuestionForm") QuestionForm editQuestionForm,
                                     @ModelAttribute("editAnswerForm") AnswerForm editAnswerForm,
                                     @PathVariable("idTest") int idTest,
                                     @PathVariable("idQuestion") int idQuestion,
                                     @PathVariable("idAnswer") int idAnswer) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            answerService.deleteAnswerById(accountEntity, idAnswer);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Delete successful.");
            return editQuestionPage(request, session, model, idTest, idQuestion);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't delete answer - " + ex.getMessage());
            return editQuestionPage(request, session, model, idTest, idQuestion);
        }
    }


    @RequestMapping("/editAnswerAction/{idTest}/{idQuestion}")
    public String editAnswerAction(HttpServletRequest request,
                                   HttpSession session,
                                   Model model,
                                   @ModelAttribute("editQuestionForm") QuestionForm editQuestionForm,
                                   @ModelAttribute("editAnswerForm") AnswerForm editAnswerForm,
                                   @PathVariable("idTest") int idTest,
                                   @PathVariable("idQuestion") int idQuestion) {
        try {
            AccountEntity accountEntity = (AccountEntity) session.getAttribute(WebTesterConstants.SESSION_ACCOUNT);
            QuestionEntity questionEntity = questionService.getQuestionById(accountEntity, idQuestion);
            editAnswerForm.updateAnswerList(questionEntity.getAnswerList());
            questionService.updateQuestion(accountEntity, questionEntity);
            request.setAttribute(WebTesterConstants.REQUEST_INFO, "Answers successfully created.");
            return editQuestionPage(request, session, model, idTest, idQuestion);
        } catch (Exception ex) {
            request.setAttribute(WebTesterConstants.REQUEST_ERROR, "Can't update answers - " + ex.getMessage());
            return editQuestionPage(request, session, model, idTest, idQuestion);
        }
    }
}
