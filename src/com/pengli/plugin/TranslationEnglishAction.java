package com.pengli.plugin;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by pengli on 16/6/2.
 */
public class TranslationEnglishAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        String userName = askForName(project);
        String text = "";
        try {
            text = URLEncoder.encode(userName,"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = "http://fanyi.youdao.com/openapi.do?keyfrom=pengli&key=991847317&type=data&doctype=json&version=1.1&q="+text;
        String result = HttpUtils.sendGet(url);
        Gson gson = new Gson();
        TranslationBean translation = gson.fromJson(result, TranslationBean.class);
        switch (translation.getErrorCode()){
            case 0:
                translate(project, translation.toString());
                break;
            case 20:
                translate(project, "要翻译的文本过长");
                break;
            case 30:
                translate(project, "无法进行有效的翻译");
                break;
            case 40:
                translate(project, "不支持的语言类型");
                break;
            case 50:
                translate(project, "无效的key");
                break;
            case 60:
                translate(project, "无词典结果，仅在获取词典结果生效");
                break;
            default:
                translate(project, "请稍后再试");
                break;

        }

    }

    private String askForName(Project project) {
        return Messages.showInputDialog(project,
                "输入你所需要翻译的中文", "Input Your word",
                Messages.getQuestionIcon());
    }

    private void translate(Project project, String userName) {

        Messages.showMessageDialog(project,
                userName, "Question",
                Messages.getInformationIcon());
    }
}
